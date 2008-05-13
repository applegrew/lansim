/*
 * Manager.java
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
package framework;

import java.util.ArrayList;
import java.lang.String;
import module.Module;

/**
 * 
 * @author Rohit
 * 
 */
public class Manager {
    private ArrayList<Wire>   wires   = null;
    private ArrayList<Module> modules = null;
    private String	    name;
    public Clock	      clock;

    public Manager(String title) {
	this.name = title;
	clock = new Clock();
	wires = new ArrayList<Wire>();
	modules = new ArrayList<Module>();
    }

    public String getName() {
	return name;
    }

    /**
         * To be used only by ui.ManagerUI and framework.FileManger.
         * 
         * @return
         */
    public ArrayList<Wire> getWires() {
	return wires;
    }

    /**
         * To be used only by ui.ManagerUI and framework.FileManger.
         * 
         * @return
         */
    public ArrayList<Module> getModules() {
	return modules;
    }

    public void addModule(Module m) {
	modules.add(m);
    }

    public void addWire(Wire w) {
	wires.add(w);
    }

    /*
         * public Port getPort(Module mod){ return new Port("test",mod); }
         */

    /**
         * Initiates the simulation. It actually resets the variables and readies Manager for simulation.
         * 
         */
    public void initSimulation() {
	clock.rstClock();
	for (int i = 0; i < modules.size(); i++)
	    modules.get(i).reset();
	clock.setTimeStep(1);
    }

    /**
         * Simulates one step and returns control to ManagerUI
         */
    public void stepSimulation() {
	// clock.incrTime();
	// ALL BACKEND SIMUATION COLTROL CODE GOES HERE
	for (int i = 0; i < modules.size(); i++) {
	    (modules.get(i)).step(clock.getTime());
	}
	for (int i = 0; i < wires.size(); i++) {
	    (wires.get(i)).step(clock.getTime());
	}
    }

    public void endSimulation() {
	for (int i = 0; i < modules.size(); i++) {
	    modules.get(i).reset();
	}
	for (int i = 0; i < wires.size(); i++) {
	    wires.get(i).reset();
	}
    }
}