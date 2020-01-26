package goClient;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

import goExceptions.ServerUnavailableException;
import goProtocol.GoClientProtocol;
import goProtocol.ProtocolMessages;

public class ClientWriter implements GoClientProtocol {

	private BufferedWriter out;

	public ClientWriter(Socket sock) throws IOException {
		out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
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
	public void doHandshake(String version, String name, char color) throws ServerUnavailableException {
		sendMessage(ProtocolMessages.HANDSHAKE + ProtocolMessages.DELIMITER + version + ProtocolMessages.DELIMITER
				+ name + ProtocolMessages.DELIMITER + color);
	}

	@Override
	public String doMove(int move) {
		return ProtocolMessages.MOVE + ProtocolMessages.DELIMITER + move;
	}

	@Override
	public void quitGame() throws ServerUnavailableException {
		sendMessage(Character.toString(ProtocolMessages.QUIT));
	}

}
