/*
 * DataUI.java
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
package module;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

import framework.Packet;

/**
 * @author Nirupam
 * 
 */
public class DataUI {
    private static final int width      = 10;
    private static final int height     = 10;
    private int	      direction  = 0;   // 0 means it has not been set.
    private String	   dispString = null;
    Packet		   packet     = null;
    public Point2D.Double    pos;
    private Color	    colour;

    public DataUI(Packet p) {
	colour = Color.GREEN;
	pos = new Point2D.Double(-1, -1);// -ve coord means it is not placed anywhere.
	dispString = null;
	packet = p;
    }

    public DataUI(DataUI d) {
	colour = d.colour;
	pos = new Point2D.Double(d.pos.x, d.pos.y);
	dispString = new String(d.dispString);
	direction = d.direction;
    }

    public void setDisplayString(String s) {
	if (s != null && s.trim() == "")
	    s = null;
	dispString = s;
    }

    /**
         * Renders the data packet at x,y
         * 
         * @param g
         */
    public void render(Graphics g) {
	Color t = g.getColor();

	g.setColor(colour);
	g.fillOval((int) (pos.x - (double) (width / 2)), (int) (pos.y - (double) (height / 2)), width, height);
	if (dispString != null) {
	    g.setColor(Color.BLACK);
	    g.drawString(dispString, (int) pos.x + width, (int) pos.y + height);
	}
	g.setColor(t);
    }

    // /**
    // * Sets position of data packet in the client area.
    // * @param x
    // * @param y
    // */
    // public void setPos(int x, int y){
    // this.x=x;
    // this.y=y;
    // }

    public void setColour(Color color) {
	colour = color;
    }
    
    public Color getColour(){
	return colour;
    }

    public void setDirection(int dir) {
	direction = dir;
    }

    public int getDirection() {
	return direction;
    }

    public String getDisplayString() {
	return dispString;
    }
}
