# Nmea-Server
Basic Java TCP server that creates GPS position NMEA sentence and sends it to the connected clients.  
You can extend the interface `PositionProvider` to supply the server updated GPS positions (also heading and speed) to send to the clients.
```
  NmeaServer server = new NmeaServer(12345);
  server.setPositionProvider(new SimplePositionProvider());
  server.setTimeout(120); // not mandatory
  server.start();
```
