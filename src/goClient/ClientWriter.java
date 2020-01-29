package goClient;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import goExceptions.ServerUnavailableException;
import goProtocol.GoClientProtocol;
import goProtocol.ProtocolMessages;

public class ClientWriter implements GoClientProtocol {

	private BufferedWriter out;

	public ClientWriter(OutputStream out) throws IOException {
		this.out = new BufferedWriter(new OutputStreamWriter(out));
	}

	public void sendMessage(String msg) throws ServerUnavailableException {
		if (out != null) {
			try {
				out.write(msg);
				out.newLine();
				out.flush();
			} catch (IOException e) {
				System.out.println(e.getMessage());
				throw new ServerUnavailableException("Could not write to server.");
			}
		} else {
			throw new ServerUnavailableException("Could not write to server.");
		}
	}

	@Override
	public void doHandshake(int version, String name, String color) throws ServerUnavailableException, IOException {
		String messageForServer = ProtocolMessages.HANDSHAKE + ProtocolMessages.DELIMITER + version
				+ ProtocolMessages.DELIMITER + name + ProtocolMessages.DELIMITER + color;
		sendMessage(messageForServer);
	}

	@Override
	public void doMove(String move) throws ServerUnavailableException {
		sendMessage(ProtocolMessages.MOVE + ProtocolMessages.DELIMITER + move);
	}

	@Override
	public void quitGame() throws ServerUnavailableException {
		sendMessage(Character.toString(ProtocolMessages.QUIT));
	}

}
