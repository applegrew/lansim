/*
 * WireUI.java
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
package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import util.UtilGraphics;

import module.DataUI;
import module.ModuleUI;
import module.moduleWindow;

import framework.Packet;
import framework.Port;
import framework.Wire;

/**
 * @author Nirupam
 * 
 */
public class WireUI extends ComponentUI {
    /**
     * Keeps a list of segments that make up the wire.
     */
    private ArrayList<SegmentCoords> coords	     = null;
    private Point2D.Double	   currentCoord       = null;
    private Port		     currentPort	= null;
    private Handle		   currentHandle      = null;
    private int		      type;
    private int		      length	     = 0;
    private final int		wireWidth	  = 4;
    // TODO In future the colour values would be selected possibly based on type
    // and this can be changed
    // by the user also.
    private Color		    normalColour       = Color.MAGENTA;
    private Color		    dropPacketColour   = Color.RED;
    private double		   dataFlowPacketsDis = 10;	   // in pixels
    WireModWinUI		     wireModWin	 = null;
    Wire			     wire	       = null;
    private ManagerUI		managerUI	  = null;

    /**
     * Creates new WireUI. Wire can have even one port.
     * 
     * @param w
     * @param manUI
     */
    WireUI(Wire w, ManagerUI manUI) {
	wire = w;
	managerUI = manUI;
	type = wire.getType();
	currentCoord = getModuleCoord(w.getPort(0));
	currentPort = w.getPort(0);
	currentHandle = null;
	coords = new ArrayList<SegmentCoords>();
	length = 0;
    }

    public void reset() {
	if (wireModWin != null)
	    wireModWin.reset();
    }

    private void updateDataFlowPacketsDis() {
	dataFlowPacketsDis = Math.round((length / (wire.getLatency() * managerUI.WireUI2WireSteppingRatio)));
    }

    private Point2D.Double getModuleCoord(Port p) {
	ModuleUI m = managerUI.getModuleUI(p.getOwner());
	Point2D.Double pt = new Point2D.Double();
	pt.setLocation(m.getCoord());
	return pt;
    }

    private SegmentCoords findSegment(Point2D.Double p) {
	return findSegment(p, null);
    }

    private SegmentCoords findSegment(Point2D.Double p, SegmentCoords ignoreThis) {
	for (int i = 0; i < coords.size(); i++) {
	    if (coords.get(i).liesOn(p) && (ignoreThis == null || !coords.get(i).equals(ignoreThis)))
		return coords.get(i);
	}
	return null;
    }

    private Point2D.Double snap2segEnd(Point2D.Double pt, SegmentCoords seg) {
	if (seg.p1.getCoord().x - pt.x == seg.p2.getCoord().x - pt.x) {
	    if (Math.abs(seg.p1.getCoord().y - pt.y) <= Math.abs(seg.p2.getCoord().y - pt.y))
		return seg.p1.getCoord();
	    else
		return seg.p2.getCoord();
	} else {
	    if (Math.abs(seg.p1.getCoord().x - pt.x) < Math.abs(seg.p2.getCoord().x - pt.x))
		return seg.p1.getCoord();
	    else
		return seg.p2.getCoord();
	}
    }

