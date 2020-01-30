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
	private ServerGame game;
	private ServerWriter writer;

	public InputHandlerServer(ServerGame gc, OutputStream out) { // GoClientTUI tuiArg
		game = gc;
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
				game.addInputHandler(this);
				game.readyForGame();
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
			game.setCurrentMove(move);
			if (!game.gameOver()) {
				makeMove(move);
				writer.validMove(game.boardToString());
			} else {
				throw new EndOfGameException("The game has ended because both players passed consecutively.");
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
			return game.isValidMove(Integer.parseInt(move));
		} else {
			return true;
		}
	}

	private void makeMove(String move) throws IOException, EndOfGameException, ConnectionLostException {
		if (isInteger(move)) {
			try {
				game.doBoardMove(player.getColor(), move);
			} catch (FormerBoardException e) {
				writer.invalidMove("This board configuration has been played already");
				throw new EndOfGameException("Cheated");
			}
		} else {
			game.doPassMove();
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
		writer.giveTurnToMove(game.boardToString(), lastMove);
	}

	public void beginGame() throws ConnectionLostException {
		writer.startGame(player.getColor().toString().charAt(0), game.boardToString());
	}

	public void endGame(char reason) throws ConnectionLostException {
		writer.endGame(reason, game.determineWinner(), game.getScoreBoard().getWhiteScore(),
				game.getScoreBoard().getBlackScore());
	}
}
