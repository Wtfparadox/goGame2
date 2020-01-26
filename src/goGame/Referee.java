package goGame;

public class Referee {
	protected Board board;

	public Referee(Board boardArg) {
		board = boardArg;
	}

	public boolean isOccupied(int row, int col) {
		return board.getBoardMatrix()[row][col].getState() == PointState.OCCUPIED;
	}

	public boolean validMove() {
		// to be implemented
		return false;
	}
}
