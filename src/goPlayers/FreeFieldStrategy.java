package goPlayers;

import java.util.ArrayList;

import goBoard.Board;
import goBoard.Point;
import goBoard.PointState;

public class FreeFieldStrategy implements Strategy {

	@Override
	public int determineMove(Board board) {
		int randomFreeField = (int) (Math.random() * getFreeFields(board).length / 1);
		return randomFreeField;
	}

	private Integer[] getFreeFields(Board board) {
		ArrayList<Integer> list = new ArrayList<>();
		Point[][] boardMatrix = board.getBoardMatrix();
		for (int i = 0; i < board.getSize(); i++) {
			for (int j = 0; j < board.getSize(); j++) {
				if (boardMatrix[i][j].getState() == PointState.FREE) {
					list.add(i * board.getSize() + j);
				}
			}
		}
		return list.toArray(new Integer[0]);
	}

}
