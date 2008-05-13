/*
 * Port.java
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
package framework;

import module.Module;
import java.util.ArrayList;

/**
 * Port.java Class port- the one which represents a port on any module. Created on December 7, 2006
 * 
 * @author Rohit
 */
public class Port {
    /** Data members */
    private boolean	 pktAvailable  = false;
    private boolean	 active	= true; // enables or disables the port.
    private String	  name	  = null;
    private double	  speed	 = 1;    // Consider later
    private Packet	  packet	= null;  ;
    private Packet	  emittedPacket = null;
    private Module	  owner	 = null;
    private boolean	 transmiting   = false;
    private ArrayList<Long> compId;

    public Port(String Name, Module owner) {
	name = Name;
	System.out.println("added new port to " + owner.getName());
	this.owner = owner;
	compId = new ArrayList<Long>();
	packet = new Packet();
    }

    public boolean hasData() {
	return pktAvailable;
    }

    public void setActive(boolean state) {
	active = state;
    }

    public boolean isActive() {
	return active;
    }

    public void putPacket(Packet packet, Wire caller) {
	if (active) {
	    this.packet = packet;
	    // System.out.println("yapee");
	    if (pktAvailable == true)
		packet.gotCorrupted();
	    else {
		pktAvailable = true;
		//System.out.println("node got data..........."+owner.getName()+" "+packet.getData());
		// System.out.println("port of"+this.owner.getName());
	    }
	}
	transmiting = false;
    }

    public void putPacket(Packet packet, Module caller) {
	if (caller.equals(owner)) {

	    this.packet = packet;
	    this.emittedPacket = packet;
	    if (pktAvailable == true)
		;

	    else
		pktAvailable = true;
	    transmiting = true;
	    // System.out.println("port of..............."+this.owner.getName()+" "+packet.getData());

	}

    }

    /**
     * Returns the packet without destroying it. This is required by WireUI to draw data packets in GUI.
     * 
     * @return
     */
    public Packet getPacketCopy() {
	return packet;
    }

    public Packet getPacket(Wire caller) {
	pktAvailable = false;
	transmiting = false;
	// System.out.println("port of.trans false.............."+this.owner.getName());
	return packet;
    }

    public Packet getPacket(Module caller) {
	if (caller.equals(owner)) {
	    pktAvailable = false;
	    return packet;
	} else
	    return null;
    }

    public Packet getPacketRepeated(Module caller, boolean state) {
	if (caller.equals(owner)) {
	    pktAvailable = state;
	    return packet;
	} else
	    return null;
    }

    public Module getOwner() {
	return owner;
    }

    public void addCompId(long id) {
	compId.add(id);
    }

    public void removeCompId(long id) {
	compId.remove(id);
    }

    public void removeAllCompId() {
	compId.clear();
    }

    public boolean isIdPresent(long id) {
	return compId.contains(id);
    }

    public String getAllCompIds() {
	String ids = new String();
	for (int i = 0; i < compId.size(); i++) {
	    ids += " " + compId.get(i);
	}
	return ids;
    }

    public boolean isTransmiting() {
	return transmiting;
    }

    public Packet getEmittedPacket() {
	if (emittedPacket != null) {
	    Packet tempPacket = emittedPacket;
	    emittedPacket = null;
	    return tempPacket;
	} else
	    return null;
    }

}