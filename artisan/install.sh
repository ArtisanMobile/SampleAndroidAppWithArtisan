#!/bin/sh

###############################################################################
### Environment variables #####################################################
###############################################################################

### These will be passed to the JVM using the Dname=value syntax
### This is the same as calling System.setEnvironment from within the code

loggerConfig=${PWD}/log4j2.xml

###############################################################################
### Script logic ##############################################################
###############################################################################

cd build/jar

### The $@ option passes along all command line arguments to the ArtisanSupport.jar program

java -Dlog4j.configurationFile="${loggerConfig}" -Djava.awt.headless=true -jar ArtisanSupport.jar $@