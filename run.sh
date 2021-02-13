#!/bin/bash
java -Djava.library.path=./libs/lwjgl2/lwjgl-2.9.3/native/linux -Dfile.encoding=UTF-8 -classpath ./bin:./libs/slick2d/lib/jinput.jar:./libs/slick2d/lib/jogg-0.0.7.jar:./libs/slick2d/lib/jorbis-0.0.15.jar:./libs/slick2d/lib/lwjgl.jar:./libs/slick2d/lib/slick.jar se.gunning.flongout.Main
