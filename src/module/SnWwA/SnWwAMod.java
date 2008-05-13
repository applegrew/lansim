/*
 * SnWwAMod.java
 *
 * Copyright (C) 2008 AppleGrew
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 */
package module.SnWwA;

import java.awt.Component;
import java.awt.Container;
import java.lang.String;
import java.util.ArrayList;

import framework.Packet;
import framework.Port;
import module.ModuleUI;

/**
 * This module is to be used for testing and demonstrating Stop-and-Wait ARQ protocol.<br>
 * <i>(Ref: Data and Computer Communication; William Stallings; 7th Ed.)</i><br>
 * The weird <b>SnWwA</b> stands for <b>S</b>top a<b>n</b>d <b>W</b>ait <b>w</b>ith <b>A</b>cknowledgement.
 * 
 * @author Nirupam
 * 
 */
public class SnWwAMod implements module.Module {
    static final int	  MAXPORTS	   = 1;
    volatile boolean	  canEmitData	= false;
    int		       snd_targetSlNo     = 2;
    int		       ack_targetSlNo     = 2;
    int		       snd_ackno	  = 0;	// Received ack no
    int		       snd_frameno	= -1;       // Sent frame no
    int		       rcv_ackno	  = 0;	// Received ack no
    int		       rcv_frameno	= -1;       // Sent frame no
    int		       sendTimer	  = 90;
    int		       noOfPorts	  = 0;
    private int	       timeoutTimer;		  // timeoutTimer counts down. It then resets to sendTimer value.
    private int	       serialNo	   = 0;
    double		    elapsedTime	= 0;
    double		    absoluteTime       = 0;
    private boolean	   newPacket	  = false;
    private boolean	   snd_receivedPacket = false;
    private boolean	   rcv_receivedPacket = false;
    private Port	      ports[];
    private final String      name	       = "SW Node";
    final double	      LATENCY	    = 0;
    private ModuleUI	  modUI	      = null;
    private ArrayList<Packet> iPhysicalBuffer;
    private ArrayList<Packet> oPhysicalBuffer;

    public SnWwAMod() {
	ports = new Port[MAXPORTS];
	buffer.EditMode.module = this;
	modUI = new SnWwAUI(this);
	iPhysicalBuffer = new ArrayList<Packet>();
	oPhysicalBuffer = new ArrayList<Packet>();
	resetSendTimer();
    }

    /**
     * Adds a new port (connection)
     * 
     * @return The error code if any. e.g. ERROR_ALREADY_MAX_ports is returned if no more ports of type wireType can be handeled by this module.
     */
    public int addPort(Port port, int wireType) {
	if (noOfPorts < MAXPORTS) {
	    port.setActive(true);
	    ports[noOfPorts++] = port;
	    System.out.println("ADDED port " + noOfPorts);
	    return 1;
	} else
	    return ERROR_ALREADY_MAX_PORTS;
    }

    /**
     * If the implementing class doesn't want to extend or create an object of ModuleUI then return null.
     * 
     * @return
     */
    public ModuleUI getModuleUI() {
	return modUI;
    }

    public void setModuleUI(ModuleUI m) {
	modUI = m;
    }

    public String getName() {
	return name;
    }

    public int getSno() {
	return serialNo;
    }

    public void setSno(int n) {
	serialNo = n;
    }

    public boolean step(double currtime) {// boolean Success or failure
	stepPhysical(currtime);
	stepApplication(currtime);
	return true;
    }

    private boolean stepPhysical(double currtime) {
	Packet packet;
	for (int i = 0; i < noOfPorts; i++) {
	    if (ports[i].isActive() && ports[i].hasData() && !ports[i].isTransmiting()) {
		packet = ports[i].getPacket(this);
		if (packet.getToId() == serialNo) {
		    iPhysicalBuffer.add(packet);
		    modUI.getModWin().msg.append("\nPhysical-Received packet from " + packet.getFromId() + " on port " + i);
		    modUI.getModWin().msg.append("\n" + packet.getData() + "\n");
		}
	    }
	}
	for (int i = 0, portNo; i < oPhysicalBuffer.size(); i++) {
	    portNo = oPhysicalBuffer.get(i).toPort;
	    if (ports[portNo] == null)
		oPhysicalBuffer.remove(i);
	    else if (ports[portNo].isActive() && !ports[portNo].hasData()) {
		ports[portNo].putPacket(oPhysicalBuffer.get(i), this);
		newPacket = true;
		modUI.getModWin().msg.append("\nPhysical-Put packet on port " + portNo + "  to " + oPhysicalBuffer.get(i).getToId());
		oPhysicalBuffer.remove(i);
	    }
	}
	return true;
    }

