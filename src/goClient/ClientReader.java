package goClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;

public class ClientReader extends Reader {

	public ClientReader(InputStream in, Queue<String> queue) {
		super(in, queue);
	}

	@Override
	public void run() {
		while (true) {
			queue.add(readMessage());
		}
	}

	@Override
	public String readMessage() {
		String incomingMessage = "";
		try {
			incomingMessage = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return incomingMessage;
	}

}
