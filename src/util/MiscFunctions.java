/*
 * MiscFunctions.java
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA. 
 */
package util;

/**
 * Created on 13-May-08
 *
 * @author Nirupam
 * 
 */
public class MiscFunctions {
    public static String convertPath2className(String path) {
	//Converting Pathname to classname
	String className = path.replaceAll("/", "."); //Replacing all / with .
	className = className.substring(0, className.lastIndexOf('.')); //Truncating the .class extension.
	return className;
    }
}