    private boolean stepApplication(double currtime) {
	Packet packet = null;
	if (iPhysicalBuffer.size() > 0) {
	    packet = iPhysicalBuffer.get(0);
	    iPhysicalBuffer.remove(0);
	}
	absoluteTime++;
	if (canEmitData) { // TRANSMITTER MODE

	    if (packet != null && packet.getToId() == serialNo) {
		if (!packet.isCorrupt() && packet.getData() != null && packet.isReply) {
		    snd_ackno = Integer.parseInt(packet.getData());
		    modUI.getModWin().amsg.append("\nReceived ACK " + snd_ackno + " from " + packet.getFromId());
		    snd_receivedPacket = true;
		}
	    }
	    if (snd_frameno == -1 || snd_receivedPacket) {
		if (noOfPorts != 0) {
		    snd_frameno = snd_ackno;
		    oPhysicalBuffer.add(new Packet(snd_targetSlNo, serialNo, 10, String.valueOf(snd_frameno)));
		    resetSendTimer();
		    modUI.getModWin().amsg.append("\nSent data packet " + snd_frameno + " to " + snd_targetSlNo);
		    snd_receivedPacket = false;
		}
	    } else if (!snd_receivedPacket) { // Waiting for ACK
		timeoutTimer--;
		if (timeoutTimer <= 0) {
		    if (noOfPorts != 0) {
			oPhysicalBuffer.add(new Packet(snd_targetSlNo, serialNo, 10, String.valueOf(snd_frameno)));
			resetSendTimer();
			modUI.getModWin().amsg.append("\nTimer timed-out. Re-sent data packet " + snd_frameno + " to " + snd_targetSlNo);
		    }
		}
	    }

	}
	{ // RECEIVER MODE

	    if (packet != null && packet.getToId() == serialNo) {
		if (!packet.isCorrupt() && packet.getData() != null && !packet.isReply) {
		    int tframeno = Integer.parseInt(packet.getData());
		    if (tframeno == rcv_frameno)
			modUI.getModWin().amsg.append("\nReceived duplicate data packet " + tframeno + " from " + packet.getFromId()
				+ ". Discarding it.");
		    else {
			rcv_frameno = tframeno;
			modUI.getModWin().amsg.append("\nReceived data packet " + rcv_frameno + " from " + packet.getFromId());
		    }
		    ack_targetSlNo = (int) packet.getFromId();
		    rcv_receivedPacket = true;
		}
	    }
	    if (rcv_receivedPacket) {
		if (noOfPorts != 0) {
		    rcv_ackno = getNextSequenceNo(rcv_frameno);
		    Packet ack = new Packet(ack_targetSlNo, serialNo, 10, String.valueOf(rcv_ackno));
		    ack.isReply = true;
		    oPhysicalBuffer.add(ack);
		    modUI.getModWin().amsg.append("\nSent ack packet " + rcv_ackno + " to " + ack_targetSlNo);
		    rcv_receivedPacket = false;
		}
	    }

	}
	return true;
    }

    private int getNextSequenceNo(int currNo) {
	if (currNo == 0)
	    return 1;
	else
	    return 0;
    }

    private void resetSendTimer() {
	timeoutTimer = sendTimer;
    }

    public boolean reset() { // passed when the simulation is reset
	elapsedTime = 0;
	absoluteTime = 0;
	snd_frameno = -1;
	snd_ackno = 0;
	rcv_frameno = -1;
	rcv_ackno = 0;
	snd_receivedPacket = false;
	rcv_receivedPacket = false;
	resetSendTimer();
	return true;
    }

    public Port[] getPorts() {// Gives the list of already initialized ports
	return ports;
    }

    public boolean isNewPacket() {// Tells whether new packet is released by the module. If released, it sends true and resets the flag,else it
	// returns false.
	if (newPacket == true) {
	    newPacket = false;
	    return true;
	} else
	    return false;

    }

    public int getNoOfPorts() {
	return noOfPorts;
    }

    public String getIconPath() {
	return "module/SnWwA/terminal.jpg";
    }

}