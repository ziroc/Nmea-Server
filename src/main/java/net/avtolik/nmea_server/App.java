package net.avtolik.nmea_server;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        System.out.println( "Starting Nmea server" );
        NmeaServer server = new NmeaServer(12345);
        server.setPositionProvider(new SimplePositionProvider());
        server.start();
        
    }
}
