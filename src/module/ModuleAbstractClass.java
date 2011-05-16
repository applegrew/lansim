/*
 * ModuleAbstractClass.java
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

import ui.main.Config;
import framework.Port;

/**
 * 
 * @author Nirupam
 * 
 */
public class ModuleAbstractClass implements Module {

    public int addPort(Port port, int wireType) {
	// TODO Auto-generated method stub
	return 0;
    }

    public String getIconPath() {
	return  Config.moduleDirPath + "/" + getName() + "/icon.gif";
    }

    public ModuleUI getModuleUI() {
	// TODO Auto-generated method stub
	return null;
    }

    public String getName() {
	// TODO Auto-generated method stub
	return null;
    }

    public int getNoOfPorts() {
	// TODO Auto-generated method stub
	return 0;
    }

    public Port[] getPorts() {
	// TODO Auto-generated method stub
	return null;
    }

    public int getSno() {
	// TODO Auto-generated method stub
	return 0;
    }

    public boolean isNewPacket() {
	// TODO Auto-generated method stub
	return false;
    }

    public boolean reset() {
	// TODO Auto-generated method stub
	return false;
    }

    public void setModuleUI(ModuleUI m) {
    // TODO Auto-generated method stub

    }

    public void setSno(int n) {
    // TODO Auto-generated method stub

    }

    public boolean step(double TimeStep) {
	// TODO Auto-generated method stub
	return false;
    }

}
