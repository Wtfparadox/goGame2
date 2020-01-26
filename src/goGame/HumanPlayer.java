package goGame;

public class HumanPlayer extends Player {

	public HumanPlayer(String nameArg, StoneColor colorArg) {
		super(nameArg, colorArg);
	}

	public void makeMove(int row, int col, Board board) {
		board.placeStone(row, col, this.getColor());
	}

}
