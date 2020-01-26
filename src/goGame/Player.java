package goGame;

public class Player {
	private String name;
	private StoneColor stone;

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

	public void makeMove(int row, int col, Board board) {
		board.placeStone(row, col, stone);
	}

}
