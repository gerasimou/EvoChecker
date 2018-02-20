#! /bin/sh

#  launch.sh
#
#  Created by Simos Gerasimou on 04/09/2017.

WD=`pwd`


#create data dir if does not exist
if [ -d data ]; then
  rm -rf data/*
else
  mkdir data
fi


#For launching an instance only
PRISM_DIR=lib/prism-tacas17

export DYLD_LIBRARY_PATH="$PRISM_DIR":$DYLD_LIBRARY_PATH

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


JAR=""

#for file in $WD/*; do
#     case $(file --mime-encoding -b "$file") in
#        binary)
#            JAR=$file
#            break;;           
#     esac
#done
JAR=EvoChecker-1.0.1.jar

vmArgs="-Xmx3g -XX:ParallelGCThreads=1"
java $vmArgs -jar $JAR

