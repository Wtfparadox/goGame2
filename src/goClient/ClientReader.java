package goClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;

import goServerClientCommunication.Reader;

public class ClientReader extends Reader {

	public ClientReader(InputStream in, Queue<String> queue) {
		super(in, queue);
	}

	@Override
	public void run() {
		String msg;
		try {
			msg = readMessage();
			while (msg != null) {
				queue.add(msg);
				msg = readMessage();
			}
		} catch (IOException e) {
			System.out.println("Reader closed");
		}
		this.close();
	}

	@Override
	public String readMessage() throws IOException {
		return in.readLine();
	}

}
