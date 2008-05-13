/*
 * NodeUI.java
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
package module.Node;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import framework.Packet;
import module.DataUI;
import module.ModuleUI;
import ui.Mode;

/**
 * @author Nirupam
 * 
 */
public class NodeUI extends ModuleUI implements ActionListener {
    JPanel    propPg  = null;
    JCheckBox chkEmit = null;
    NodeMod   mod     = null;

    public NodeUI(NodeMod m) {
	super(m);
	mod = m;
    }

    protected DataUI getNewDataUI(Packet p) {
	DataUI dui = super.getNewDataUI(p);
	dui.setDisplayString(p.getFromId() + "->" + p.getToId());
	return dui;
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
	if (chkEmit == null) {
	    chkEmit = new JCheckBox("Emit Packets");
	    chkEmit.setSelected(mod.canEmitData);
	    chkEmit.addActionListener(this);
	}
	return chkEmit;
    }

    public boolean isPropertyPageAvailable(Mode mode) {
	if (mode == Mode.EDIT_MODE)
	    return true;
	else
	    return false;
    }

    public void actionPerformed(ActionEvent e) {
	mod.canEmitData = chkEmit.isSelected();// When Loader (our custom class loader) was used to load NodeMod this line
	// allways threw "LinkageError NodeMod violates loader constraints" runtime exception. Phew! We got rid of that of
	// that by using Class.forName().
    }
}
