/*
 * Packet.java
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

import module.DataUI;

/**
 * Packet.java A data packet in the network, can be in a port or a wire. Created on December 7, 2006,
 * 
 * @author Nirupam
 * @author Rohit
 */
public class Packet {
    /** Data Members */
    private long    toId;
    private long    fromId;
    private boolean corrupt;
    private boolean willGetCorrupt = false;
    private long    size;
    private String  data;
    private Port    fromPort;
    private boolean isDropped      = false;
    // private double pickUpTime;
    public boolean  isReply	= false;
    public DataUI   dataUI;
    public int      toPort	 = 0;

    /** constructors */
    public Packet() {
	toId = 0;
	fromId = 0;
	size = 0;
	corrupt = false;
	data = null;
	dataUI = null;// new DataUI();
    }

    public Packet(Packet p) {
	toId = p.toId;
	fromId = p.fromId;
	size = p.size;
	corrupt = p.corrupt;
	data = new String(p.data);
	isReply = p.isReply;
    }

    /* member functions */
    public Packet(long toId, long fromId, long size, String data) {
	this.toId = toId;
	this.fromId = fromId;
	this.size = size;
	this.data = data;
	corrupt = false;
	dataUI = null;// new DataUI();
    }

    public Packet(long toId, long fromId, long size, String data, int portIndex) {
	this.toId = toId;
	this.fromId = fromId;
	this.size = size;
	this.data = data;
	corrupt = false;
	dataUI = null;// new DataUI();
	toPort = portIndex;
    }

    public Packet(long toId, long fromId, long size, String data, int portIndex, boolean reply) {
	this.toId = toId;
	this.fromId = fromId;
	this.size = size;
	this.data = data;
	corrupt = false;
	dataUI = null;// new DataUI();
	toPort = portIndex;
	isReply = reply;
    }

    public void gotCorrupted() {
	corrupt = true;
    }

    public boolean isCorrupt() {
	return corrupt;
    }

    public long getToId() {
	return toId;
    }

    public long getFromId() {
	return fromId;
    }

    public String getData() {
	return data;
    }

    public Port getFromPort() {
	return fromPort;
    }

    public boolean isDroppedPacket() {
	return isDropped;
    }

    public void setDroppedPacket(boolean f) {
	isDropped = f;
    }

    /*
     * public double getPickUpTime (){ return pickUpTime; }
     */
    public void setFromPort(Port port) {
	fromPort = port;
    }

    /*
     * public void setPickUpTime (double time){ pickUpTime=time; }
     */
    public void setWillGetCorrupt() {
	willGetCorrupt = true;
    }

    public boolean getWillGetCorrupt() {
	return willGetCorrupt;
    }
}