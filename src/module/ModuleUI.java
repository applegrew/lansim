/*
 * ModuleUI.java
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

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import framework.Packet;
import framework.Port;
import ui.ComponentUI;
import ui.ManagerUI;
import ui.Mode;

import java.awt.Color;
import java.awt.geom.Point2D;

/**
 * 
 * @author Nirupam
 * @author Rohit
 */
public class ModuleUI extends ComponentUI implements ModuleWinUI {
    public static final int NONE     = 0;
    public static final int SELECTED = 1;
    public static final int MOVING   = 2;
    private Module	  module   = null;
    private Image	   image    = null;
    private Point	   coord    = null; // The coordinate of the module's upper-left corner.
    private int	     width    = 30;
    private int	     height   = 30;
    private int	     status   = NONE;
    moduleWindow	    modWin   = null;

    public ModuleUI(Module m) {
	module = m;
	coord = new Point();
    }

    /**
     * Must be overridden by deriving class so as to display a custom property tab in the properties dialog of the module. If this is not
     * overriden (i.e. null is returned) then no custom property tab is shown.
     * 
     * @return The JPanel object with all the custom buttons and fields in it. The events generated on this must be handeled by the module itself.
     */
    public JPanel getPropertyPage() {
	return null;
    }

    /**
     * Returns true if PropertyPage available for the give mode (SIMULATION_MODE, EDIT_MODE, etc.) else false.
     * 
     * @param mode
     * @return
     */
    public boolean isPropertyPageAvailable(ui.Mode mode) {
	return false;
    }

    /**
     * Must be overriden by deriving class to display the desired name in UI.
     * 
     * @return The name to be displayed.
     */
    public String getNameToDisplay() {
	// return module==null?" ":module.getName();
	return module.getName() + module.getSno();
    }

    /**
     * Override this if your module needs to update its state while simulating in SIMULATION_MODE or PAUSED_MODE or EDIT_MODE.
     * 
     * @param mode
     *                The mode in which this function has been called.
     * 
     */
    public void stepSimulation(Mode mode) {
	if (mode == Mode.SIMULATION_MODE) {
	    if (module.isNewPacket()) {
		Port ports[] = module.getPorts();
		for (int i = 0; i < module.getNoOfPorts(); i++) {
		    Packet emittedPacket = ports[i].getEmittedPacket();
		    if (emittedPacket != null) {
			if (emittedPacket.dataUI == null)
			    emittedPacket.dataUI = getNewDataUI(emittedPacket);
		    }
		}
	    }
	}
    }

    /**
     * Used by setpSimulation to get a new instance of DataUI. If the module writer just creates a new subclass of DataUI then he must override
     * this method to return object its custom DataUI (sub) class. If the module writer overrides also setpSimulation then instead of re-writing
     * the codes in stepSimulation he can just override this method too (as described above) and make a call to super.setpSimulation().
     * 
     * @return
     */
    protected DataUI getNewDataUI(Packet p) {
	DataUI dataUI = null;
	if (p.isDroppedPacket())
	    dataUI = new DroppedPacketDataUI(p);
	else
	    dataUI = new DataUI(p);
	if (p.isReply == true)
	    dataUI.setColour(Color.BLUE);
	if (p.isCorrupt())
	    dataUI.setColour(Color.RED);
	return dataUI;
    }

    /**
     * All rendering of visuals go here. Override this to create your own module with custom looks.
     * 
     * @param g
     * @param CallingObj
     */
    public void render(Graphics g, ManagerUI CallingObj) {
	Color c = g.getColor();
	if (image == null) {// Syncronisingly loading the image.
	    ImageIcon icon = new ImageIcon(module.getIconPath());
	    image = icon.getImage();

	    // image = Toolkit.getDefaultToolkit().getImage("module/"+module.getName()+"/icon.gif");
	    // MediaTracker tracker = new MediaTracker(CallingObj);
	    // tracker.addImage(image, 0);
	    // try{tracker.waitForID(0);}
	    // catch(InterruptedException e){}

	}
	g.drawImage(image, (int) coord.x, (int) coord.y, width, height, null);
	g.setColor(Color.black);
	g.drawString(module.getName() + module.getSno(), ((int) (coord.x + width / 2)) + 15, (int) coord.y + height / 2);
	if (status == SELECTED) {
	    g.setColor(Color.blue);
	    g.drawRect((int) coord.x - 2, (int) coord.y - 2, width + 4, height + 4);

	}
	if (status == MOVING) {
	    g.setColor(Color.black);
	    g.drawRect((int) coord.x + width + 5, (int) coord.y + height + 5, 5, 5);
	    g.drawRect((int) coord.x + width + 5, (int) coord.y - 10, 5, 5);
	    g.drawRect((int) coord.x - 10, (int) coord.y + height + 5, 5, 5);
	    g.drawRect((int) coord.x - 10, (int) coord.y - 10, 5, 5);
	}
	g.setColor(c);
    }

    public void setCoord(Point p) {
	/*
	 * coord.x=p.x; coord.y=p.y;
	 */
	coord.x = p.x - width / 2;
	coord.y = p.y - height / 2;
    }

    public Point getCoord() {
	// return new Point(coord.x+height/2,coord.y+width/2);
	return new Point(coord.x + width / 2, coord.y + height / 2);
    }

    public Point2D.Double getDoubleCoord() {
	// return new Point2D.Double(coord.x+((double)height)/2,coord.y+((double)width)/2);
	return new Point2D.Double(coord.x + ((double) width) / 2, coord.y + ((double) height) / 2);
    }

    public boolean isClicked(Point pt) {
	return (new Rectangle((int) coord.x, (int) coord.y, width, height)).contains(pt);

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

    public void reset() {
	if (modWin != null)
	    modWin.reset();
    }

    public moduleWindow getModWin() {
	return modWin;
    }

    public void setModWin(moduleWindow m) {
	modWin = m;
    }
}