    /**
     * Steps the simulation of wire.
     * 
     * @param mode
     *                The mode in which this function has been called. It can be any of the 4 modes.
     * 
     */
    public void stepSimulation(Mode mode) {

	if (mode == Mode.SIMULATION_MODE) {
	    if (wire.hasData() || wire.hasDroppedPackets()) {
		ArrayList<Packet> packets = wire.getPackets();
		if (wire.hasDroppedPackets())
		    packets.addAll(wire.getDroppedPackets());
		for (int i = 0; i < packets.size(); i++) {
		    Packet packet = packets.get(i);

		    DataUI dataUI = packet.dataUI;
		    if (dataUI.pos.x == -1) {// i.e. this dataUI has just been created by ModuleUI and needs to be pos.ed properly.
			dataUI.pos = getModuleCoord(packet.getFromPort());
			SegmentCoords segment = findSegment(dataUI.pos);
			if (segment == null) {
			    System.out.println("Internal Error!!! No line segment for the current wire found with the location ("
				    + dataUI.pos.toString() + ") on it.");
			    return;
			}
			segment.setDirection(dataUI, dataUI.pos.equals(segment.p1.getCoord()) ? segment.p2.getCoord() : segment.p1.getCoord());

		    } else {
			SegmentCoords segment = findSegment(dataUI.pos);
			if (segment == null) {
			    System.out.println("Internal Error!!! No line segment for the current wire found with the location ("
				    + dataUI.pos.toString() + ") on it.");
			    return;
			}
			dataUI.pos = segment.getNxtXYfor(dataUI);
			if (segment.isEndReached()) {
			    segment = findSegment(dataUI.pos, segment);
			    if (segment != null) { // segment==null when data packet has reached at the other end of wire (i.e. the target
				// module).
				dataUI.pos = snap2segEnd(dataUI.pos, segment);
				segment
					.setDirection(dataUI, dataUI.pos.equals(segment.p1.getCoord()) ? segment.p2.getCoord() : segment.p1
						.getCoord());
				dataUI.pos = segment.getNxtXYfor(dataUI);
			    }
			}
		    }
		}
	    }
	}
    }

    /**
     * Renders the wire.
     * 
     * @param g
     */
    public void render(Graphics g) {
	Color tc = g.getColor();
	g.setColor(normalColour);
	for (int i = 0; i < coords.size(); i++) {
	    coords.get(i).render(g);
	}
	g.setColor(tc);
	if (managerUI.getMode() == Mode.SIMULATION_MODE || managerUI.getMode() == Mode.PAUSED_MODE) {
	    ArrayList<Packet> packets = wire.getPackets();
	    if (wire.hasDroppedPackets())
		packets.addAll(wire.getDroppedPackets());
	    for (int i = 0; i < packets.size(); i++) {
		DataUI tempDataUi = packets.get(i).dataUI;
		if (tempDataUi != null) {
		    tempDataUi.render(g);
		} else
		    System.out.println("tmpDataUI is null! Couldn't render data packet.");
	    }
	}
    }

    /**
     * Draws a st. line from its initial point to pointerPos.
     * 
     * @param g
     * @param pointerPos
     */
    public void render(Graphics g, Point2D.Double pointerPos) {
	if (managerUI.getMode() != Mode.WIRE_CREATION_MODE) {
	    System.out.println("Error: Invalid mode for this render() method of WireUI method. WIRE_CREATION_MODE needed.");
	    return;
	}

	if (currentCoord == null) {
	    System.out.println("currentCoord is null");
	    currentCoord = getModuleCoord(wire.getPort(0));
	    if (currentCoord == null)
		System.out.println("currentCoord is null");
	}
	//g.drawLine((int)currentCoord.x, (int)currentCoord.y, (int)pointerPos.x, (int)pointerPos.y);
	UtilGraphics.drawThickLine(g, (int) currentCoord.x, (int) currentCoord.y, (int) pointerPos.x, (int) pointerPos.y, wireWidth, normalColour);
    }

    /**
     * This is called to finalise the creation of line/wire-segment. This creates a segment ending with a node.
     * 
     */
    public Handle[] commit(Point2D.Double clickPos) {
	if (managerUI.getMode() != Mode.WIRE_CREATION_MODE) {
	    System.out.println("Error: Invalid mode for this commit(Point2D.Double) method of WireUI method. WIRE_CREATION_MODE needed.");
	    return null;
	}

	Handle lastHandle;
	if (currentHandle == null) {
	    boolean is_currAPort = true;
	    if (currentPort == null)
		is_currAPort = false;
	    lastHandle = new Handle(currentCoord, is_currAPort);
	} else {
	    lastHandle = currentHandle;
	}
	currentHandle = new Handle(clickPos, false);
	SegmentCoords s = new SegmentCoords(lastHandle, currentHandle);
	s.setEndPorts(currentPort, null);
	coords.add(s);
	currentCoord = clickPos;
	currentPort = null;
	length += s.segLen();
	Handle[] handlePair = new Handle[2];
	handlePair[0] = s.p1;
	handlePair[1] = s.p2;
	return handlePair;
    }

