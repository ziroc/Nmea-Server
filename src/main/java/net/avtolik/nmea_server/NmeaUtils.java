package net.avtolik.nmea_server;
import java.nio.charset.Charset;
import java.text.DecimalFormat;

public class NmeaUtils {

	private static final String nmea1 = "GPGGA,124112.00,";
	private static final String nmea2 = ",1,0,0,,M,,,,";
	static Charset charset = Charset.forName("ASCII");

	public static void main(String[] args) {
		String nmea = generatePositionString(34.719536, -14.350990);

		System.out.println(nmea);
	}

	private static String generatePositionString(double lat, double lng)	{
		DecimalFormat f1 = new DecimalFormat("#0");
		DecimalFormat f2 = new DecimalFormat("#00.00000");

		String nmea = "";
		double latAbs = Math.abs(lat);
		double latDegrees = Math.floor(latAbs);
		double latMinutes = (latAbs - latDegrees) * 60;
		String latChar = lat > 0 ? "N" : "S";

		double lngAbs = Math.abs(lng);
		double lngDegrees = Math.floor(lngAbs);
		double lngMinutes = (lngAbs - lngDegrees) * 60;
		String lngChar = lng > 0 ? "E" : "W";

		nmea +=  f1.format(latDegrees) + f2.format(latMinutes) + "," + latChar + ",";
		nmea += f1.format(lngDegrees) + f2.format(lngMinutes) + "," + lngChar;


		return nmea;
	}

	private static String CalculateCheckSum(String sentence) {
		int checksum = 0;
		for (int i = 0; i < sentence.length(); i++) {
			checksum = checksum ^ sentence.charAt(i);
		}
		String hex = Integer.toHexString(checksum);
		if (hex.length() == 1)
			hex = "0" + hex;
		return hex.toUpperCase();
	}

	public static String generateNmeaSentence(String pos) {
		String[] split = pos.split(", ");

		String basic = nmea1 + generatePositionString(Double.parseDouble(split[0]), Double.parseDouble(split[1])) + nmea2;
		return "$" + basic + "*" + CalculateCheckSum(basic) + "\n";
	}

}
