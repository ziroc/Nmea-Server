package net.avtolik.nmea_server;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;

public class NmeaServer {
	
	static Charset charset = Charset.forName("ASCII");
	
	private PositionProvider positionProvider;
	private int port = 10101;
	private int timeout = 120;


	public NmeaServer(int port) {
		this.port = port;
	}

	public static void main(String[] args) {
		NmeaServer server = new NmeaServer(10101);
		SimplePositionProvider spp = new SimplePositionProvider();
		server.setPositionProvider(spp);
		server.setTimeout(120);
		server.start();
	}


	private ServerSocket serverSocket;

	public void start() {
		try {
			serverSocket = new ServerSocket(port);

			while (true)
				new ClientHandler(serverSocket.accept()).start();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void stop() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public PositionProvider getPos() {
		return positionProvider;
	}

	public void setPositionProvider(PositionProvider pos) {
		this.positionProvider = pos;
	}

	private  class ClientHandler extends Thread {
		private Socket clientSocket;
		OutputStream outputStream = null;

		public ClientHandler(Socket socket) {
			this.clientSocket = socket;
		}

		public void run() {
			System.out.println("client connected");

			try {
				
				
				outputStream = clientSocket.getOutputStream();

				while(true) {
					
					String pos = positionProvider.getPosition();
					System.out.println("Got position: " + pos);
					String nmea;
					
					double speed = positionProvider.getSpeed();
					double headingTrue = positionProvider.getHeadingTrue();
					
					if(speed <0 || headingTrue <0)
						nmea = NmeaUtils.generateGGANmeaSentence(pos);
					else 
						nmea = NmeaUtils.generateRMCNmeaSentence(pos, speed, headingTrue);
					System.out.println("sending: " + nmea);

					outputStream.write( nmea.getBytes(charset));
					outputStream.flush();

					Thread.sleep(timeout * 1000);
				}

			} catch (SocketException se) {
				System.out.println("socket exception");
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();				
			} finally {
				try {
					System.out.println("cleanup");
					outputStream.close();
					clientSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	public int getPort() {
		return port;
	}

	/**
	 * @param port - The port on which the server is listening for clients. Default is 10101
	 */
	public void setPort(int port) {
		this.port = port;
	}

	public int getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout - the period between sending the data to the clients. Default is 120
	 * 
	 */
	public void setTimeout(int seconds) {
		this.timeout = seconds;
	}
}
