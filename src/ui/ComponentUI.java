/*
 * ComponentUI.java
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

import java.awt.Point;

/**
 * @author Nirupam
 * 
 */
public abstract class ComponentUI {
    protected boolean selected = false;

    /**
         * Returns true if the concerned component (wire or module) as been clicked.
         * 
         * @return
         */
    abstract public boolean isClicked(Point pt);

    /**
         * Sets or resets selected flag.
         * 
         * @param flag
         */
    public void setSelected(boolean flag) {
	selected = flag;
    }

    public boolean isSelected() {
	return selected;
    }
}