## Client-Server Socket chat

### About:
JavaFX apllication with two modules: client and server, which communicate using Sockets over TCP/IP network.<br>
TCP/IP is connection-oriented, meaning an exclusive connection must first be established between the Client and Server for communication to take place. <br>
There is ServerSocket on Server side that waits for connection request from Client. <br>
And Socket on Client Side that sends connection request.<br>
When connection is established, both sides can communicate over specified port in ```application.properties``` file.

### How to run?
1. Clone/extract from archive this repo on your PC.
2. Change in both projects ```src/main/resources/application.properties``` files with your custom data (if you wish).<br>
<b>Hint 1:</b> To run application correctly don't change ```client.host``` property in ```src/main/resources/application.properties```<br>
<b>Hint 2:</b> Property ```server.port``` in server app and ```client.port``` in client app must be the same.
3. Run Server firstly and Client then.
   