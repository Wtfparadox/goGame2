package goPlayers;

import goBoard.Board;

public interface Strategy {

	public int determineMove(Board board);
}
