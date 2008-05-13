/*
 * SnWwAButon.java
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
import java.awt.Image;

import javax.swing.ImageIcon;

/**
 * 
 * @author Nirupam
 * 
 */
public class SnWwAButton extends javax.swing.JButton {

    private static final long serialVersionUID = 8401169865868286162L;

    public SnWwAButton() {
	//buffer.Startup.button = this;
	setToolTipText("SW Node");
	//this.setText("SW Node");
	this.setIcon(new ImageIcon(new ImageIcon("module/SnWwA/terminal.jpg").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
	this.setSize(new Dimension(40, 40));
	this.setPreferredSize(new Dimension(40, 40));
    }

}