    /**
     * This is called to finalise the creation of wire.
     * 
     * @param p
     */
    public Handle[] commit(Port p) {
	if (managerUI.getMode() != Mode.WIRE_CREATION_MODE) {
	    System.out.println("Error: Invalid mode for this commit(Port) method of WireUI method. WIRE_CREATION_MODE needed.");
	    return null;
	}

	Handle lastHandle;
	if (currentHandle == null) {
	    boolean is_currAPort = true;
	    if (currentPort == null)
		is_currAPort = false;
	    lastHandle = new Handle(currentCoord, is_currAPort);
	} else {
	    lastHandle = currentHandle;
	}
	SegmentCoords s = new SegmentCoords(lastHandle, new Handle(getModuleCoord(p), true));
	s.setEndPorts(currentPort, p);
	coords.add(s);
	currentCoord = null;
	currentPort = null;
	currentHandle = null;
	length += s.segLen();
	System.out.println("Length of WIRE::::::: " + length);
	updateDataFlowPacketsDis();
	// Now ManagerUI should reset the mode to EDIT_MODE.
	Handle[] handlePair = new Handle[2];
	handlePair[0] = s.p1;
	handlePair[1] = s.p2;
	return handlePair;
    }

    /**
     * This is called when another wire is clicked during wire creation. This means now both these 2 wires should be merged. The wire in creation
     * mode is passed the wire with which it is to be merged. So, actually the already created wire is merged into it. The coords arraylist
     * contents of the target is appended below this present one's list.
     * 
     * @param clickPos
     * @param w
     */
    /*
     * public void commit(Point2D.Double clickPos, WireUI w){ if(managerUI.getMode() != Mode.WIRE_CREATION_MODE){ System.out.println("Error:
     * Invalid mode for this commit(Point2D.Double, WireUI) method of WireUI method. WIRE_CREATION_MODE needed."); return; } SegmentCoords s = new
     * SegmentCoords(new Handle(currentCoord), new Handle(clickPos)); s.setEndPorts(currentPort, null); coords.add(s); currentCoord = null;
     * for(int i=0;i<w.coords.size();i++) coords.add(w.coords.get(i)); wire.merge(w.wire); //length += s.segLen(); //dataFlowPacketsDis=
     * length/(lenOfWireGuareentedToBeAbleShowFullAimation/dataFlowPacketsDis); //Now ManagerUI should reset the mode to EDIT_MODE. }
     */

    /**
     * Locates the entry in coords with h. It returns its index in coords list. If no occurances are found then -1 is returned.
     * 
     * @param h -
     *                The Hanlde to search for.
     * @return
     */
    private int locate(Handle h) {
	for (int i = 0; i < coords.size(); i++) {
	    if (coords.get(i).p1.equals(h) || coords.get(i).p2.equals(h)) {
		return i;
	    }
	}
	return -1;
    }

    /**
     * Returns the Handle other than the one passed to it at the row given by index.
     * 
     * @param index
     * @param h
     * @return
     */
    private Handle getTheOtherHandle(int index, Handle h) {
	return coords.get(index).p1.equals(h) ? coords.get(index).p2 : coords.get(index).p1;
    }

    public boolean isClicked(Point2D.Double pt) {
	for (int i = 0; i < coords.size(); i++) {
	    if (coords.get(i).liesOn(pt)) // i.e. This wire has been clicked if the clicked pt lies on any of the line segments making it.
		return true;
	}
	return false;
    }

