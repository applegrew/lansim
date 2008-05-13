/*
 * moduleWindow.java
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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ui.main.Config;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This creates a window for the modules to display messages. It also autoscrolls the JTexAreas. Each JTexArea's autoscrolling can be set
 * independently.
 * 
 * @author Rohit
 * @author Nirupam
 */
public class moduleWindow implements ActionListener {
    private JFrame    jFrame	  = null;
    private JPanel    jContentPane    = null;
    private JCheckBox chkAllwaysOnTop = null;
    private JCheckBox chkAutoScroll   = null;
    private JPanel    propPg	  = null;
    private JPanel    noPropPg	= null;
    private JPanel    prop	    = null;
    ModuleWinUI       owner	   = null;
    JTabbedPane       ClientArea      = null;
    public JTextArea  cmd	     = null;
    public JTextArea  msg	     = null;
    public JTextArea  amsg	    = null;
    public JTextArea  stat	    = null;
    public JTextArea  help	    = null;
    private boolean   msg_autoscroll  = true;
    private boolean   amsg_autoscroll = true;
    private boolean   cmd_autoscroll  = true;
    private boolean   stat_autoscroll = true;
    private boolean   help_autoscroll = true;

    public moduleWindow(ModuleWinUI mod) {
	owner = mod;
	if (jFrame == null) {
	    jFrame = new JFrame();
	    jFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	    jFrame.setSize(350, 250);
	    Point loc = mod.getCoord();
	    jFrame.setAlwaysOnTop(true);
	    jFrame.setLocation(loc.x + 50, loc.y + 50);
	    cmd = new JTextArea();
	    msg = new JTextArea();
	    amsg = new JTextArea();
	    stat = new JTextArea();
	    help = new JTextArea();

	    cmd.setEditable(true);
	    msg.setEditable(false);
	    amsg.setEditable(false);
	    stat.setEditable(false);
	    help.setEditable(false);

	    cmd.setSize(310, 200);
	    msg.setSize(310, 200);
	    amsg.setSize(310, 200);
	    stat.setSize(310, 200);
	    help.setSize(310, 200);

	    msg.getDocument().addDocumentListener(new DocumentListener() {
		public void changedUpdate(DocumentEvent e) {}

		public void insertUpdate(DocumentEvent e) {
		    JTextArea m = msg;
		    if (msg_autoscroll)
			m.setCaretPosition(m.getDocument().getLength());
		}

		public void removeUpdate(DocumentEvent e) {}
	    });
	    amsg.getDocument().addDocumentListener(new DocumentListener() {
		public void changedUpdate(DocumentEvent e) {}

		public void insertUpdate(DocumentEvent e) {
		    JTextArea m = amsg;
		    if (amsg_autoscroll)
			m.setCaretPosition(m.getDocument().getLength());
		}

		public void removeUpdate(DocumentEvent e) {}
	    });
	    cmd.getDocument().addDocumentListener(new DocumentListener() {
		public void changedUpdate(DocumentEvent e) {}

		public void insertUpdate(DocumentEvent e) {
		    JTextArea m = cmd;
		    if (cmd_autoscroll)
			m.setCaretPosition(m.getDocument().getLength());
		}

		public void removeUpdate(DocumentEvent e) {}
	    });
	    stat.getDocument().addDocumentListener(new DocumentListener() {
		public void changedUpdate(DocumentEvent e) {}

		public void insertUpdate(DocumentEvent e) {
		    JTextArea m = stat;
		    if (stat_autoscroll)
			m.setCaretPosition(m.getDocument().getLength());
		}

		public void removeUpdate(DocumentEvent e) {}
	    });
	    help.getDocument().addDocumentListener(new DocumentListener() {
		public void changedUpdate(DocumentEvent e) {}

		public void insertUpdate(DocumentEvent e) {
		    JTextArea m = help;
		    if (help_autoscroll)
			m.setCaretPosition(m.getDocument().getLength());
		}

		public void removeUpdate(DocumentEvent e) {}
	    });

	    jFrame.setContentPane(getJContentPane());
	    jFrame.setTitle(mod.getNameToDisplay());
	    jFrame.setIconImage(new ImageIcon(Config.iconPath + "module.png").getImage());
	}
    }

    private JPanel getJContentPane() {
	if (jContentPane == null) {
	    jContentPane = new JPanel();
	    jContentPane.setLayout(new BorderLayout());
	    jContentPane.add(getClientArea(), BorderLayout.CENTER); // Generated
	    JPanel jPane = new JPanel();
	    jPane.setLayout(new BorderLayout());
	    jPane.add(getchkAllwaysOnTop(), BorderLayout.WEST);
	    jPane.add(getchkAutoScroll(), BorderLayout.EAST);
	    jContentPane.add(jPane, BorderLayout.SOUTH);
	    chkAutoScroll.setSelected(cmd_autoscroll);
	}
	return jContentPane;
    }

