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

import java.lang.String;
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
    static final int     MAXPORTS       = 1;
    volatile boolean     canEmitData    = false;
    int		  targetSlNo     = 2;
    int		  ackno	  = 0;	// Received ack no
    int		  frameno	= -1;       // Sent frame no
    int		  sendTimer      = 90;
    int		  noOfPorts      = 0;
    private int	  timeoutTimer;	      // timeoutTimer counts down. It then resets to sendTimer value.
    private int	  serialNo       = 0;
    double	       elapsedTime    = 0;
    double	       absoluteTime   = 0;
    private boolean      newPacket      = false;
    private boolean      receivedPacket = false;
    private Port	 ports[];
    private final String name	   = "SW Node";
    final double	 LATENCY	= 0;
    private ModuleUI     modUI	  = null;

    public SnWwAMod() {
	ports = new Port[MAXPORTS];
	buffer.EditMode.module = this;
	modUI = new SnWwAUI(this);
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
	Packet packet;
	absoluteTime++;
	if (canEmitData) { // TRANSMITTER MODE
	    if (ports[0].isActive() && ports[0].hasData() && !ports[0].isTransmiting()) {
		packet = ports[0].getPacket(this);
		if (packet.getToId() == serialNo) {
		    if (!packet.isCorrupt() && packet.getData() != null && packet.isReply) {
			ackno = Integer.parseInt(packet.getData());
			modUI.getModWin().msg.append("\nReceived ACK " + ackno + " from " + packet.getFromId());
			receivedPacket = true;
		    }
		} else {
		    modUI.getModWin().msg.append("\nReceived a corrupt/invalid packet. Ignoring.");
		}
	    }
	    if (frameno == -1 || receivedPacket) {
		if (noOfPorts != 0) {
		    frameno = ackno;
		    ports[0].putPacket(new Packet(targetSlNo, serialNo, 10, String.valueOf(frameno)), this);
		    ports[0].setActive(true);
		    newPacket = true;
		    resetSendTimer();
		    modUI.getModWin().msg.append("\nSent data packet " + frameno + " to " + targetSlNo);
		    receivedPacket = false;
		}
	    } else if (!receivedPacket) { // Waiting for ACK
		timeoutTimer--;
		if (timeoutTimer <= 0) {
		    if (noOfPorts != 0) {
			ports[0].putPacket(new Packet(targetSlNo, serialNo, 10, String.valueOf(frameno)), this);
			ports[0].setActive(true);
			newPacket = true;
			resetSendTimer();
			modUI.getModWin().msg.append("\nTimer timed-out. Re-sent data packet " + frameno + " to " + targetSlNo);
		    }
		}
	    }

	} else { // RECEIVER MODE
	    if (ackno == -1)
		ackno = 0;

	    if (ports[0].isActive() && ports[0].hasData() && !ports[0].isTransmiting()) {
		packet = ports[0].getPacket(this);
		if (packet.getToId() == serialNo) {
		    if (!packet.isCorrupt() && packet.getData() != null && !packet.isReply) {
			int tframeno = Integer.parseInt(packet.getData());
			if (tframeno == frameno)
			    modUI.getModWin().msg.append("\nReceived duplicate data packet " + tframeno + " from " + packet.getFromId()
				    + ". Discarding it.");
			else {
			    frameno = tframeno;
			    modUI.getModWin().msg.append("\nReceived data packet " + frameno + " from " + packet.getFromId());
			}
			targetSlNo = (int) packet.getFromId();
			receivedPacket = true;
		    }
		} else {
		    modUI.getModWin().msg.append("\nReceived a corrupt/invalid packet. Ignoring.");
		}
	    }
	    if (receivedPacket) {
		if (noOfPorts != 0) {
		    ackno = getNextSequenceNo(frameno);
		    Packet ack = new Packet(targetSlNo, serialNo, 10, String.valueOf(ackno));
		    ack.isReply = true;
		    ports[0].putPacket(ack, this);
		    ports[0].setActive(true);
		    newPacket = true;
		    modUI.getModWin().msg.append("\nSent ack packet " + ackno + " to " + targetSlNo);
		    receivedPacket = false;
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
	frameno = -1;
	ackno = 0;
	receivedPacket = false;
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