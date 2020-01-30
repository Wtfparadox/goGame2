package goRulesImplementation;

import goBoard.Board;
import goBoard.PointState;

public class Referee {
	protected Board board;

	public Referee(Board boardArg) {
		board = boardArg;
	}

	public boolean isOccupied(int index) {
		return board.getPointFromIndex(index).getState() == PointState.OCCUPIED;
	}

	public boolean validMove(int index) {
		return !isOccupied(index) && board.validPointFromIndex(index);
	}
}
