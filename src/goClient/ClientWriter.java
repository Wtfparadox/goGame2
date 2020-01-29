package goClient;

import java.io.IOException;
import java.io.OutputStream;

import goExceptions.ConnectionLostException;
import goProtocol.GoClientProtocol;
import goProtocol.ProtocolMessages;
import goServerClientCommunication.Writer;

public class ClientWriter extends Writer implements GoClientProtocol {

	public ClientWriter(OutputStream out) throws IOException {
		super(out);
	}

	@Override
	public void doHandshake(int version, String name, String color) throws IOException, ConnectionLostException {
		String messageForServer = ProtocolMessages.HANDSHAKE + ProtocolMessages.DELIMITER + version
				+ ProtocolMessages.DELIMITER + name + ProtocolMessages.DELIMITER + color;
		sendMessage(messageForServer);
	}

	@Override
	public void doMove(String move) throws ConnectionLostException {
		sendMessage(ProtocolMessages.MOVE + ProtocolMessages.DELIMITER + move);
	}

	@Override
	public void quitGame() throws ConnectionLostException {
		sendMessage(Character.toString(ProtocolMessages.QUIT));
	}

}
