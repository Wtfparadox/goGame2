package goServer;

import java.io.OutputStream;

import goExceptions.ConnectionLostException;
import goProtocol.GoServerProtocol;
import goProtocol.ProtocolMessages;
import goServerClientCommunication.Writer;

public class ServerWriter extends Writer implements GoServerProtocol {

	public ServerWriter(OutputStream out) {
		super(out);
	}

	@Override
	public void respondToHandshake(String finalVersion) throws ConnectionLostException {
		sendMessage(ProtocolMessages.HANDSHAKE + ProtocolMessages.DELIMITER + finalVersion);
	}

	@Override
	public void startGame(char color, String board) throws ConnectionLostException {
		sendMessage(ProtocolMessages.GAME + ProtocolMessages.DELIMITER + board + ProtocolMessages.DELIMITER + color);
	}

	@Override
	public void giveTurnToMove(String board, String lastMove) throws ConnectionLostException {
		sendMessage(ProtocolMessages.TURN + ProtocolMessages.DELIMITER + board + ProtocolMessages.DELIMITER + lastMove);
	}

	@Override
	public void validMove(String board) throws ConnectionLostException {
		sendMessage(ProtocolMessages.RESULT + ProtocolMessages.DELIMITER + ProtocolMessages.VALID
				+ ProtocolMessages.DELIMITER + board);
	}

	@Override
	public void invalidMove(String msg) throws ConnectionLostException {
		sendMessage(ProtocolMessages.RESULT + ProtocolMessages.DELIMITER + ProtocolMessages.INVALID
				+ ProtocolMessages.DELIMITER + msg);
	}

	@Override
	public void endGame(char reason, char winner, double scoreBlack, double scoreWhite) throws ConnectionLostException {
		sendMessage(ProtocolMessages.END + ProtocolMessages.DELIMITER + reason + ProtocolMessages.DELIMITER + winner
				+ ProtocolMessages.DELIMITER + scoreBlack + ProtocolMessages.DELIMITER + scoreWhite);
	}

}
