/*
 * Clock.java
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

/**
 * Clock.java The clock which is actually the timekeeper for the virtual and discrete timesteps of the software. Created on December 7, 2006,
 * 
 * @author Rohit
 * @author Nirupam
 */
public class Clock {
    private long	    time;
    private long	    lasttime;
    final private long      TimeRatio = 60; // Actual time in millisec taken for each simulated step.
    volatile private double TimeStep  = 1; // It allows to slow and fast the clock. Setting i to 0.5 will half the animation speed and setting it
                                                // to 2

    // will double the animation speed.
    Clock() {
	this.time = 0;
	this.TimeStep = 1;
    }

    Clock(double TimeStep) {
	time = 0;
	if (TimeStep != 0)
	    this.TimeStep = TimeStep;
	else
	    this.TimeStep = 1;
	lasttime = 0;
    }

    public void rstClock() {
	time = 0;
	lasttime = 0;
    }

    public double getTime() {
	return time;
    }

    public double getRealTime() {
	return time * TimeRatio;
    }

    public void setTimeStep(double newTimeStep) {
	if (newTimeStep != 0)
	    this.TimeStep = newTimeStep;
	else
	    this.TimeStep = 1;
    }

    /**
         * If ignoreTick is true then time is not incrmented even if tick return true.
         * 
         * @param ignoreTick
         * @return 
         */
    public boolean tick(boolean ignoreTick) {
	long currtime = System.currentTimeMillis();
	if (lasttime == 0) {
	    lasttime = currtime;
	    return true;
	}
	double millisPerStep = (TimeRatio / TimeStep);
	double diff = (currtime - lasttime) - millisPerStep;
	if (diff >= 0) {
	    lasttime = currtime;
	    if (!ignoreTick)
		time++;
	    return true;
	} else
	    return false;
    }
}