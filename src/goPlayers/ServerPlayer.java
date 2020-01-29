package goPlayers;

import goBoard.Board;
import goBoard.StoneColor;

public class ServerPlayer extends Player {

	public ServerPlayer(String nameArg, StoneColor stoneArg) {
		super(nameArg, stoneArg);
	}

	public int determineMove(int index) {
		return index;
	}

	@Override
	public int handleTurn(Board board) {
		// TODO Auto-generated method stub
		return 0;
	}

}
