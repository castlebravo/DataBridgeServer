@echo off

echo checking dependencies...
IF EXIST "./lib" (

    echo lib in place
    echo starting server
    echo.
    java -cp "./lib/*"  -jar "./DataBridgeServer.jar"
    echo.
    echo server closed
) ELSE (
    echo lib folder is missing! find it and put it here!"
)
echo.
PAUSE