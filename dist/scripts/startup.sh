#!/bin/bash
echo "checking dependencies..."
if [ ! -d "./lib" ]
    then
        echo "lib folder is missing! find it and put it here!"
	exit 1
fi
echo "all dependencies in place"


echo "starting server"
java -cp "./lib/*"  -jar "./DataBridgeServer.jar"

echo "server closed"
echo "you may now close this terminal"
exit 0
