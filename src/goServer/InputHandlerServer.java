package goServer;

import java.io.IOException;
import java.io.OutputStream;

import goExceptions.ConnectionLostException;
import goExceptions.EndOfGameException;
import goExceptions.ExitProgram;
import goExceptions.FormerBoardException;
import goGame.ServerGame;
import goProtocol.ProtocolMessages;
import goServerClientCommunication.InputHandler;

public class InputHandlerServer extends InputHandler {
	// private GoClientTUI tui;
	private ServerGame controller;
	private ServerWriter writer;

	public InputHandlerServer(ServerGame gc, OutputStream out) { // GoClientTUI tuiArg
		controller = gc;
		writer = new ServerWriter(out);
	}

	@Override
	protected void handleInput(String input)
			throws ExitProgram, EndOfGameException, IOException, ConnectionLostException {
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

	private void handleTurn(String move) throws IOException, EndOfGameException, ConnectionLostException {
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

	private boolean validateMove(String move) throws IOException, EndOfGameException {
		if (!move.contentEquals("P") && !isInteger(move)) {
			return false;
		} else if (isInteger(move)) {
			return controller.isValidMove(Integer.parseInt(move));
		} else {
			return true;
		}
	}

	private void makeMove(String move) throws IOException, EndOfGameException, ConnectionLostException {
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

	public void notifyTurn(String lastMove) throws ConnectionLostException {
		writer.giveTurnToMove(controller.boardToString(), lastMove);
	}

	public void beginGame() throws ConnectionLostException {
		writer.startGame(player.getColor().toString().charAt(0), controller.boardToString());
	}
}
