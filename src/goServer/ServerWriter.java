package goServer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import goExceptions.ClientUnavailableException;
import goProtocol.GoServerProtocol;
import goProtocol.ProtocolMessages;

public class ServerWriter implements GoServerProtocol {

	private BufferedWriter out;

	public ServerWriter(OutputStream out) {
		this.out = new BufferedWriter(new OutputStreamWriter(out));
	}

	public void sendMessage(String msg) throws ClientUnavailableException {
		if (out != null) {
			try {
				out.write(msg);
				out.newLine();
				out.flush();
			} catch (IOException e) {
				System.out.println(e.getMessage());
				throw new ClientUnavailableException("Could not write to client.");
			}
		} else {
			throw new ClientUnavailableException("Could not write to client.");
		}
	}

	@Override
	public void respondToHandshake(String finalVersion) throws ClientUnavailableException {
		sendMessage(ProtocolMessages.HANDSHAKE + ProtocolMessages.DELIMITER + finalVersion);
	}

	@Override
	public void startGame(char color, String board) throws ClientUnavailableException {
		sendMessage(ProtocolMessages.GAME + ProtocolMessages.DELIMITER + board + ProtocolMessages.DELIMITER + color);
	}

	@Override
	public void giveTurnToMove(String board, String lastMove) throws ClientUnavailableException {
		sendMessage(ProtocolMessages.TURN + ProtocolMessages.DELIMITER + board + ProtocolMessages.DELIMITER + lastMove);
	}

	@Override
	public void validMove(String board) throws ClientUnavailableException {
		sendMessage(ProtocolMessages.RESULT + ProtocolMessages.DELIMITER + ProtocolMessages.VALID
				+ ProtocolMessages.DELIMITER + board);
	}

	@Override
	public void invalidMove(String msg) throws ClientUnavailableException {
		sendMessage(ProtocolMessages.RESULT + ProtocolMessages.DELIMITER + ProtocolMessages.INVALID
				+ ProtocolMessages.DELIMITER + msg);
	}

	@Override
	public void endGame(char reason, char winner, double scoreBlack, double scoreWhite)
			throws ClientUnavailableException {
		sendMessage(ProtocolMessages.END + ProtocolMessages.DELIMITER + reason + ProtocolMessages.DELIMITER + winner
				+ ProtocolMessages.DELIMITER + scoreBlack + ProtocolMessages.DELIMITER + scoreWhite);
	}

}
