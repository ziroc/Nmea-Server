# Nmea-Server
Basic Java TCP server that creates GPS position NMEA sentence and sends it to the connected clients.  
You can extend the interface `PositionGetter` to supply the server updated GPS positions to send to the clients.
```
  NmeaServer server = new NmeaServer(12345);
  server.setPositionGetter(new SimplePositionGetter());
  server.start();
```
