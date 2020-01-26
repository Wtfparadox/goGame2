package goClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientReader implements Runnable {

	private BufferedReader in;
	private LinkedBlockingQueue<String> queue;

	public ClientReader(InputStream input, LinkedBlockingQueue<String> clq) throws IOException {
		in = new BufferedReader(new InputStreamReader(input));
		queue = clq;
	}

	@Override
	public void run() {
		while (true) {
			String incomingMessage = "";
			try {
				incomingMessage = in.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			queue.add(incomingMessage);
		}
	}

}
