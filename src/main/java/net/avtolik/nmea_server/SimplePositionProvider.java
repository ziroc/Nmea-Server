package net.avtolik.nmea_server;

public class SimplePositionProvider implements PositionProvider {

	@Override
	public String getPosition() {
		return "34.566267, -14.456167";
	}

}
