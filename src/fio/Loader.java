/*
 * Loader.java
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
package fio;

import java.lang.Class;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ClassLoader;
import java.io.FileInputStream;

/**
 * 
 * @author Rohit
 * @deprecated No longer used and will break some modules if used.
 * 
 */
public class Loader extends ClassLoader {
    final int MAX = 100000;

    public Loader() {

    }

    public Class load(String path) {
	FileInputStream ifstream;
	int len;
	Class newClass;
	byte b[] = new byte[MAX];
	try {
	    ifstream = new FileInputStream(path);
	} catch (FileNotFoundException ex) {
	    System.out.println("  FileNotFound");
	    return null;
	}
	try {
	    len = ifstream.read(b);
	    System.out.println(path + " of " + len + " bytes Loaded");
	} catch (IOException IOe) {
	    System.out.println(path + "  IOException");
	    return null;
	}
	try {
	    newClass = defineClass(null, b, 0, len);
	    resolveClass(newClass);
	} catch (Exception e) {
	    System.out.println("Exception");
	    return null;
	}
	return newClass;
    }
}