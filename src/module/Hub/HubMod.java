/*
 * HubMod.java
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
package module.Hub;

import framework.Port;
import module.ModuleUI;

import java.lang.String;
import framework.Packet;

/**
 * 
 * @author Nirupam
 * 
 */
public class HubMod extends module.ModuleAbstractClass {
    static final int     MAXPORTS     = 10;
    int		  noOfPorts    = 0;
    double	       absoluteTime = 0;
    private Port	 ports[];
    private int	  serialNo     = 0;
    private final String name	 = "Hub";
    private boolean      newPacket    = false;
    private ModuleUI     modUI	= null;

    public HubMod() {
	buffer.EditMode.module = this;
	ports = new Port[MAXPORTS];
	modUI = new HubUI(this);
    }

    public void setSno(int n) {
	serialNo = n;
    }

    /**
         * Adds a new port (connection)
         * 
         * @return The error code if any. e.g. ERROR_ALREADY_MAX_ports is returned if no more ports of type wireType can be handeled by this module.
         */
    public int addPort(Port port, int wireType) {
	if (noOfPorts < MAXPORTS) {
	    ports[noOfPorts++] = port;
	    port.setActive(true);
	    modUI.getModWin().msg.append("\nADDED port " + noOfPorts);
	    return 1;
	} else
	    return ERROR_ALREADY_MAX_PORTS;
    }

    /**
         * If the implementing class doesn't want to extend or create an object of ModuleUI them return null.
         * 
         * @return
         */
    public ModuleUI getModuleUI() {
	return modUI;
    }

    public void setModuleUI(ModuleUI m) {
	modUI = m;
    }

    /**
         * Returns the name of the implementing module. This name is displayed by the UI.
         * 
         * @return
         */
    public String getName() {
	return name;
    }

    /**
         * USES R-R SCHEDULING TO SERVICE THE PORTS
         */
    public boolean step(double TimeStep) {
	absoluteTime++;
	int noOfDataPorts = 0;
	for (int x = 0; x < noOfPorts; x++) {
	    if (ports[x].hasData()) {
		noOfDataPorts++;
	    }
	}
	if (noOfDataPorts == 1) {
	    for (int i = 0; i < noOfPorts; i++) {
		if (ports[i].hasData()) {
		    Packet packet = ports[i].getPacket(this);
		    modUI.getModWin().msg.append("\nFrom:" + i + "   To:");
		    for (int j = 0; j < noOfPorts; j++) {
			if (j != i) {
			    modUI.getModWin().msg.append(" " + j);
			    Packet tempPacket = new Packet(packet);
			    tempPacket.dataUI = null;
			    ports[j].putPacket(tempPacket, this);
			    ports[j].setActive(true);
			    newPacket = true;
			}

		    }
		    return true;
		}
	    }

	} else if (noOfDataPorts > 1) {
	    modUI.getModWin().msg.append("\nMultiple Packets, ports:  ");
	    for (int i = 0; i < noOfPorts; i++) {
		if (ports[i].hasData()) {
		    modUI.getModWin().msg.append(" " + i);
		    Packet packet = ports[i].getPacket(this);
		    for (int j = 0; j < noOfPorts; j++) {
			Packet tempPacket = new Packet(packet);
			tempPacket.dataUI = null;
			tempPacket.gotCorrupted();
			ports[j].putPacket(tempPacket, this);
			ports[j].setActive(true);
			newPacket = true;

		    }
		    return false;
		}
	    }
	}
	// boolean Success or failure
	return true;
    }

    public boolean reset() { // passed when the simulation is reset
	for (int i = 0; i < noOfPorts; i++)
	    if (ports[i].hasData())
		ports[i].getPacket(this);
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

    public int getSno() {

	return serialNo;
    }

}