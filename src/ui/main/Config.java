/*
 * Config.java
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

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * @author Nirupam
 * 
 */
public final class Config {
	final static public String moduleDirPath = Config.class.getResource("/module").getPath();
    final static public String iconPath = moduleDirPath + "/../../icons/";

    final static public void setLookNFeel() {
	try {
	    UIManager.setLookAndFeel("com.birosoft.liquid.LiquidLookAndFeel");
	    // UIManager.setLookAndFeel("net.sourceforge.napkinlaf.NapkinLookAndFeel");
	} catch (ClassNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (InstantiationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IllegalAccessException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (UnsupportedLookAndFeelException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	com.birosoft.liquid.LiquidLookAndFeel.setLiquidDecorations(true, "mac");

	// SwingUtilities.updateComponentTreeUI(mainWin.jFrame);
    }
}
