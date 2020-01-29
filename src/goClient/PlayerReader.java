package goClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;

import goServerClientCommunication.Reader;

public class PlayerReader extends Reader {

	public PlayerReader(InputStream in, Queue<String> queue) {
		super(in, queue);
	}

	@Override
	public void run() {
		while (true) {
			queue.add(readMessage());
		}
	}

	public String readMessage() {
		String incomingMessage = "";
		try {
			incomingMessage = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return incomingMessage;
	}

	public void readAndAddMessage() {
		queue.add(readMessage());
	}

	public int readInteger() {
		String incomingMessage = "";
		try {
			incomingMessage = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Integer.parseInt(incomingMessage);
	}

}
