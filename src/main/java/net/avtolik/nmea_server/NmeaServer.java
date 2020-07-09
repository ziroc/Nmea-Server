package net.avtolik.nmea_server;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;

public class NmeaServer {
	
	static Charset charset = Charset.forName("ASCII");
	
	private PositionGetter positionGetter;
	private int port = 10101;
	private int seconds = 120;


	public NmeaServer(int port) {
		this.port = port;
	}

	public static void main(String[] args) {
		NmeaServer s = new NmeaServer(10101);
		SailGameService gpg = new SailGameService();
		s.setPositionGetter(gpg);
		s.start();
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

	public PositionGetter getPos() {
		return positionGetter;
	}

	public void setPositionGetter(PositionGetter pos) {
		this.positionGetter = pos;
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
					
					String pos = positionGetter.getPosition();
					System.out.println("Got position: " + pos);
					String nmea = NmeaUtils.generateNmeaSentence(pos);
					System.out.println("sending: " + nmea);

					outputStream.write( nmea.getBytes(charset));
					outputStream.flush();

					Thread.sleep(seconds * 1000);
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

	public int getSeconds() {
		return seconds;
	}

	/**
	 * @param seconds - the period between sending the data to the clients. Default is 120
	 * 
	 */
	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}
}
