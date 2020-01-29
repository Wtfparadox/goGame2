package goPlayers;

import java.util.Queue;

import goBoard.Board;
import goBoard.StoneColor;
import goClient.ClientWriter;
import goClient.PlayerReader;
import goExceptions.ConnectionLostException;
import goProtocol.ProtocolMessages;

public class HumanPlayer extends Player {

	private PlayerReader reader;
	private ClientWriter writer;

	public HumanPlayer(String nameArg, StoneColor colorArg, Queue<String> queue, ClientWriter writer) {
		super(nameArg, colorArg);
		reader = new PlayerReader(System.in, queue);
		this.writer = writer;
	}

	public int determineMove() {
		int move = reader.readInteger();
		return move;
	}

	public int handleTurn(Board board) throws ConnectionLostException {
		System.out.println("What do you want to do? [M]ove, [Q]uit, [P]ass");
		String input = reader.readMessage();

		switch (input.charAt(0)) {
		case ProtocolMessages.QUIT:
			System.out.println("Quitting");
			writer.quitGame();
			return -1;
		case ProtocolMessages.PASS:
			System.out.println("You passed your turn");
			writer.doMove(Character.toString(ProtocolMessages.PASS));
			return -1;
		case ProtocolMessages.MOVE:
			int turn = determineMove();
			writer.doMove(String.valueOf(turn));
			return turn;
		default:
			return -1;
		}

	}

}
