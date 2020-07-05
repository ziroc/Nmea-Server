package net.avtolik.nmea_server;

public class SimplePositionGetter implements PositionGetter {

	@Override
	public String get() {
		return "34.566267, -14.456167";
	}

}
