/*
 * Handle.java
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

import java.awt.geom.Point2D;

/**
 * This is responsible for giving the location of the bends.
 * 
 * @author Nirupam
 * 
 */
public class Handle {
    private Point2D.Double coord    = null;
    private boolean	portFlag = false; // This is true if this handle represents a port.

    /*
         * Handle(Point2D.Double p){ coord = new Point2D.Double(); coord.setLocation(p); }
         */

    Handle(Point2D.Double p, boolean isPort) {
	coord = new Point2D.Double();
	coord.setLocation(p);
	portFlag = isPort;
    }

    boolean equals(Handle h) {
	if (coord.x == h.coord.x && coord.y == h.coord.y && portFlag == h.portFlag)
	    return true;
	else
	    return false;
    }

    Point2D.Double getCoord() {
	return coord;
    }

    boolean isPort() {
	return portFlag;
    }

    public void setCoord(Point2D.Double p) {
	coord.setLocation(p);
    }
}
