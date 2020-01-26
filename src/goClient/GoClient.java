package goClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class GoClient {
	public static void main(String[] s) {

		int port = Integer.parseInt(s[2]);

		InetAddress serverAddress = null;
		try {
			serverAddress = InetAddress.getByName(s[1]);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			Socket socket = new Socket(serverAddress, port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
