package goServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.LinkedBlockingQueue;

import goClient.ClientReader;
import goController.GameController;
import goExceptions.ClientUnavailableException;
import goExceptions.EndOfGameException;
import goExceptions.ExitProgram;
import goExceptions.FormerBoardException;
import goGame.Player;
import goProtocol.ProtocolMessages;

public class InputHandlerServer {
	// private GoClientTUI tui;
	private LinkedBlockingQueue<String> queue;
	private GameController controller;
	private ServerWriter writer;

	private Player player;

	public InputHandlerServer(GameController gc, OutputStream out) { // GoClientTUI tuiArg
		controller = gc;
		writer = new ServerWriter(out);
		queue = new LinkedBlockingQueue<>();
	}

	private void handleInput(String input)
			throws ExitProgram, EndOfGameException, IOException, ClientUnavailableException {
		String[] inputArguments = input.split(ProtocolMessages.DELIMITER);
		if (inputArguments.length > 0) {
			switch (input.charAt(0)) {
			case ProtocolMessages.HANDSHAKE:
				System.out.println("Handshake recieved");
				writer.respondToHandshake("1");
				controller.addInputHandler(this);
				controller.readyForGame();
				break;
			case ProtocolMessages.QUIT:
				System.out.println("Quit received");
				break;
			case ProtocolMessages.MOVE:
				System.out.println("Move received");
				handleTurn(inputArguments[1]);
				break;
			}
		} else {
			System.out.println("Unkown command");
		}
	}

	private void handleTurn(String move) throws IOException, EndOfGameException, ClientUnavailableException {
		if (validateMove(move)) {
			controller.setCurrentMove(move);
			if (!controller.gameOver()) {
				makeMove(move);
				writer.validMove(controller.boardToString());
			} else {
				writer.endGame(ProtocolMessages.FINISHED, ProtocolMessages.BLACK, 1.0, 1.0);
				throw new EndOfGameException("Two passes");
			}
		} else {
			writer.invalidMove("Your move does not correspond to an existing field on the Go board.");
			throw new EndOfGameException("Cheated");
		}
	}

	private boolean validateMove(String move) throws IOException, EndOfGameException, ClientUnavailableException {
		if (!move.contentEquals("P") && !isInteger(move)) {
			return false;
		} else if (isInteger(move)) {
			return controller.isValidMove(Integer.parseInt(move));
		} else {
			return true;
		}
	}

	private void makeMove(String move) throws IOException, ClientUnavailableException, EndOfGameException {
		if (isInteger(move)) {
			try {
				controller.doBoardMove(player.getColor(), move);
			} catch (FormerBoardException e) {
				writer.invalidMove("This board configuration has been played already");
				throw new EndOfGameException("Cheated");
			}
		} else {
			controller.doPassMove();
		}
	}

	private boolean isInteger(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public void notifyTurn(String lastMove) throws ClientUnavailableException {
		writer.giveTurnToMove(controller.boardToString(), lastMove);
	}

	public void beginGame() throws ClientUnavailableException {
		writer.startGame(player.getColor().toString().charAt(0), controller.boardToString());
	}

	public void startReader(InputStream in) throws IOException {
		new Thread(new ClientReader(in, queue)).start();
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void processInput() throws ExitProgram, EndOfGameException, IOException, ClientUnavailableException {
		try {
			handleInput(queue.take());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
