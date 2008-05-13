/*
 * PortData.java
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
package module.Router;

import java.util.ArrayList;

/**
 * 
 * @author Rohit
 * 
 */
public class PortData {
    ArrayList<RoutElement> compData;
    static final int       MAXHEALTH = 50;

    public PortData() {
	compData = new ArrayList<RoutElement>();
    }

    public void updateCompId(long id) {
	for (int i = 0; i < compData.size(); i++) {
	    if (compData.get(i).compID == id) {
		compData.get(i).health = MAXHEALTH;
		return;
	    }
	}
	compData.add(new RoutElement(id, MAXHEALTH));
    }

    public void removeExpiredId() {
	for (int i = 0; i < compData.size(); i++) {
	    compData.get(i).health--;
	    if (compData.get(i).health <= 0) {
		compData.remove(i);
	    }
	}
    }

    public void removeAllCompInfo() {
	compData.clear();
    }

    public boolean isIdPresent(long id) {
	for (int i = 0; i < compData.size(); i++)
	    if (compData.get(i).compID == id)
		return true;
	return false;
    }

    public String getAllCompInfo() {
	String info = new String();
	for (int i = 0; i < compData.size(); i++) {
	    info += " (" + compData.get(i).compID + "," + compData.get(i).health + ")";
	}
	return info;
    }
}