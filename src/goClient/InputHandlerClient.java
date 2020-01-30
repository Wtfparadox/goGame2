package goClient;

import java.io.IOException;
import java.io.OutputStream;

import goBoard.StoneColor;
import goExceptions.ConnectionLostException;
import goExceptions.EndOfGameException;
import goGame.ClientGame;
import goPlayers.HumanPlayer;
import goProtocol.ProtocolMessages;
import goServerClientCommunication.InputHandler;

public class InputHandlerClient extends InputHandler {

	private GoClientTUI tui;
	private ClientGame game;
	private ClientWriter writer;

	public InputHandlerClient(GoClientTUI tuiArg, OutputStream out) throws IOException, ConnectionLostException {
		writer = new ClientWriter(out);
		tui = tuiArg;
	}

	@Override
	protected void handleInput(String input) throws EndOfGameException, IOException, ConnectionLostException {
		String[] inputArguments = input.split(ProtocolMessages.DELIMITER);
		if (inputArguments.length > 0) {
			switch (input.charAt(0)) {
			case ProtocolMessages.GAME:
				tui.showMessage("game start");
				char color = inputArguments[2].charAt(0);
				game = new ClientGame(5, color, this,
						new HumanPlayer("Thomas", stringToStoneColor(color), queue, writer));
				break;
			case ProtocolMessages.TURN:
				tui.showMessage("Its your turn!");
				processTurn(inputArguments[2]);
				break;
			case ProtocolMessages.RESULT:
				tui.showMessage(inputArguments[0]);
				break;
			case ProtocolMessages.END:
				tui.showMessage(inputArguments[0]);
				throw new EndOfGameException("The game has ended because " + inputArguments[0]);
			}
		} else {
			tui.showMessage("Message not defined in protocol, command unknown.");
		}
	}

	private void processTurn(String input) throws ConnectionLostException {
		if (!input.contentEquals(Character.toString(ProtocolMessages.PASS)) && input != null) {
			game.processOpponentsMove(Integer.parseInt(input));
		}
		game.giveTurn();
	}

	public void makeHandShake() throws IOException, ConnectionLostException {
//		int version = tui.getInt("Which protocol version do you prefer?");
//		String name = tui.getString("What is your name?");
//		String color = tui.getColor();
//		writer.doHandshake(version, name, color);
		writer.doHandshake(1, "w", "w");
	}

	private StoneColor stringToStoneColor(char color) {
		if (color == 'B') {
			return StoneColor.BLACK;
		} else {
			return StoneColor.WHITE;
		}
	}
}
