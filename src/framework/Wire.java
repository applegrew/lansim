/*
 * Wire.java
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

import java.awt.Color;
import java.util.ArrayList;

import module.DroppedPacketDataUI;

/**
 * Wire.java Represents a link in the network can be connected to any number of Ports. Created on December 7, 2006,
 * 
 * @author Nirupam
 * @author Rohit
 */
public class Wire {
    public static final int     GENERAL	 = 0;    // Wire tye = General.
    private int		 type	    = 0;    // To be used later.
    private int		 noOfPorts       = 0;
    private boolean	     pktAvailable    = false;
    private ArrayList<Packet>   packets	 = null;
    private ArrayList<Packet>   dropped_packets = null;
    private ArrayList<Double>   packets_in_time = null;
    private static final double LATENCY	 = 15;   // later make changeable by user
    // private double dpu = 1; //Data packet transfer rate per unit time. Take this as a convinient factor times actual bps of a connection.
    private Port		ports[]	 = null;
    private volatile boolean    dropPackets     = false;

    public Wire() {
	packets = new ArrayList<Packet>();
	dropped_packets = new ArrayList<Packet>();
	packets_in_time = new ArrayList<Double>();
	ports = new Port[2];
    }

    public Wire(Port port) {
	packets = new ArrayList<Packet>();
	dropped_packets = new ArrayList<Packet>();
	packets_in_time = new ArrayList<Double>();
	ports = new Port[2];
	addPort(port);

    }

    public Wire(Port port1, Port port2) {
	packets = new ArrayList<Packet>();
	dropped_packets = new ArrayList<Packet>();
	packets_in_time = new ArrayList<Double>();
	ports = new Port[2];
	addPort(port1);
	addPort(port2);

    }

    /**
     * If set to true then all packets from that point of time will be dropped. These dropped packets will then be put into dropped_packet ArrayList.
     * @param state
     */
    public void setDropPackets(boolean state) {
	dropPackets = state;
    }

    public boolean isDroppingPackets() {
	return dropPackets;
    }

    public void reset() {
	packets.clear();
	packets_in_time.clear();
	pktAvailable = false;
    }

    // public void setDataRate(double dpu){
    // this.dpu=dpu;
    // }
    //    
    // public double getDataRate(){
    // return dpu;
    // }

    public boolean hasData() {
	return pktAvailable;
    }

    public Port getPort(int index) {
	return ports[index];
    }

    public Port[] getPorts() {
	return ports;

    }

    public int getType() {
	return type;
    }

    public void addPort(Port port) {
	System.out.println("port of added " + noOfPorts);
	ports[noOfPorts] = port;
	noOfPorts++;
    }

    private void putPacketOnPorts(double currtime) {
	Packet tempPacket;
	for (int p = 0; p < packets.size(); p++) {
	    tempPacket = packets.get(p);
	    if ((currtime - packets_in_time.get(p)) >= LATENCY) {
		for (int i = 0; i < noOfPorts; i++) {
		    if (ports[i].isActive() && (ports[i] != tempPacket.getFromPort())) {
			ports[i].putPacket(tempPacket, this);
			// System.out.println("delivered "+tempPacket.getData()+" "+ports[i].getOwner().getName());
			// ports.get(fromPortNo).rstPacketSourceFlag();
		    }
		}
		int index = packets.indexOf(tempPacket);
		packets_in_time.remove(index);
		packets.remove(index);
	    }
	}
	if (packets.size() == 0)
	    this.pktAvailable = false;
    }

    private void getPacketFromPorts(double currtime) {
	for (int i = 0; i < dropped_packets.size();)
	    if (!(dropped_packets.get(i).dataUI instanceof DroppedPacketDataUI)) {
		try {
		    throw new Exception(
			    "dropped_packets has dataUI object NOT of the class DroppedPacketDataUI. Since, ModuleUI instanciates dataUI, hence it or its sub-class must be responible for this bug. Correct this bug now!");
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    } else if (!((DroppedPacketDataUI) dropped_packets.get(i).dataUI).isAlive())
		dropped_packets.remove(i);
	    else
		i++;

	Packet tempPacket = null;
	for (int i = 0; i < noOfPorts; i++) {
	    if (ports[i].isActive() && ports[i].isTransmiting()) {
		if (ports[i].hasData())
		    if (dropPackets) {
			tempPacket = ports[i].getPacket(this);
			tempPacket.setFromPort(ports[i]);
			tempPacket.setDroppedPacket(true);
			dropped_packets.add(tempPacket);
			//DataUI is set in ModuleUI.

		    } else {
			tempPacket = ports[i].getPacket(this);
			tempPacket.setFromPort(ports[i]);
			packets.add(tempPacket);
			packets_in_time.add(currtime);
			if (this.pktAvailable)
			    for (int j = 0; j < packets.size(); j++) {
				if (packets.get(j).getFromPort() != tempPacket.getFromPort()) {
				    for (int k = 0; k < packets.size(); k++)
					packets.get(k).setWillGetCorrupt();
				    break;
				}
			    }
			else {
			    this.pktAvailable = true;
			}

		    }
	    }
	}
    }

    public boolean step(double currtime) {
	getPacketFromPorts(currtime);
	if (this.pktAvailable == true)
	    putPacketOnPorts(currtime);
	return true;
    }

    /* TO DO LATER, probably never. */
    public void merge(Wire wire) {
    // TODO Auto-generated method stub

    }

    public void removePort(Port p) {
	if (p == ports[1])
	    ports[1] = null;
	else if (p == ports[0]) {
	    ports[0] = ports[1];
	    ports[1] = null;
	}
	noOfPorts--;
	// TODO Auto-generated method stub
    }

    public double getLatency() {
	return LATENCY;
    }

    public int getNoOfPorts() {
	return noOfPorts;
    }

    public ArrayList<Packet> getPackets() {
	return new ArrayList<Packet>(packets);
    }

    public boolean hasDroppedPackets() {
	return dropped_packets.size() != 0;
    }

    public ArrayList<Packet> getDroppedPackets() {
	return new ArrayList<Packet>(dropped_packets);
    }

}