    public moduleWindow getModWin() {
	if (wireModWin == null)
	    return null;
	return wireModWin.modWin;
    }

    public void setModWin(moduleWindow m) {
	if (wireModWin == null)
	    wireModWin = new WireModWinUI(this);
	wireModWin.modWin = m;
    }

    public void createWireModWin() {
	wireModWin = new WireModWinUI(this);
    }

    public WireModWinUI getWireModWin() {
	return wireModWin;
    }

    public boolean isClicked(Point pt) {
	for (int i = 0; i < coords.size(); i++) {
	    Point2D.Double ptD = new Point2D.Double(pt.x, pt.y);
	    if ((coords.get(i)).liesOn(ptD)) // i.e. This wire has been clicked if the clicked pt lies on any of the line segments making it.
		return true;
	}
	return false;
    }

    private Handle[] getAdjacentHandles(Handle h) {
	int i = locate(h);
	if (i == -1) {
	    System.out.println("Error: The given handle doesn't exist");
	    return null;
	}
	return getAdjacentHandles(i, h);
    }

    private Handle[] getAdjacentHandles(int i, Handle h) {
	Handle[] handles = new Handle[2];
	if (coords.get(i).p1 == h) {// It is the left handle.
	    if (i != 0)
		handles[0] = coords.get(i - 1).p1;
	    else
		handles[0] = null;
	    handles[1] = coords.get(i).p2;
	} else if (coords.get(i).p2 == h) {// It is the right handle.
	    if (i != (coords.size() - 1))
		handles[1] = coords.get(i + 1).p2;
	    else
		handles[1] = null;
	    handles[0] = coords.get(i).p1;
	}
	return handles;
    }

    public boolean areAdjacent(Handle h1, Handle h2) {
	Handle[] handles = getAdjacentHandles(h1);
	if (handles[0] == h2 || handles[1] == h2)
	    return true;
	else
	    return false;
    }

    /**
     * Removes only 'naked' handles, i.e. Handles that are not associated with any ports.
     * 
     * @param handle
     */
    public void removeNakedHandle(Handle h) {
	if (h.isPort()) {
	    System.out.println("Error! Handle to remove is not naked. Ignoring request.");
	    return;
	}
	int i = locate(h);
	if (i == -1) {
	    System.out.println("Error: The given naked handle doesn't exist");
	    return;
	}
	/*
	 * Since a naked handle will always be in some segments' p1 and p2 both, and, since, locate() scans from the top hence it will return the
	 * segment where this handle is on p2. So, we need not use getAdjacentHandles().
	 */
	SegmentCoords s1 = coords.get(i);
	SegmentCoords s2 = coords.get(i + 1);
	s1.p2 = s2.p2;
	coords.remove(s2);
	updatePrivateData();
    }

    public void updatePrivateData() {
	length = 0;
	for (int i = 0; i < coords.size(); i++) {
	    coords.get(i).updatePrivate();
	    length += coords.get(i).segLen();
	}
	updateDataFlowPacketsDis();
    }

    // --------------------Private Inner Class---------------------------------------------------------------------------------------------
    /**
     * Data structure to store the coords of the two end points of a line segment.
     * 
     * @author Niruam
     * 
     */
    private class SegmentCoords {
	private boolean infinity   = false;
	private double  slope;
	private double  sqrtOfSlopeSquarePlus1;  // Used by getNxtXYfor()
	private boolean endReached = false;
	// private int direction = 0; //0 means it has not been set.
	private Port    port[]     = new Port[2];

	Handle	  p1	 = null;
	Handle	  p2	 = null;

	private SegmentCoords() {
	    port[0] = null;
	    port[1] = null;
	}

	SegmentCoords(Handle a, Handle b) {
	    p1 = a;
	    p2 = b;
	    updatePrivate();
	    // direction = 0;
	    port[0] = null;
	    port[1] = null;
	}

