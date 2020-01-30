package goClient;

import java.io.IOException;
import java.io.OutputStream;

import goBoard.StoneColor;
import goExceptions.ConnectionLostException;
import goExceptions.EndOfGameException;
import goGame.ClientGame;
import goProtocol.ProtocolMessages;
import goServerClientCommunication.InputHandler;

public class InputHandlerClient extends InputHandler {

	private GoClientTUI tui;
	private ClientGame game;
	private ClientWriter writer;

	public InputHandlerClient(GoClientTUI tuiArg, OutputStream out) throws IOException, ConnectionLostException {
		writer = new ClientWriter(out);
		tui = tuiArg;
		player = tui.choosePlayer(queue, writer);
	}

	@Override
	protected void handleInput(String input) throws EndOfGameException, IOException, ConnectionLostException {
		String[] inputArguments = input.split(ProtocolMessages.DELIMITER);
		if (inputArguments.length > 0) {
			switch (input.charAt(0)) {
			case ProtocolMessages.GAME:
				char color = inputArguments[2].charAt(0);
				tui.showMessage("Game will start, your color is " + color);
				player.setColor(stringToStoneColor(color));
				game = new ClientGame(5, color, this, player);
				break;
			case ProtocolMessages.TURN:
				tui.showMessage("Its your turn!");
				processTurn(inputArguments[2]);
				break;
			case ProtocolMessages.RESULT:
				processResult(inputArguments[1].charAt(0));
				break;
			case ProtocolMessages.END:
				processEndOfGame(inputArguments);
				throw new EndOfGameException("The game has ended");
			}
		} else {
			tui.showMessage("Message not defined in protocol, command unknown.");
		}
	}

	private void processEndOfGame(String[] input) {
		if (input[1].charAt(0) == ProtocolMessages.FINISHED) {
			tui.showMessage("The winner is " + input[2]);
			tui.showMessage("White score: " + input[3]);
			tui.showMessage("Black score: " + input[4]);
		} else {
			tui.showMessage("You win because the other player has failed at making a valid move");
		}
	}

	private void processResult(char result) throws EndOfGameException {
		if (result == ProtocolMessages.VALID) {
			tui.showMessage("The server has validated your move");
		} else {
			tui.showMessage("Your move is invalid! You lost");
			throw new EndOfGameException("Game has ended because your move was invalid");
		}
	}

	private void processTurn(String input) throws ConnectionLostException {
		if (!input.contentEquals(Character.toString(ProtocolMessages.PASS)) && (Object) input != null) {
			game.processOpponentsMove(Integer.parseInt(input));
		}
		game.giveTurn();
	}

	public void makeHandShake() throws IOException, ConnectionLostException {
		int version = tui.getInt("Which protocol version do you prefer?");
		String name = tui.getString("What is your name?");
		String color = tui.getColor();
		writer.doHandshake(version, name, color);
	}

	private StoneColor stringToStoneColor(char color) {
		if (color == 'B') {
			return StoneColor.BLACK;
		} else {
			return StoneColor.WHITE;
		}
	}
}
