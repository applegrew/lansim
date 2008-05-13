/*
 * DroppedPacketDataUI.java
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA. 
 */
package module;

import java.awt.Graphics;
import java.awt.geom.Point2D;

import framework.Packet;

/**
 * Created on 12-May-08
 *
 * @author Nirupam
 * 
 */
public class DroppedPacketDataUI extends DataUI {
    private int	    renderss2live = 15;
    private int	    reners2pause  = 15;
    private Point2D.Double lastPos;

    public DroppedPacketDataUI(Packet p) {
	super(p);
	setDisplayString("Dropped!");
    }

    public void render(Graphics g) {
	if (renderss2live > 0) {
	    super.render(g);
	    lastPos = super.pos;
	    renderss2live--;
	} else if (reners2pause > 0) {
	    super.pos = lastPos;
	    super.render(g);
	    reners2pause--;
	}
    }

    public boolean isAlive() {
	return renderss2live > 0 || reners2pause > 0;
    }
}
