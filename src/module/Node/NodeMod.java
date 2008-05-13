/*
 * NodeMod.java
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
package module.Node;

import java.lang.String;
import framework.Packet;
import framework.Port;
import module.ModuleUI;

/**
 * 
 * @author Nirupam
 * 
 */
public class NodeMod extends module.ModuleAbstractClass {
    static final int     MAXPORTS     = 2;
    volatile boolean     canEmitData  = false;
    int		  noOfPorts    = 0;
    double	       elapsedTime  = 0;
    double	       absoluteTime = 0;
    private boolean      newPacket    = false;
    private Port	 ports[];
    private int	  serialNo     = 0;
    private final String name	 = "Node";
    final double	 LATENCY      = 0;
    private ModuleUI     modUI	= null;

    public NodeMod() {
	ports = new Port[MAXPORTS];
	buffer.EditMode.module = this;
	modUI = new NodeUI(this);
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
	if (canEmitData) {
	    for (int i = 0; i < noOfPorts; i++) {
		// System.out.println(name+serialNo+" "+absoluteTime+" "+i+" "+ports[i].isActive() +" "+ ports[i].hasData());
		if (ports[i].isActive() && ports[i].hasData() && !ports[0].isTransmiting()) {
		    packet = ports[i].getPacket(this);
		    if (packet.getToId() == 1) {
			System.out.println("Packet received at node " + serialNo);
			if (!packet.isCorrupt() && packet.getData() != null) {
			    System.out.println("from " + packet.getFromId() + " with data " + packet.getData() + "to " + packet.getToId());
			} else {
			    System.out.println("from " + packet.getFromId() + " but packet is CORRUPTED.");
			}
			// else {
			// for(int j=0;j<MAXPORTS;j++){
			// if(i==j) continue;
			// ports[j].putPacket(new Packet(packet), this);
			// }
			// }
		    }
		}
	    }
	}
	if (absoluteTime % 25 == 0 && canEmitData && noOfPorts != 0 && !ports[0].isTransmiting()) {
	    // System.out.println("pcppcpc ,,,,,,,,,, "+ports.length);
	    for (int i = 0; i < noOfPorts; i++) {
		ports[i].putPacket(new Packet((int) (Math.random() * 10), serialNo, 10, name + serialNo + absoluteTime), this);
		ports[i].setActive(true);
		System.out.println("Put packet on port " + i + "   " + serialNo);
		newPacket = true;
	    }
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