	/**
	 * Updates the values of some private data that are pre-calculated during segment's creation for optimisation. The users must call this to
	 * update.
	 */
	public void updatePrivate() {
	    Handle a = p1, b = p2;
	    if (a.getCoord().x == b.getCoord().x) {
		slope = 0;
		sqrtOfSlopeSquarePlus1 = 1;
		infinity = true;
	    } else {
		slope = ((b.getCoord().y - a.getCoord().y) / (b.getCoord().x - a.getCoord().x));
		sqrtOfSlopeSquarePlus1 = Math.sqrt(slope * slope + 1); // Used by getNxtXYfor()
		infinity = false;
	    }
	}

	void setEndPorts(Port a, Port b) {
	    port[0] = a;
	    port[1] = b;
	}

	Port[] getEndPorts() {
	    return port;
	}

	double segLen() {
	    return p1.getCoord().distance(p2.getCoord());
	}

	/**
	 * Same as liesOn(Point2D.Double,int) but uses wire width defined at the top of WireUI class.
	 * 
	 * @param pt
	 * @return
	 */
	boolean liesOn(Point2D.Double pt) {
	    return liesOn(pt, wireWidth); // i.e. it defaults to the wire width defined at the top of WireUI class.
	}

	/**
	 * Return true if pt lies on this line segment.
	 * 
	 * @param pt
	 * @param wireWidth
	 * @return
	 */
	boolean liesOn(Point2D.Double pt, double wireWidth) {
	    double x1, x2, y1, y2;
	    double x, yy;

	    x = pt.x;

	    x1 = p1.getCoord().x;
	    x2 = p2.getCoord().x;
	    y1 = p1.getCoord().y;
	    y2 = p2.getCoord().y;

	    wireWidth = (wireWidth / 2);
	    if (x1 < x2) {
		if (x < x1 - wireWidth || x > x2 + wireWidth) {

		    return false;
		}
	    } else {
		if (x < x2 - wireWidth || x > x1 + wireWidth) {

		    return false;
		}
	    }

	    double y = pt.y;
	    if (infinity) {
		if (y1 <= y2) {
		    if (y < y1 || y > y2)
			return false;
		} else {
		    if (y < y2 || y > y1)
			return false;
		}
		return true;
	    }
	    yy = getYfor(x);
	    if (y < yy - wireWidth || y > yy + wireWidth) {
		//System.out.println("for y  " + (yy - wireWidth) + "  " + (yy + wireWidth) + " " + y + " where x,x1,x2=" + x + " " + x1 + " " + x2);
		return false;
	    } // This is an approximation where actually we are subracting from c of y=mx+c.
	    // This will only hold for small values of wireWidth. For large values c can't
	    else
		// be taken along the width of wire. Actually the width of wire is along a line
		return true; // perpendicular to the actual line, extending half the width of wire on both sides.
	}

	/**
	 * Returns the value of y lying on this line segemnt for a given x. When slope is infinity, i.e the line is parallel to y-axis then 0 is
	 * returned and sets infinity flag to true. Use slopeInfinity() to check it.
	 * 
	 * @param x
	 * @return
	 */
	double getYfor(double x) {
	    double x1, y1;
	    double y;
	    x1 = p1.getCoord().x;
	    y1 = p1.getCoord().y;
	    // x2 = (int) p2.getCoord().x;
	    // y2 = (int) p2.getCoord().y;

	    // int m;
	    if (!infinity) {
		// m =(inst) ((y2-y1) / (x2-x1));
		y = slope * (x - x1) + y1;
		// infinity = false;
	    } else {
		// infinity = true;
		y = 0;
	    }
	    return y;
	}

