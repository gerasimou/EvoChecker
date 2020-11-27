#! /bin/sh

#  launch.sh
#
WD=`pwd`
JAR=EvoChecker-1.1.0.jar


#create data dir if does not exist
if [ -d data ]; then
  rm -rf data/*
else
  mkdir data
fi


#For launching an instance only
PRISM_DIR=libs/runtime


#Get machine type
unameOut="$(uname -s)"
case "${unameOut}" in
    Linux*)     machine=Linux
    				export LD_LIBRARY_PATH="$PRISM_DIR":$LD_LIBRARY_PATH
    				;;
    Darwin*)    machine=Mac
    				export DYLD_LIBRARY_PATH="$PRISM_DIR":$DYLD_LIBRARY_PATH
    				;;
    CYGWIN*)    machine=Cygwin;;
    MINGW*)     machine=MinGw;;
    *)          machine="UNKNOWN:${unameOut}"
esac


JAVA_EXEC=""
# Command to launch Java
if [ "$JAVA_EXEC" = "" ]; then
	if [ -x /usr/libexec/java_home ]; then
		JAVA_EXEC=`/usr/libexec/java_home`"/bin/java"
	else
		JAVA_EXEC=java
	fi
fi



vmArgs="-Xmx3g -XX:ParallelGCThreads=1"
$JAVA_EXEC $vmArgs -jar $JAR

