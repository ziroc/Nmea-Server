package net.avtolik.nmea_server;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NmeaUtils {

	private static final String nmea1 = "GPGGA,124112.00,";
	private static final String nmea2 = ",4,6,0,,M,,,,";
	static Charset charset = Charset.forName("ASCII");
	
	private static final String HEADING_TRUE_BEGIN = "$GPHDT";
	private static final String HEADING_TRUE_END = "T";
	
	private static final String RMC_BEGIN = "GPRMC,";
	private static final String RMC_END = ",020.3,E";
	
	private static DecimalFormat df_one = new DecimalFormat("##.##");
	private static DecimalFormat df_two = new DecimalFormat("##.#");
	private static DecimalFormat f1 = new DecimalFormat("#0");
	
	private static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");
	private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("ddMMyy");

	public static void main(String[] args) {
		String nmea = generateRMCNmeaSentence("34.719536, -14.350990", 15.2, 180.5);

		System.out.println(nmea);
	}

	private static String generatePositionString(double lat, double lng) {
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

	public static String generateGGANmeaSentence(String pos) {
		String[] split = pos.split(", ");

		String basic = nmea1 + generatePositionString(Double.parseDouble(split[0]), Double.parseDouble(split[1])) + nmea2;
		return "$" + basic + "*" + CalculateCheckSum(basic) + "\n";
	}
	
	public static String generateRMCNmeaSentence(String pos, double speed, double degrees) {
		String[] split = pos.split(", ");
		LocalDateTime now = LocalDateTime.now(Clock.systemUTC()); 

		String basic = RMC_BEGIN + now.format(timeFormatter) + ",A," + generatePositionString(Double.parseDouble(split[0]), Double.parseDouble(split[1])) + ','+ df_one.format(speed) + ',' 
				+ df_two.format(degrees)+ ',' + now.format(dateFormatter)+ RMC_END;
		return "$" + basic + "*" + CalculateCheckSum(basic) + "\n";
	}
	
	
	public static String generateHeadingSentence(double degrees) {
		return HEADING_TRUE_BEGIN + df_two.format(degrees) + HEADING_TRUE_END;
	}

}
