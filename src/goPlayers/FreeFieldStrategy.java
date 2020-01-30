package goPlayers;

import java.util.ArrayList;
import java.util.List;

import goBoard.Board;
import goBoard.Point;
import goBoard.PointState;

public class FreeFieldStrategy implements Strategy {

	@Override
	public int determineMove(Board board) {
		List<Integer> freeFields = getFreeFields(board);
		int randomFreeField = (int) (Math.random() * freeFields.size() / 1);
		return freeFields.get(randomFreeField);
	}

	public List<Integer> getFreeFields(Board board) {
		ArrayList<Integer> list = new ArrayList<>();
		Point[][] boardMatrix = board.getBoardMatrix();
		System.out.println(board.getSize());
		int i = 0;
		for (i = 0; i < board.getSize(); i++) {
			for (int j = 0; j < board.getSize(); j++) {
				if (boardMatrix[i][j].getState() == PointState.FREE) {
					System.out.println("reached");
					list.add(i * board.getSize() + j);
				}
			}
		}
		System.out.println(i);
		System.out.println(list.size());
		return list;
	}

}
