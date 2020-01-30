package goPlayers;

import java.util.Queue;

import goBoard.Board;
import goBoard.StoneColor;
import goClient.ClientReader;
import goClient.ClientWriter;
import goExceptions.ConnectionLostException;
import goServerClientCommunication.Reader;

public class ComputerPlayer extends Player {

	private Strategy strategy;
	private Reader reader;
	private ClientWriter writer;

	public ComputerPlayer(String nameArg, StoneColor stoneArg, Strategy strategy, Queue<String> queue,
			ClientWriter writer) {
		super(nameArg, stoneArg);
		reader = new ClientReader(System.in, queue);
		new Thread(reader).start();
		this.writer = writer;
		this.strategy = strategy;
	}

	@Override
	public int handleTurn(Board board) throws ConnectionLostException {
		int move = strategy.determineMove(board);
		writer.doMove(String.valueOf(move));
		return move;
	}
}
