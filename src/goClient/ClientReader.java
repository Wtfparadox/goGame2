package goClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientReader implements Runnable {

	private BufferedReader in;

	public ClientReader(Socket sock) throws IOException {
		in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
	}

	@Override
	public void run() {
		while (true) {
			String serverMessage = "";
			try {
				serverMessage = in.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(serverMessage);
		}
	}

}
