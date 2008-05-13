/*
 * MainWindow.java
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
package ui.main;

import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Event;
import java.awt.BorderLayout;
import java.awt.Image;

import javax.swing.SwingConstants;
import javax.swing.KeyStroke;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JToolBar;
import javax.swing.JToggleButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import java.awt.Insets;
import java.awt.ComponentOrientation;

import javax.swing.border.EtchedBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.JProgressBar;

import ui.ManagerUI;

import java.util.ArrayList;

/**
 * @author Nirupam
 * 
 */
class MainWindow {

    // ArrayList<ManagerUI> clientSpace = null;

    JScrollPane	  jScp		  = null;

    JFrame	       jFrame		= null; // @jve:decl-index=0:visual-constraint="10,10"
    private JPanel       jContentPane	  = null;
    private JMenuBar     jJMenuBar	     = null;
    private JMenu	fileMenu	      = null;
    private JMenu	editMenu	      = null;
    private JMenu	viewMenu	      = null;
    private JMenu	helpMenu	      = null;
    JMenuItem	    exitMenuItem	  = null;
    JMenuItem	    aboutMenuItem	 = null;
    JMenuItem	    cutMenuItem	   = null;
    JMenuItem	    copyMenuItem	  = null;
    JMenuItem	    pasteMenuItem	 = null;
    JMenuItem	    saveMenuItem	  = null;
    JMenuItem	    newMenuItem	   = null;
    JMenuItem	    openMenuItem	  = null;
    JMenuItem	    componentMenuItem     = null;
    private JDialog      aboutDialog	   = null; // @jve:decl-index=0:visual-constraint="312,558"
    private JPanel       aboutContentPane      = null;
    private JLabel       aboutVersionLabel     = null;
    private JLabel       jLabel		= null;
    private JToolBar     TaskToolBar	   = null;
    JToggleButton	cmdRun		= null;
    JButton	      cmdNew		= null;
    JButton	      cmdSave	       = null;
    JButton	      cmdOpen	       = null;
    JToggleButton	cmdPause	      = null;
    private JToolBar     ComponentToolBar      = null;
    JButton	      cmdComponents[]       = null; // This will contain all the components' buttons.
    String	       ComponentsName[]      = null; // Both this and cmdComponents are initialized in ApplicationManager.
    String	       ComponentModulePath[] = null; // Path to Component modules. Set my ApplicationManager.
    private JPanel       StatusBar	     = null;
    private JLabel       StatusLabel	   = null;
    private JProgressBar ProgressBar	   = null;
    // JTabbedPane ClientArea = null;
    ArrayList<ManagerUI> clientSpace	   = null;

    /**
         * This method initializes jFrame
         * 
         * @return javax.swing.JFrame
         */
    private JFrame getJFrame() {
	if (jFrame == null) {
	    jFrame = new JFrame();
	    jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    jFrame.setJMenuBar(getJJMenuBar());
	    jFrame.setSize(1014, 534);
	    jFrame.setContentPane(getJContentPane());
	    jFrame.setTitle("LANSim");
	    jFrame.setIconImage(new ImageIcon(Config.iconPath + "lansim.png").getImage());
	}
	return jFrame;
    }

    /**
         * This method initializes jContentPane
         * 
         * @return javax.swing.JPanel
         */
    private JPanel getJContentPane() {
	if (jContentPane == null) {
	    jContentPane = new JPanel();
	    jContentPane.setLayout(new BorderLayout());

	    jContentPane.add(getTaskToolBar(), BorderLayout.NORTH); // Generated

	    JToolBar jComponentToolBar = getComponentToolBar();
	    if (jComponentToolBar != null)
		jContentPane.add(jComponentToolBar, BorderLayout.WEST); // Generated

	    jContentPane.add(getStatusBar(), BorderLayout.SOUTH); // Generated
	    // jContentPane.add(getClientArea(), BorderLayout.CENTER); // Generated
	    jContentPane.add(getClientArea(), BorderLayout.CENTER);
	}
	return jContentPane;
    }

    /**
         * This method initializes jJMenuBar
         * 
         * @return javax.swing.JMenuBar
         */
    private JMenuBar getJJMenuBar() {
	if (jJMenuBar == null) {
	    jJMenuBar = new JMenuBar();
	    jJMenuBar.add(getFileMenu());
	    jJMenuBar.add(getEditMenu());
	    jJMenuBar.add(getViewMenu());
	    jJMenuBar.add(getHelpMenu());
	}
	return jJMenuBar;
    }

