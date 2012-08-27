Current Project Status
----------------------

We worked on this in 2008, for our college project. It would not be wrong to say, as of now it is dormant and abandoned. This project has been migrated from sf.net to here.

Introduction
------------

LANSim is a CNet-like network simulator. It aims to be very easy to use GUI based network simulator. We started this (and is still is) our college semester project.

This is meant to be user friendly as well as feature rich. This field lacks software with good user interface except for the very popular CNet (which is still not available for Windows platform). The commercial softwares in this field are too complicated. This aimed to go along Multisim(R) and Blender(R) way. As user-friendly as Multisim and as functional as [Blender][bl].

[bl]: http://www.blender.org/

Compiling the code
------------------

1. `cd` to `lansim` folder.
1. Run the following:-

        ant

**- Or -**

**This method is not recommended as it won't copy the resource files like icon images etc.**

The following is for Unix/Linux or Mac OSX platforms.

1. `cd` to `lansim` folder.
1. Then run the following:-

        find src/ -iname "*.java"| xargs  javac -cp lib/liquidlnf.jar:lib/napkinlaf-swingset2.jar:lib/napkinlaf.jar -d bin

**- Or -**

You can [Import this project](http://help.eclipse.org/helios/index.jsp?topic=%2Forg.eclipse.platform.doc.user%2Ftasks%2Ftasks-importproject.htm) into [Eclipse](http://www.eclipse.org/downloads/), and build from there. This is applicable for Windows too.

Running this project
--------------------

**Notice for Windows people:** There are **no EXE** files. This is a Java project and all it needs is that [JRE](http://www.oracle.com/technetwork/java/javase/downloads/index.html) is installed.

1. Open console ([command prompt](http://en.wikipedia.org/wiki/Command_Prompt) for Windows).
1. `cd` to `bin` directory.
1. There type:-

        java -cp .:../lib/liquidlnf.jar ui.main.ApplicationManager

    If you are on Windows then replace `:` by `;`.

More Details and Documentation
------------------------------

Goto to project's homepage - [http://lansim.sourceforge.net/](http://lansim.sourceforge.net/). Also do checkout `LANSim.doc` and `LANSim.ppt` in `doc` folder.
