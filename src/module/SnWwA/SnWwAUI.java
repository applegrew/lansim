/*
 * SnWwAUI.java
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
package module.SnWwA;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import framework.Packet;
import module.DataUI;
import module.ModuleUI;
import ui.Mode;

/**
 * @author Nirupam
 * 
 */
public class SnWwAUI extends ModuleUI implements ActionListener {
    JPanel     propPg	   = null;
    JCheckBox  chkEmit	  = null;
    JTextField txtTargetSlNo    = null;
    JButton    cmdSetVal	= null;
    JTextField txtTimeoutVal    = null;
    JButton    cmdSetTimeoutVal = null;
    SnWwAMod   mod	      = null;

    public SnWwAUI(SnWwAMod m) {
	super(m);
	mod = m;
    }

    protected DataUI getNewDataUI(Packet p) {
	DataUI dui = super.getNewDataUI(p);
	String lbl = p.getFromId() + "->" + p.getToId();
	if (p.isReply) {
	    lbl = lbl + " ACKNo:" + mod.rcv_ackno;
	} else {
	    lbl = lbl + " FNo:" + mod.snd_frameno;
	}
	dui.setDisplayString(lbl);
	return dui;
    }

    public JPanel getPropertyPage() {
	if (propPg == null) {
	    propPg = new JPanel();
	    propPg.setLayout(new GridLayout(6, 1));
	    propPg.add(new JPanel());
	    propPg.add(getChkEmit());
	    propPg.add(new JPanel());
	    propPg.add(getTargetSlNo());
	    propPg.add(new JPanel());
	    propPg.add(getTimeoutTimer());
	    propPg.setPreferredSize(new Dimension(150, 100));
	}
	return propPg;
    }

    private JPanel getTargetSlNo() {
	JPanel jTSN = null;
	if (txtTargetSlNo == null) {
	    jTSN = new JPanel();
	    jTSN.setLayout(new GridLayout(1, 3));

	    txtTargetSlNo = new JTextField();
	    txtTargetSlNo.setMaximumSize(new Dimension(150, 30));
	    txtTargetSlNo.setText(String.valueOf(mod.snd_targetSlNo));
	    txtTargetSlNo.addActionListener(this);
	    txtTargetSlNo.setEnabled(false);

	    cmdSetVal = new JButton("Set Value");
	    cmdSetVal.setName("cmdSetVal");
	    cmdSetVal.addActionListener(this);
	    cmdSetVal.setEnabled(false);

	    jTSN.add(new JLabel("Target PC Sl No.: "));
	    jTSN.add(txtTargetSlNo);
	    jTSN.add(cmdSetVal);
	    jTSN.setPreferredSize(new Dimension(150, 50));
	}
	return jTSN;
    }

    private JPanel getTimeoutTimer() {
	JPanel jTSN = null;
	if (txtTimeoutVal == null) {
	    jTSN = new JPanel();
	    jTSN.setLayout(new GridLayout(1, 3));

	    txtTimeoutVal = new JTextField();
	    txtTimeoutVal.setMaximumSize(new Dimension(150, 30));
	    txtTimeoutVal.setText(String.valueOf(mod.sendTimer));
	    txtTimeoutVal.addActionListener(this);
	    txtTimeoutVal.setEnabled(false);

	    cmdSetTimeoutVal = new JButton("Set Value");
	    cmdSetTimeoutVal.setName("cmdSetTimeoutVal");
	    cmdSetTimeoutVal.addActionListener(this);
	    cmdSetTimeoutVal.setEnabled(false);

	    jTSN.add(new JLabel("Timeout Value: "));
	    jTSN.add(txtTimeoutVal);
	    jTSN.add(cmdSetTimeoutVal);
	    jTSN.setPreferredSize(new Dimension(150, 50));
	}
	return jTSN;
    }

    private JCheckBox getChkEmit() {
	if (chkEmit == null) {
	    chkEmit = new JCheckBox("Send Packets");
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
	if (e.getSource() instanceof JCheckBox) {
	    mod.canEmitData = chkEmit.isSelected();
	    if (chkEmit.isSelected()) {
		txtTargetSlNo.setEnabled(true);
		cmdSetVal.setEnabled(true);
		txtTimeoutVal.setEnabled(true);
		cmdSetTimeoutVal.setEnabled(true);
	    } else {
		txtTargetSlNo.setEnabled(false);
		cmdSetVal.setEnabled(false);
		txtTimeoutVal.setEnabled(false);
		cmdSetTimeoutVal.setEnabled(false);
	    }
	} else if (e.getSource() instanceof JButton) {
	    JButton jb = (JButton) e.getSource();
	    if (jb.getName().equals("cmdSetVal"))
		mod.snd_targetSlNo = Integer.parseInt((txtTargetSlNo.getText()));
	    else if (jb.getName().equals("cmdSetTimeoutVal"))
		mod.sendTimer = Integer.parseInt((txtTimeoutVal.getText()));
	}
    }
}
