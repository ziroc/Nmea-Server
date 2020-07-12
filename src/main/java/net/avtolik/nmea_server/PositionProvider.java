package net.avtolik.nmea_server;

public interface PositionProvider {

	public String getPosition();

	public double getSpeed();

	public double getHeadingTrue();
}
