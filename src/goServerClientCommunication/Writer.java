package goServerClientCommunication;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import goExceptions.ConnectionLostException;

public class Writer {

	protected BufferedWriter out;

	public Writer(OutputStream out) {
		this.out = new BufferedWriter(new OutputStreamWriter(out));
	}

	public void sendMessage(String msg) throws ConnectionLostException {
		if (out != null) {
			try {
				out.write(msg);
				out.newLine();
				out.flush();
			} catch (IOException e) {
				System.out.println(e.getMessage());
				throw new ConnectionLostException("Could not write to server.");
			}
		} else {
			throw new ConnectionLostException("Could not write to server.");
		}
	}
}
