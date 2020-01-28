package goServer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

import goController.GameController;
import goExceptions.EndOfGameException;
import goExceptions.ExitProgram;
import goExceptions.ServerUnavailableException;
import goGame.Player;
import goGame.StoneColor;
import goProtocol.GoServerProtocol;
import goProtocol.ProtocolMessages;

public class GoClientHandler implements GoServerProtocol, Runnable {

	private InputHandlerServer ihs;
	private Socket sock;

	private BufferedWriter out;

	private Player player;

	public GoClientHandler(Socket sockArg, GameController gc) throws IOException {
		ihs = new InputHandlerServer(gc, this);
		sock = sockArg;
		out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
	}

	@Override
	public void run() {
		boolean isRunning = true;
		while (isRunning) {
			initializeReaders();
			try {
				ihs.processInput();
			} catch (ExitProgram | EndOfGameException | ServerUnavailableException e) {
				isRunning = false;
				e.printStackTrace();
			}
		}

	}

	private void initializeReaders() {
		try {
			ihs.startReader(sock.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendToClient(String msg) throws IOException {
		out.write(msg);
		out.newLine();
		out.flush();
	}

	@Override
	public String respondToHandshake(String finalVersion) {
		return ProtocolMessages.HANDSHAKE + ProtocolMessages.DELIMITER + finalVersion;
	}

	@Override
	public String startGame(StoneColor color, String board) {
		return ProtocolMessages.GAME + ProtocolMessages.DELIMITER + board + ProtocolMessages.DELIMITER + color;
	}

	@Override
	public String giveTurnToMove(String board, int lastMove) {
		return ProtocolMessages.TURN + ProtocolMessages.DELIMITER + board + ProtocolMessages.DELIMITER + lastMove;
	}

	@Override
	public String validMove(String board) {
		return ProtocolMessages.RESULT + ProtocolMessages.DELIMITER + ProtocolMessages.VALID
				+ ProtocolMessages.DELIMITER + board;
	}

	@Override
	public String invalidMove(String msg) {
		return ProtocolMessages.RESULT + ProtocolMessages.DELIMITER + ProtocolMessages.INVALID
				+ ProtocolMessages.DELIMITER + msg;
	}

	@Override
	public String endGame(char reason, char winner, double scoreBlack, double scoreWhite) {
		return ProtocolMessages.END + ProtocolMessages.DELIMITER + reason + ProtocolMessages.DELIMITER + winner
				+ ProtocolMessages.DELIMITER + scoreBlack + ProtocolMessages.DELIMITER + scoreWhite;
	}

	public static void main(String[] s) {
		GoServer server = new GoServer(new GoServerTUI());
		new Thread(server).start();
	}
}
