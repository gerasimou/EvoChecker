#!/bin/bash

#  launch.sh
#
#  Created by Simos Gerasimou 
#  Copyright (c) SPG. All rights reserved.

PRISM_JAVA=`/usr/libexec/java_home`"/bin/java" 
export DYLD_LIBRARY_PATH=`pwd`"/prism-tacas17"
echo $DYLD_LIBRARY_PATH
$PRISM_JAVA -jar PrismTacas17.jar 8880 1
