/*
 * PcMod.java
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
package module.Pc;

import java.lang.String;
import framework.Packet;
import java.util.ArrayList;
import framework.Port;
import module.ModuleUI;

/**
 * 
 * @author Rohit
 * 
 */
public class PcMod extends module.ModuleAbstractClass {
    static final int     MAXPORTS	     = 5;
    int		  noOfPorts	    = 0;
    double	       elapsedTime	  = 0;
    double	       absoluteTime	 = 0;
    private Port	 ports[];
    private int	  serialNo	     = 0;
    private String       pingData	     = null;
    private double       pingTime;
    private final String name		 = "Pc";
    private final int    TIMEOUT	      = 300;
    private int	  toid		 = 0;
    private boolean      newPacket	    = false;
    final double	 PHYSICALATENCY       = 1;
    final int	    SIZE_PHYSICAL_BUFFER = 10;
    ArrayList<Packet>    iPhysicalBuffer;
    ArrayList<Packet>    oPhysicalBuffer;
    private ModuleUI     modUI		= null;

    public PcMod() {
	buffer.EditMode.module = this;
	iPhysicalBuffer = new ArrayList<Packet>();
	oPhysicalBuffer = new ArrayList<Packet>();
	ports = new Port[MAXPORTS];
	modUI = new PCUI(this);
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
	    modUI.getModWin().msg.append("\nADDED port " + noOfPorts);
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
	absoluteTime++;
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
	if (iPhysicalBuffer.size() > 0) {
	    Packet packet = iPhysicalBuffer.get(0);
	    iPhysicalBuffer.remove(0);
	    if (packet.getData().startsWith("ping")) {
		if (pingData != null && packet.getData().compareTo("ping" + pingData + "ping" + pingData) == 0) {
		    modUI.getModWin().amsg.append("\n" + "Reply from " + packet.getFromId() + " time= " + (absoluteTime - pingTime));
		    pingData = null;
		} else if (packet.getData().lastIndexOf("ping") < 3) {
		    modUI.getModWin().amsg.append("\n Ping from " + packet.getFromId());
		    oPhysicalBuffer.add(new Packet(packet.getFromId(), serialNo, 10, packet.getData() + packet.getData(), 0, true));
		}
	    }
	}
	if (pingData == null && absoluteTime % (serialNo * 50) == 0) {
	    pingTime = absoluteTime;
	    toid = toid + 1;
	    toid = toid % 15 + 1;
	    pingData = new String(name + serialNo + absoluteTime);
	    oPhysicalBuffer.add(new Packet(toid, serialNo, 10, "ping" + pingData, 0));
	    modUI.getModWin().amsg.append("\nPinging " + toid + " with 10 bytes of data");
	}
	if (pingData != null && (absoluteTime - pingTime > TIMEOUT)) {
	    modUI.getModWin().amsg.append("\nRequestTimeout");
	    pingData = null;
	}
	return true;
    }

    public boolean reset() { // passed when the simulation is reset
	elapsedTime = 0;
	absoluteTime = 0;
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

}