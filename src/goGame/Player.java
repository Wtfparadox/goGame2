package goGame;

import goExceptions.ServerUnavailableException;

public abstract class Player {
	protected String name;
	protected StoneColor stone;
	protected boolean turn;

	public Player(String nameArg, StoneColor stoneArg) {
		name = nameArg;
		stone = stoneArg;
	}

	public String getName() {
		return name;
	}

	public StoneColor getColor() {
		return stone;
	}

	public abstract int determineMove(Board board);

	public abstract int handleTurn(Board board) throws ServerUnavailableException;

	public void makeMove(Board board) throws ServerUnavailableException {
		int index = handleTurn(board);
		board.placeStone(board.getRowFromIndex(index), board.getColFromIndex(index), stone);
	}

}