    /**
         * This method initializes jMenu
         * 
         * @return javax.swing.JMenu
         */
    private JMenu getFileMenu() {
	if (fileMenu == null) {
	    fileMenu = new JMenu();
	    fileMenu.setText("File");
	    fileMenu.add(getNewMenuItem());
	    fileMenu.add(getOpenMenuItem());
	    fileMenu.add(getSaveMenuItem());
	    fileMenu.add(getExitMenuItem());
	}
	return fileMenu;
    }

    /**
         * This method initializes jMenu
         * 
         * @return javax.swing.JMenu
         */
    private JMenu getEditMenu() {
	if (editMenu == null) {
	    editMenu = new JMenu();
	    editMenu.setText("Edit");
	    editMenu.add(getCutMenuItem());
	    editMenu.add(getCopyMenuItem());
	    editMenu.add(getPasteMenuItem());
	}
	return editMenu;
    }

    private JMenu getViewMenu() {
	if (viewMenu == null) {
	    viewMenu = new JMenu();
	    viewMenu.setText("View");
	    viewMenu.add(getComponentMenuItem());
	}
	return viewMenu;
    }

    /**
         * This method initializes jMenu
         * 
         * @return javax.swing.JMenu
         */
    private JMenu getHelpMenu() {
	if (helpMenu == null) {
	    helpMenu = new JMenu();
	    helpMenu.setText("Help");
	    helpMenu.add(getAboutMenuItem());
	}
	return helpMenu;
    }

