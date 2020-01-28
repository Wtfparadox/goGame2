package goClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.LinkedBlockingQueue;

import goExceptions.EndOfGameException;
import goExceptions.ExitProgram;
import goExceptions.ServerUnavailableException;
import goProtocol.ProtocolMessages;

public class InputHandlerClient {
	private GoClientTUI tui;
	private LinkedBlockingQueue<String> queue;
	private ClientGame game;
	private ClientWriter writer;

	public InputHandlerClient(GoClientTUI tuiArg) {
		queue = new LinkedBlockingQueue<>();
		tui = tuiArg;
	}

	private synchronized void handleInput(String input)
			throws ExitProgram, EndOfGameException, ServerUnavailableException {
		String[] inputArguments = input.split(ProtocolMessages.DELIMITER);
		if (inputArguments.length > 0) {
			switch (input.charAt(0)) {
			case ProtocolMessages.QUIT:
				writer.quitGame();
				throw new ExitProgram("User has quit.");
			case ProtocolMessages.GAME:
				tui.showMessage("game start");
				char color = inputArguments[2].charAt(0);
				game = new ClientGame(color, this);
				break;
			case ProtocolMessages.TURN:
				tui.showMessage("Its your turn!");
				handleTurn(inputArguments[2]);
				break;
			case ProtocolMessages.RESULT:
				tui.showMessage("game result");
				break;
			case ProtocolMessages.END:
				String reason = "";
				throw new EndOfGameException("The game has ended because " + reason);
			}
		} else {
			tui.showMessage("Message not defined in protocol, command unknown.");
		}
	}

	public void handleTurn(String input) throws ServerUnavailableException {
		if (!input.contentEquals(Character.toString(ProtocolMessages.PASS)) && input != null) {
			int otherPlayerMove = Integer.parseInt(input);
			game.processOpponentsMove(otherPlayerMove);
		}
		int ownMove = game.processOwnMove();
		writer.doMove(ownMove);
	}

	public void startReader(InputStream in) throws IOException {
		new Thread(new ClientReader(in, queue)).start();
	}

	public void addWriter(ClientWriter writer) {
		this.writer = writer;
	}

	public void processInput() throws ExitProgram, EndOfGameException, ServerUnavailableException {
		try {
			handleInput(queue.take());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
