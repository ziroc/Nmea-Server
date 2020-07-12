package net.avtolik.nmea_server;

public class SimplePositionProvider implements PositionProvider {

	@Override
	public String getPosition() {
		return "34.566267, -14.456167";
	}

	@Override
	public double getSpeed() {
		return -1;
	}

	@Override
	public double getHeadingTrue() {
		return -1;
	}

}
