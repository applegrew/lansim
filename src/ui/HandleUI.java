/*
 * HandleUI.java
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
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import module.ModuleUI;

/**
 * @author apple
 * 
 */
public class HandleUI extends ComponentUI {
    public static final int NONE      = 0;
    public static final int SELECTED  = 1;
    public static final int MOVING    = 2;

    final private int       dimension = 6;
    private int	     status    = NONE;
    public WireUI	   wireUIOwner;
    public Handle	   handle;
    public ComponentUI      owner;	   // Cannot be null. If it lies naked on the wire then its owner is WireUI, else has owners like
                                                // ModuleUI.

    // It is only rendered if owner is WireUI.

    public HandleUI(Handle h, ComponentUI o) {
	handle = h;
	owner = o;
	if (owner instanceof WireUI)
	    wireUIOwner = (WireUI) owner;
	else
	    wireUIOwner = null;
	status = HandleUI.NONE;
    }

    /*
         * (non-Javadoc)
         * 
         * @see ui.ComponentUI#isClicked(java.awt.Point)
         */
    @Override
    public boolean isClicked(Point pt) {
	int Dimension = dimension + 12;
	int relaxPosBy = 1;
	int X = (int) (handle.getCoord().x - Dimension / 2);
	int Y = (int) (handle.getCoord().y - Dimension / 2);
	boolean ret = (new Rectangle(X - relaxPosBy, Y - relaxPosBy, Dimension, Dimension)).contains(pt);
	// System.out.println("isClicked called. Returned "+ret);
	return ret;
    }

    public boolean isClicked(Point2D.Double pt) {
	Point p = new Point((int) pt.x, (int) pt.y);
	return isClicked(p);
    }

    /**
         * If the owner is instance of WireUI then returns true. This method is named is_naked() because this is only when the handle is rendered.
         * 
         * @return
         */
    public boolean is_naked() {
	if (owner instanceof WireUI)
	    return true;
	else
	    return false;
    }

    public void render(Graphics g) {
	if (owner instanceof WireUI) {
	    Point2D.Double coord = handle.getCoord();
	    g.setColor(Color.GREEN);
	    g.fillRect((int) (coord.x - dimension / 2), (int) (coord.y - dimension / 2), dimension, dimension);
	}
    }

    final public void setSelected(boolean flag) {
	selected = flag;
	if (flag == true)
	    status = ModuleUI.SELECTED;
	else
	    status = ModuleUI.NONE;
    }

    final public void setStatus(int s) {
	status = s;
	if (s == SELECTED)
	    super.setSelected(true);
	else
	    super.setSelected(false);
    }

    final public int getStatus() {
	return status;
    }
}
