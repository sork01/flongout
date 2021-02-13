#!/bin/bash
LD_LIBRARY_PATH=libs/lwjgl2/lwjgl-2.9.3/native/linux/ javac -sourcepath ./src -d bin -classpath libs/slick2d/lib/slick.jar:libs/slick2d/lib/lwjgl.jar:libs/slick2d/lib/jorbis-0.0.15.jar:libs/slick2d/lib/jogg-0.0.7.jar src/se/gunning/flongout/Main.java
