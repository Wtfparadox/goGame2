package goGame;

public abstract class Player {
	protected String name;
	protected StoneColor stone;

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

	public void makeMove(Board board) {
		int index = determineMove(board);
		board.placeStone(board.getRowFromIndex(index), board.getColFromIndex(index), stone);
	}

}
