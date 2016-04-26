The videoServer can be run using the runnable jar file runnableServer.jar 

Use this command from command line to run the videoServer: java -jar runnableServer.jar

The command line options available for the running the server are:
1) -port <value>
     To run the server on a particular port use -port <value>. 
     Example: java -jar runnableServer.jar -port 8008
     The default port number if the "-port" option is not specified is 10010.
2) -timeout
     To set the timeout of the server socket use -timeout <value>. The value is in milliseconds.
     Example: java -jar runnableServer.jar -timeout 5000000
     The default timeout value if the "-timeout" option is not specified is 600000.

The configuration file named "configurationFile.properties" is included in the jar file as well as in a same directoru as the jar file.

The server maintains a log file named "sererLog".The server logs information about the following:
1) The port on which the server is running.
2) The Ip address of the server.
3) The IP address of the client that requests information.
4) The action or event requested by the client.