    /**
         * This method initializes jMenuItem
         * 
         * @return javax.swing.JMenuItem
         */
    private JMenuItem getExitMenuItem() {
	if (exitMenuItem == null) {
	    exitMenuItem = new JMenuItem();
	    exitMenuItem.setText("Exit");
	    exitMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    System.exit(0);
		}
	    });
	}
	return exitMenuItem;
    }

    /**
         * This method initializes jMenuItem
         * 
         * @return javax.swing.JMenuItem
         */
    private JMenuItem getAboutMenuItem() {
	if (aboutMenuItem == null) {
	    aboutMenuItem = new JMenuItem();
	    aboutMenuItem.setText("About");
	    aboutMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    JDialog aboutDialog = getAboutDialog();
		    aboutDialog.pack();
		    aboutDialog.setBounds(400, 250, 200, 100);
		    aboutDialog.setVisible(true);
		}
	    });
	}
	return aboutMenuItem;
    }

    /**
         * This method initializes aboutDialog
         * 
         * @return javax.swing.JDialog
         */
    private JDialog getAboutDialog() {
	if (aboutDialog == null) {
	    aboutDialog = new JDialog(getJFrame(), true);
	    aboutDialog.setTitle("About");
	    aboutDialog.setSize(new Dimension(20, 20)); // Generated
	    aboutDialog.setContentPane(getAboutContentPane());
	}
	return aboutDialog;
    }

    /**
         * This method initializes aboutContentPane
         * 
         * @return javax.swing.JPanel
         */
    private JPanel getAboutContentPane() {
	if (aboutContentPane == null) {
	    jLabel = new JLabel();
	    jLabel.setText("LANSim"); // Generated
	    jLabel.setHorizontalAlignment(SwingConstants.CENTER); // Generated
	    jLabel.setBounds(new Rectangle(20, 9, 171, 25)); // Generated
	    aboutContentPane = new JPanel();
	    aboutContentPane.setLayout(null);
	    aboutContentPane.add(jLabel, null); // Generated
	    aboutContentPane.add(getAboutVersionLabel(), null); // Generated
	}
	return aboutContentPane;
    }

    /**
         * This method initializes aboutVersionLabel
         * 
         * @return javax.swing.JLabel
         */
    private JLabel getAboutVersionLabel() {
	if (aboutVersionLabel == null) {
	    aboutVersionLabel = new JLabel();
	    aboutVersionLabel.setText("Version 1.0.8");
	    aboutVersionLabel.setBounds(new Rectangle(20, 29, 171, 48)); // Generated
	    aboutVersionLabel.setHorizontalAlignment(SwingConstants.CENTER);
	}
	return aboutVersionLabel;
    }

    /**
         * This method initializes jMenuItem
         * 
         * @return javax.swing.JMenuItem
         */
    private JMenuItem getCutMenuItem() {
	if (cutMenuItem == null) {
	    cutMenuItem = new JMenuItem();
	    cutMenuItem.setText("Cut");
	    cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.CTRL_MASK, true));
	    cutMenuItem.addActionListener(ApplicationManager.application);
	}
	return cutMenuItem;
    }

    /**
         * This method initializes jMenuItem
         * 
         * @return javax.swing.JMenuItem
         */
    private JMenuItem getCopyMenuItem() {
	if (copyMenuItem == null) {
	    copyMenuItem = new JMenuItem();
	    copyMenuItem.setText("Copy");
	    copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.CTRL_MASK, true));
	    copyMenuItem.addActionListener(ApplicationManager.application);
	}
	return copyMenuItem;
    }

    /**
         * This method initializes jMenuItem
         * 
         * @return javax.swing.JMenuItem
         */
    private JMenuItem getPasteMenuItem() {
	if (pasteMenuItem == null) {
	    pasteMenuItem = new JMenuItem();
	    pasteMenuItem.setText("Paste");
	    pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, Event.CTRL_MASK, true));
	    pasteMenuItem.addActionListener(ApplicationManager.application);
	}
	return pasteMenuItem;
    }

    /**
         * This method initializes jMenuItem
         * 
         * @return javax.swing.JMenuItem
         */
    private JMenuItem getSaveMenuItem() {
	if (saveMenuItem == null) {
	    saveMenuItem = new JMenuItem();
	    saveMenuItem.setText("Save");
	    saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK, true));
	    saveMenuItem.addActionListener(ApplicationManager.application);
	}
	return saveMenuItem;
    }

    private JMenuItem getNewMenuItem() {
	if (newMenuItem == null) {
	    newMenuItem = new JMenuItem();
	    newMenuItem.setText("New");
	    newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK, true));
	    newMenuItem.addActionListener(ApplicationManager.application);
	}
	return newMenuItem;
    }

    private JMenuItem getOpenMenuItem() {
	if (openMenuItem == null) {
	    openMenuItem = new JMenuItem();
	    openMenuItem.setText("Open");
	    openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK, true));
	    openMenuItem.addActionListener(ApplicationManager.application);
	}
	return openMenuItem;
    }

    private JMenuItem getComponentMenuItem() {
	if (componentMenuItem == null) {
	    componentMenuItem = new JMenuItem();
	    componentMenuItem.setText("Component Toolbar");
	    componentMenuItem.addActionListener(ApplicationManager.application);
	}
	return componentMenuItem;
    }

    /**
         * This method initializes TaskToolBar
         * 
         * @return javax.swing.JToolBar
         */
    private JToolBar getTaskToolBar() {
	if (TaskToolBar == null) {
	    TaskToolBar = new JToolBar("Tasks");
	    TaskToolBar.setName("Tasks"); // Generated
	    TaskToolBar.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

	    TaskToolBar.add(getCmdNew());
	    TaskToolBar.add(getCmdOpen());
	    TaskToolBar.add(getCmdSave());
	    TaskToolBar.addSeparator();
	    TaskToolBar.add(getCmdPause());
	    TaskToolBar.add(getCmdRun());

	}
	return TaskToolBar;
    }

    /**
         * This method initializes cmdNew
         * 
         * @return javax.swing.JButton
         */
    private JButton getCmdNew() {
	if (cmdNew == null) {
	    cmdNew = new JButton();
	    // cmdNew.setText("New"); // Generated
	    cmdNew.setIcon(new ImageIcon(Config.iconPath + "new.png"));
	    cmdNew.setToolTipText("New");
	    cmdNew.setSize(40, 25);
	    cmdNew.addActionListener(ApplicationManager.application);
	}
	return cmdNew;
    }

    /**
         * This method initializes cmdSave
         * 
         * @return javax.swing.JButton
         */
    private JButton getCmdSave() {
	if (cmdSave == null) {
	    cmdSave = new JButton();
	    // cmdSave.setText("Save"); // Generated
	    cmdSave.setIcon(new ImageIcon(Config.iconPath + "save.png"));
	    cmdSave.setToolTipText("Save");
	    cmdSave.setSize(40, 25);
	    cmdSave.addActionListener(ApplicationManager.application);
	}
	return cmdSave;
    }

    private JButton getCmdOpen() {
	if (cmdOpen == null) {
	    cmdOpen = new JButton();
	    // cmdOpen.setText("Open"); // Generated
	    cmdOpen.setToolTipText("Open");
	    cmdOpen.setIcon(new ImageIcon(Config.iconPath + "open.png"));
	    cmdOpen.setSize(40, 25);
	    cmdOpen.addActionListener(ApplicationManager.application);
	}
	return cmdOpen;
    }

    /**
         * This method initializes cmdRun
         * 
         * @return javax.swing.JToggleButton
         */
    private JToggleButton getCmdRun() {
	if (cmdRun == null) {
	    cmdRun = new JToggleButton();
	    cmdRun.setIcon(new ImageIcon(Config.iconPath + "Run.png"));
	    cmdRun.setToolTipText("Run");
	    cmdRun.setSize(40, 25);
	    cmdRun.setEnabled(false);
	    cmdRun.addActionListener(ApplicationManager.application);
	}
	return cmdRun;
    }

    /**
         * This method initializes cmdPause
         * 
         * @return javax.swing.JToggleButton
         */
    private JToggleButton getCmdPause() {
	if (cmdPause == null) {
	    cmdPause = new JToggleButton();
	    // cmdPause.setText("Pause"); // Generated
	    cmdPause.setIcon(new ImageIcon(Config.iconPath + "pause.png"));
	    cmdPause.setToolTipText("Pause");
	    cmdPause.setSize(40, 25);
	    cmdPause.setEnabled(false);
	    cmdPause.addActionListener(ApplicationManager.application);
	}
	return cmdPause;
    }

    /**
         * This method initializes ComponentToolBar
         * 
         * @return javax.swing.JToolBar
         */
    private JToolBar getComponentToolBar() {
	if (ComponentToolBar == null && cmdComponents != null) {
	    ComponentToolBar = new JToolBar("Components");
	    ComponentToolBar.setOrientation(JToolBar.VERTICAL); // Generated
	    ComponentToolBar.setName("Components"); // Generated
	    ComponentToolBar.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
	    int ht = 0;
	    for (int i = 0; i < cmdComponents.length; i++) {
		ComponentToolBar.add(getCmdComponents(i));
		ht += 45;
	    }
	    ComponentToolBar.setPreferredSize(new Dimension(50, ht));
	}
	return ComponentToolBar;
    }

    /**
         * This method initializes cmdComponents[]
         * 
         * @return javax.swing.JButton
         */
    private JButton getCmdComponents(int i) {
	if (cmdComponents[i] == null) {
	    JButton newButton = new JButton();
	    // newButton.setText(ComponentsName[i]);
	    newButton.setToolTipText(ComponentsName[i]);
	    Image icon = new ImageIcon("module/" + ComponentsName[i] + "/icon.gif").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
	    newButton.setIcon(new ImageIcon(icon));
	    newButton.setSize(new Dimension(40, 40));
	    newButton.setPreferredSize(new Dimension(40, 40));
	    cmdComponents[i] = newButton;
	}
	if (cmdComponents[i].getActionListeners().length == 0)
	    cmdComponents[i].addActionListener(ApplicationManager.application);
	return cmdComponents[i];
    }

    /**
         * This method initializes StatusBar
         * 
         * @return javax.swing.JPanel
         */
    private JPanel getStatusBar() {
	if (StatusBar == null) {
	    GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
	    gridBagConstraints2.gridx = 1; // Generated
	    gridBagConstraints2.anchor = GridBagConstraints.EAST; // Generated
	    gridBagConstraints2.fill = GridBagConstraints.NONE; // Generated
	    gridBagConstraints2.insets = new Insets(0, 2, 0, 2); // Generated
	    gridBagConstraints2.weightx = 0.0D; // Generated
	    gridBagConstraints2.ipadx = 100; // Generated
	    gridBagConstraints2.gridy = 0; // Generated
	    GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
	    gridBagConstraints1.gridx = 0; // Generated
	    gridBagConstraints1.ipadx = 100; // Generated
	    gridBagConstraints1.ipady = 0; // Generated
	    gridBagConstraints1.anchor = GridBagConstraints.WEST; // Generated
	    gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL; // Generated
	    gridBagConstraints1.weightx = 100.0D; // Generated
	    gridBagConstraints1.weighty = 100.0D; // Generated
	    gridBagConstraints1.insets = new Insets(0, 2, 0, 2); // Generated
	    gridBagConstraints1.gridwidth = 1; // Generated
	    gridBagConstraints1.gridy = 0; // Generated
	    StatusBar = new JPanel();
	    StatusBar.setLayout(new GridBagLayout()); // Generated
	    StatusBar.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT); // Generated
	    StatusBar.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED)); // Generated
	    StatusBar.setPreferredSize(new Dimension(456, 25)); // Generated
	    StatusBar.add(getStatusLabel(), gridBagConstraints1); // Generated
	    StatusBar.add(getProgressBar(), gridBagConstraints2); // Generated
	}
	return StatusBar;
    }

    /**
         * This method initializes StatusLabel
         * 
         * @return javax.swing.JLabel
         */
    private JLabel getStatusLabel() {
	if (StatusLabel == null) {
	    StatusLabel = new JLabel();
	    StatusLabel.setText("Time: 0 us"); // Generated
	    StatusLabel.setPreferredSize(new Dimension(200, 25)); // Generated
	    StatusLabel.setDisplayedMnemonic(KeyEvent.VK_UNDEFINED); // Generated
	    StatusLabel.setHorizontalTextPosition(SwingConstants.LEFT); // Generated
	    StatusLabel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT); // Generated
	}
	return StatusLabel;
    }

    /**
         * This method initializes ProgressBar
         * 
         * @return javax.swing.JProgressBar
         */
    private JProgressBar getProgressBar() {
	if (ProgressBar == null) {
	    ProgressBar = new JProgressBar();
	    ProgressBar.setPreferredSize(new Dimension(100, 25)); // Generated
	    ProgressBar.setMinimum(100); // Generated
	    ProgressBar.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT); // Generated
	}
	return ProgressBar;
    }

    /**
         * This method initializes ClientArea
         * 
         * @return javax.swing.JTabbedPane
         */
    /*
         * private JTabbedPane getClientArea() { if (ClientArea == null) { ClientArea = new JTabbedPane();
         * ClientArea.setTabPlacement(JTabbedPane.BOTTOM); // Generated ClientArea.setPreferredSize(new Dimension(100, 100)); // Generated
         * //ClientArea.setBackground(Color.white); // Generated ClientArea.addMouseListener(ApplicationManager.application);
         *  } return ClientArea; }
         */
    private JScrollPane getClientArea() {
	if (jScp == null) {
	    jScp = new JScrollPane();
	    jScp.setAutoscrolls(true);
	    jScp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    jScp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    jScp.addComponentListener(ApplicationManager.application);
	}
	return jScp;
    }

    /**
         * Called to create a new client space where the contents of another file can be shown or created. This allows for opening and creation of
         * multiple files.
         * 
         */
    public void addNewClientSpace(String title) {
	// ClientArea.addTab(title, new JScrollPane(getClientRegion(title)));
	ManagerUI m = getClientRegion(title);
	jScp.setViewportView(m);
	jScp.revalidate();
	m.Resize(jScp.getVisibleRect().width, jScp.getVisibleRect().height);
    }

    /**
         * Removes the given client space.
         * 
         * @param clientspc
         */
    /*
         * public void removeClientSpace(ManagerUI clientspc){ ClientArea.remove(clientspc); clientSpace.remove(clientspc); }
         */

    /**
         * Removes the given client space.
         * 
         * @param index
         *                The index of the tab position of the concerned client space.
         */
    /*
         * public void removeClientSpace(int index){ ClientArea.remove(index); clientSpace.remove(index); }
         */

    /**
         * Creates a new client Region and also updates clientSpace.
         * 
         * @return
         */
    private ManagerUI getClientRegion(String title) {
	ManagerUI m = new ManagerUI(title, StatusLabel);
	m.setPreferredSize(new Dimension(1000, 1500));
	m.revalidate();
	getClientSpace();
	clientSpace.clear();
	clientSpace.add(m);
	return m;
    }

    /**
         * This Method intiializes clientSpace if is null
         * 
         * @return java.util.ArrayList
         */

    private ArrayList getClientSpace() {
	if (clientSpace == null) {
	    clientSpace = new ArrayList<ManagerUI>();
	    cmdRun.setEnabled(true);
	}
	return clientSpace;
    }

    /**
         * Makes the MainWindow visible
         * 
         */
    public void makeAppVisible() {
	getJFrame().setVisible(true);
    }

}