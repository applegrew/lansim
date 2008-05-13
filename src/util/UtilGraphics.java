/*
 * UtilGraphics.java
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
package util;

import java.awt.Color;
import java.awt.Graphics;

/**
 * 
 * @author Nirupam
 * @author Rohit
 */
public class UtilGraphics {
    /**
         * Source http://www.rgagnon.com/howto.html
         */
    public static void drawThickLine(Graphics g, int x1, int y1, int x2, int y2, int thickness, Color c) {
	// The thick line is in fact a filled polygon
	Color tc = g.getColor();
	g.setColor(c);

	if (thickness == 1) {
	    g.drawLine(x1, y1, x2, y2);
	} else {
	    int dX = x2 - x1;
	    int dY = y2 - y1;
	    // line length
	    double lineLength = Math.sqrt(dX * dX + dY * dY);

	    double scale = (double) (thickness) / (2 * lineLength);

	    // The x,y increments from an endpoint needed to create a rectangle...
	    double ddx = -scale * (double) dY;
	    double ddy = scale * (double) dX;
	    ddx += (ddx > 0) ? 0.5 : -0.5;
	    ddy += (ddy > 0) ? 0.5 : -0.5;
	    int dx = (int) ddx;
	    int dy = (int) ddy;

	    // Now we can compute the corner points...
	    int xPoints[] = new int[4];
	    int yPoints[] = new int[4];

	    xPoints[0] = x1 + dx;
	    yPoints[0] = y1 + dy;
	    xPoints[1] = x1 - dx;
	    yPoints[1] = y1 - dy;
	    xPoints[2] = x2 - dx;
	    yPoints[2] = y2 - dy;
	    xPoints[3] = x2 + dx;
	    yPoints[3] = y2 + dy;

	    g.fillPolygon(xPoints, yPoints, 4);
	}

	g.setColor(tc);
    }
}
