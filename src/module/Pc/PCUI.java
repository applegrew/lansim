/*
 * PCUI.java
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
package module.Pc;

import framework.Packet;
import module.DataUI;
import module.Module;
import module.ModuleUI;

/**
 * @author Nirupam
 * 
 */
public class PCUI extends ModuleUI {
    public PCUI(Module m) {
	super(m);
    }

    protected DataUI getNewDataUI(Packet p) {
	DataUI dui = super.getNewDataUI(p);
	dui.setDisplayString(p.getFromId() + "->" + p.getToId());
	return dui;
    }
}
