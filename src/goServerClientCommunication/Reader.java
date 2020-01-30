package goServerClientCommunication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Queue;

public abstract class Reader implements Runnable {

	protected Queue<String> queue;
	protected BufferedReader in;

	public Reader(InputStream in, Queue<String> queue) {
		this.in = new BufferedReader(new InputStreamReader(in));
		this.queue = queue;
	}

	public abstract String readMessage() throws IOException;

	public void close() {
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
