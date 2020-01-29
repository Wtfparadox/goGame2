package goGame;

import goExceptions.ServerUnavailableException;

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

	@Override
	public int handleTurn(Board board) throws ServerUnavailableException {
		// TODO Auto-generated method stub
		return 0;
	}

}
