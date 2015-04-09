# chatplatform
Websocket-based Chat Server using Jetty 

The chat server uses Jetty library to handle websocket connections. Jetty is an implementation for JSR 356 standard for websocket API.  

----------------------------------------------------------------------------------------
To run the server, you need to have Java 8 or recompile code with Java7.

1- To start the start in default port 9001
./chatserver.sh

To start the server in different port
./chatserver.sh -port <port>

There are three folders:
src : for java source
lib:  for open-source libs
dist: for executable code.  You can compile the code and generate chatserver.jar inside this folder