	/**
	 * Sets flow direction of data in this segment given the 'from' and 'to' coordinates of two different points on this segment.
	 * 
	 * @param from
	 * @param to
	 */
	void setDirection(DataUI dataUi, Point2D.Double to) {
	    Point2D.Double from = dataUi.pos;
	    if (from.equals(to))
		System.out.println("Internal err in Segment:: from==to");
	    double direc;
	    int direction = 0;
	    direc = to.x - from.x;
	    if (direc == 0 || infinity)
		direc = to.y - from.y;

	    if (direc < 0)
		direction = -1;
	    else if (direc > 0)
		direction = 1;
	    else
		System.out.println("Internal Error!!! Value of both y's OR x's are same. So direction is zero. Infinity = " + infinity);
	    dataUi.setDirection(direction);
	}

	/**
	 * Gives the value of next x,y for the given x,y while taking into consideration the dataFlowPacketsDis and slope of this segment.
	 * 
	 * @param curr
	 * @return
	 */
	Point2D.Double getNxtXYfor(DataUI dataUi) {
	    Point2D.Double curr = dataUi.pos;
	    int direction = dataUi.getDirection();
	    if (direction == 0) {
		System.out.println("Internal Error!!! Segment method getNxtXYfor() called without setting the direction of data packet flow!");
		return null;
	    } else
		return getNxtXYfor(curr, direction);
	}

	/**
	 * Gives the value of next x,y for the given x,y while taking into consideration the dataFlowPacketsDis and slope of this segment.
	 * 
	 * @param curr
	 * @param direc
	 *                This decides the direction in which to move (i.e. increment or decrement it). -ve or positive value.
	 * @return
	 */
	Point2D.Double getNxtXYfor(Point2D.Double curr, int direc) {
	    endReached = false;
	    double x, y, x0, y0;

	    x0 = curr.x;
	    y0 = curr.y;

	    if (direc < 0)
		direc = -1;
	    else
		direc = 1;

	    if (infinity) {
		x = x0;
		y = y0 + dataFlowPacketsDis * direc;
	    } else {
		x = x0 + direc * dataFlowPacketsDis / sqrtOfSlopeSquarePlus1;
		y = getYfor(x);
	    }
	    Point2D.Double p = new Point2D.Double(x, y);
	    if (!liesOn(p)) { // The generated coords are out of this line segement so snap it to one of the ends.
		endReached = true;
		if (p1.getCoord().x < p2.getCoord().x) {
		    x = (direc < 0 ? p1.getCoord().x : p2.getCoord().x);
		    y = (direc < 0 ? p1.getCoord().y : p2.getCoord().y);
		} else if (p1.getCoord().x > p2.getCoord().x) {
		    x = (direc > 0 ? p1.getCoord().x : p2.getCoord().x);
		    y = (direc > 0 ? p1.getCoord().y : p2.getCoord().y);
		} else {// infinity
		    x = p1.getCoord().x;
		    if (p1.getCoord().y < p2.getCoord().y)
			y = (direc < 0 ? p1.getCoord().y : p2.getCoord().y);
		    else
			y = (direc > 0 ? p1.getCoord().y : p2.getCoord().y);
		}
	    }
	    p.setLocation(x, y);
	    return p;
	}

	boolean slopeInfinity() {
	    return infinity;
	}

	boolean isEndReached() {
	    return endReached;
	}

	boolean equals(SegmentCoords s) {
	    if ((p1.equals(s.p1) && p2.equals(s.p2)) || (p1.equals(s.p2) && p2.equals(s.p1)))
		return true;
	    else
		return false;
	}

	void render(Graphics g) {
	    // g.drawLine((int)p1.getCoord().x, (int)p1.getCoord().y, (int)p2.getCoord().x, (int)p2.getCoord().y);
	    Color colour = normalColour;
	    if (wire.isDroppingPackets())
		colour = dropPacketColour;
	    UtilGraphics.drawThickLine(g, (int) p1.getCoord().x, (int) p1.getCoord().y, (int) p2.getCoord().x, (int) p2.getCoord().y, wireWidth,
		    colour);
	}
    }
}