    private JTabbedPane getClientArea() {
	if (ClientArea == null) {
	    ClientArea = new JTabbedPane();
	    ClientArea.setTabPlacement(JTabbedPane.BOTTOM); // Generated
	    ClientArea.setPreferredSize(new Dimension(100, 100)); // Generated
	    ClientArea.setBackground(Color.white); // Generated
	    ClientArea.addTab("Commands", new JScrollPane(cmd));
	    ClientArea.addTab("Messages", new JScrollPane(msg));
	    ClientArea.addTab("App Msg", new JScrollPane(amsg));
	    ClientArea.addTab("Statistics", new JScrollPane(stat));
	    ClientArea.addTab("Help", new JScrollPane(help));
	    propPg = owner.getPropertyPage();
	    if (propPg != null) {
		noPropPg = new JPanel();
		noPropPg.setLayout(new BorderLayout());
		noPropPg.add(new JLabel("Property Page is not available in this mode."), BorderLayout.CENTER);

		prop = new JPanel();
		prop.setLayout(new BorderLayout());
		prop.add(propPg, BorderLayout.CENTER);
		prop.setPreferredSize(propPg.getPreferredSize());
		ClientArea.addTab("Property", new JScrollPane(prop));// Sets property window.
	    }

	    ClientArea.addChangeListener(new ChangeListener() {

		public void stateChanged(ChangeEvent e) {
		    int i = ClientArea.getSelectedIndex();
		    chkAutoScroll.setEnabled(true);
		    switch (i) {
		    case 0:
			chkAutoScroll.setSelected(cmd_autoscroll);
			break;
		    case 1:
			chkAutoScroll.setSelected(msg_autoscroll);
			break;
		    case 2:
			chkAutoScroll.setSelected(amsg_autoscroll);
			break;
		    case 3:
			chkAutoScroll.setSelected(stat_autoscroll);
			break;
		    case 4:
			chkAutoScroll.setSelected(help_autoscroll);
			break;
		    case 5:
			chkAutoScroll.setSelected(false);
			chkAutoScroll.setEnabled(false);
		    }
		}
	    });
	    ClientArea.setPreferredSize(new Dimension(310, 200));

	}
	return ClientArea;
    }

    /**
         * Makes property page visible or hides it based on the given mode.
         * 
         * @param mode
         */
    public void updatePropertyPageState(ui.Mode mode) {
	if (propPg != null) {
	    if (owner.isPropertyPageAvailable(mode)) {
		prop.remove(noPropPg);
		prop.add(propPg, BorderLayout.CENTER);
		prop.revalidate();
	    } else {
		prop.remove(propPg);
		prop.add(noPropPg, BorderLayout.CENTER);
		prop.revalidate();
	    }
	}
    }

    public void makeWindowVisible() {
	jFrame.setVisible(true);
    }

    private JCheckBox getchkAllwaysOnTop() {
	if (chkAllwaysOnTop == null) {
	    chkAllwaysOnTop = new JCheckBox();
	    chkAllwaysOnTop.addActionListener(this);
	    chkAllwaysOnTop.setText("Allways on top");
	    chkAllwaysOnTop.setSelected(jFrame.isAlwaysOnTop());
	}
	return chkAllwaysOnTop;
    }

    private JCheckBox getchkAutoScroll() {
	if (chkAutoScroll == null) {
	    chkAutoScroll = new JCheckBox();
	    chkAutoScroll.addActionListener(this);
	    chkAutoScroll.setText("Autoscroll");
	}
	return chkAutoScroll;
    }

    public void reset() {
	cmd.setText("");
	msg.setText("");
	amsg.setText("");
	stat.setText("");
	msg_autoscroll = true;
	amsg_autoscroll = true;
	cmd_autoscroll = true;
	stat_autoscroll = true;
	help_autoscroll = true;
	jFrame.setAlwaysOnTop(true);
	chkAllwaysOnTop.setSelected(true);
    }

    public void actionPerformed(ActionEvent e) {
	Object src = e.getSource();
	if (src.equals(chkAllwaysOnTop)) {
	    jFrame.setAlwaysOnTop(chkAllwaysOnTop.isSelected());
	}
	if (src.equals(chkAutoScroll)) {
	    int i = ClientArea.getSelectedIndex();
	    boolean state = chkAutoScroll.isSelected();
	    switch (i) {
	    case 0:
		cmd_autoscroll = state;
		break;
	    case 1:
		msg_autoscroll = state;
		break;
	    case 2:
		amsg_autoscroll = state;
		break;
	    case 3:
		stat_autoscroll = state;
		break;
	    case 4:
		help_autoscroll = state;
		break;
	    }
	}
    }
}
