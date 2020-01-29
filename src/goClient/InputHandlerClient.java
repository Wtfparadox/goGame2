package goClient;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import goExceptions.EndOfGameException;
import goExceptions.ExitProgram;
import goExceptions.ServerUnavailableException;
import goGame.HumanPlayer;
import goGame.Player;
import goGame.StoneColor;
import goProtocol.ProtocolMessages;

public class InputHandlerClient {
	private GoClientTUI tui;
	private LinkedBlockingQueue<String> queue;
	private ClientGame game;
	private ClientWriter writer;
	private Player player;

	public InputHandlerClient(GoClientTUI tuiArg) {
		queue = new LinkedBlockingQueue<>();
		tui = tuiArg;
	}

	private void handleInput(String input)
			throws ExitProgram, EndOfGameException, ServerUnavailableException, IOException {
		String[] inputArguments = input.split(ProtocolMessages.DELIMITER);
		if (inputArguments.length > 0) {
			switch (input.charAt(0)) {
			case ProtocolMessages.QUIT:
				writer.quitGame();
				throw new ExitProgram("User has quit.");
			case ProtocolMessages.GAME:
				tui.showMessage("game start");
				char color = inputArguments[2].charAt(0);
				game = new ClientGame(color, this, new HumanPlayer("Thomas", stringToStoneColor(color), queue, writer));
				break;
			case ProtocolMessages.TURN:
				tui.showMessage("Its your turn!");
				processTurn(inputArguments[2]);
				break;
			case ProtocolMessages.RESULT:
				tui.showMessage("game result");
				break;
			case ProtocolMessages.END:
				String reason = "";
				throw new EndOfGameException("The game has ended because " + reason);
//			case 'O':
//				if (inputArguments[1] == "2832432") {
//					writer.doMove(Integer.parseInt(inputArguments[2]));
//				} else {
//					tui.showMessage("Message not defined in protocol, command unknown.");
//				}
//				break;
			}
		} else {
			tui.showMessage("Message not defined in protocol, command unknown.");
		}
	}

	private void processTurn(String input) throws ServerUnavailableException {
		if (!input.contentEquals(Character.toString(ProtocolMessages.PASS)) && input != null) {
			game.processOpponentsMove(Integer.parseInt(input));
		}
		game.giveTurn();
	}

//	public void handleTurn(String input) throws ServerUnavailableException {
//		if (!input.contentEquals(Character.toString(ProtocolMessages.PASS)) && input != null) {
//			int otherPlayerMove = Integer.parseInt(input);
//			game.processOpponentsMove(otherPlayerMove);
//		}
//		int ownMove = game.processOwnMove();
//		writer.doMove(ownMove);
//	}

	public void startReader(Reader reader) throws IOException {
		new Thread(reader).start();
	}

	public void setPlayer(Player player) throws IOException {
		this.player = player;
	}

	public Queue<String> getQueue() {
		return queue;
	}

	public void addWriter(ClientWriter writer) {
		this.writer = writer;
	}

	public void processInput() throws ExitProgram, EndOfGameException, ServerUnavailableException, IOException {
		try {
			handleInput(queue.take());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private StoneColor stringToStoneColor(char color) {
		if (color == 'B') {
			return StoneColor.BLACK;
		} else {
			return StoneColor.WHITE;
		}
	}
}
