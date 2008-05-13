/*
 * WireModWinUI.java
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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import module.ModuleWinUI;
import module.moduleWindow;

/**
 * 
 * @author Nirupam
 * 
 */
public class WireModWinUI implements ActionListener, ModuleWinUI {
    JPanel       propPg      = null;
    JCheckBox    dropPackets = null;
    moduleWindow modWin      = null;
    WireUI       wireUI      = null;

    public WireModWinUI(WireUI w) {
	wireUI = w;
    }

    public JPanel getPropertyPage() {
	if (propPg == null) {
	    propPg = new JPanel();
	    propPg.setLayout(new BorderLayout());
	    propPg.add(getChkEmit(), BorderLayout.CENTER);
	    propPg.setPreferredSize(new Dimension(150, 100));
	}
	return propPg;
    }

    private JCheckBox getChkEmit() {
	if (dropPackets == null) {
	    dropPackets = new JCheckBox("Drop Packets");
	    dropPackets.setSelected(wireUI.wire.isDroppingPackets());
	    dropPackets.addActionListener(this);
	}
	return dropPackets;
    }

    public boolean isPropertyPageAvailable(Mode mode) {
	if (mode == Mode.EDIT_MODE || mode == Mode.SIMULATION_MODE)
	    return true;
	else
	    return false;
    }

    public void actionPerformed(ActionEvent e) {
	wireUI.wire.setDropPackets(dropPackets.isSelected());
    }

    public void reset() {
	if (modWin != null)
	    modWin.reset();
    }

    public Point getCoord() {
	return new Point(30, 30); // TODO generate a coord value based on one of the two modules.
    }

    public String getNameToDisplay() {
	return "Wire " + wireUI.toString();
    }
}
