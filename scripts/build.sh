#!/bin/bash

if [ -z $3 ]
  then
    echo "Usage: $0 <APP_NAME> <APP_DIR> <OUTPUT_DIR>"
fi
APP_NAME="$1"
BUILDDIR="$2"
REPODIR="$3/$APP_NAME"
APPPACK="$REPODIR.tar.bz2"
LIBDIR="$REPODIR/lib"
CFGDIR="$REPODIR/etc"
MAZEDIR="$REPODIR/maze"
MAZEJAR=$BUILDDIR/build/maze-1.0.jar
MAZECONF=$BUILDDIR/build/maze.conf
PULGACONF=$BUILDDIR/build/pulga.conf
RUNSH=$BUILDDIR/build/run.sh
INITSCRIPT=$BUILDDIR/build/$APP_NAME.sh
RESOURCES="$BUILDDIR/src/main/resources"
PULGA="$RESOURCES/pulga.xml"
LOGBACK="$RESOURCES/logback.xml"
FILESDIR="$BUILDDIR/files"
cd $BUILDDIR
mvn dependency:copy-dependencies
find . -name "*.jar"
if [ -d $REPODIR ]; then rm -rf $REPODIR;fi
mkdir -p $LIBDIR
for i in `find . -name "*.jar"`;do
        cp $i $LIBDIR
done
find $LIBDIR -name "*sources*"|xargs rm -f

if [ -f $PULGA ];then
	mkdir -p $CFGDIR
	cp $PULGA $CFGDIR
	if [ -f $PULGACONF ];then
		. $PULGACONF
	      sed -i s,$ORG_VALUE,$NEW_VALUE,g $CFGDIR/pulga.xml

#	sed -i s,"8081","80",g $CFGDIR/pulga.xml
	fi
fi
if [ -f $LOGBACK ];then
	cp $LOGBACK  $CFGDIR
fi
mkdir -p $MAZEDIR
if [ -f $FILESDIR ];then
	cp -R $FILESDIR/* $CFGDIR
fi

if [ -f $MAZEJAR ];then
	cp $MAZEJAR $MAZEDIR
fi
if [ -f $MAZECONF ];then
	cp $MAZECONF $MAZEDIR
fi
if [ -f $RUNSH ];then
	cp $RUNSH $REPODIR
fi
if [ -f $INITSCRIPT ];then
	cp $INITSCRIPT $REPODIR
fi
cd $REPODIR
tar cvjf $APPPACK *

echo "Output: $APPPACK"
