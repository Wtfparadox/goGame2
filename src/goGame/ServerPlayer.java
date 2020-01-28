package goGame;

public class ServerPlayer extends Player {

	private int currentMove;

	public ServerPlayer(String nameArg, StoneColor stoneArg) {
		super(nameArg, stoneArg);
	}

	public void setCurrentMove(int index) {
		currentMove = index;
	}

	public int determineMove(Board board) {
		return currentMove;
	}

}
