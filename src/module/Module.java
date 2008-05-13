/*
 * Module.java
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

import framework.Port;

/**
 * 
 * @author Rohit
 * @author Nirupam
 * 
 */
public interface Module {
    public static final int ERROR_ALREADY_MAX_PORTS = 0;

    /**
         * Adds a new port (connection)
         * 
         * @return The error code if any. e.g. ERROR_ALREADY_MAX_PORTS is returned if no more ports of type wireType can be handeled by this module.
         */
    public int addPort(Port port, int wireType);

    /**
         * If the implementing class doesn't want to extend or create an object of ModuleUI then return null.
         * 
         * @return
         */
    public ModuleUI getModuleUI();

    public void setModuleUI(ModuleUI m);

    /**
         * Returns the name of the implementing module. This name is displayed by the UI.
         * 
         * @return
         */
    public String getName();

    /**
         * Returns the name of the icon file that is displayed in the client area.
         * 
         * @return
         */
    public String getIconPath();

    public void setSno(int n);

    public int getSno();

    public boolean step(double TimeStep);// boolean Success or failure

    public boolean reset();// passed when the simulation is reset

    public Port[] getPorts();// Gives the list of already initialized ports

    public boolean isNewPacket();// Tells whether new packet is released by the module. If released, it sends true and resets the flag,else it

    // returns false.

    public int getNoOfPorts();// return the no of ports in the module
}