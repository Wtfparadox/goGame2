package goRulesImplementation;

import goBoard.Board;
import goBoard.Point;

public class StoneGroupFormer extends GroupFormer {

	private boolean[] isVisited;
	private boolean hasFreePoint;

	public StoneGroupFormer(int boardIndex, Point initialStone, Board board) {
		super(boardIndex, initialStone, board);
		isVisited = new boolean[board.getSize() * board.getSize()];
	}

	@Override
	public void formGroup(int index) {
		if (hasFreePoint | isVisited[index]) {
			return;
		}

		isVisited[index] = true;
		pointGroup.add(index);

		for (int neighborIndex : board.getNeighborCoordinates(index)) {
			Point nextPoint = board.getPointFromIndex(neighborIndex);
			if (nextPoint.isIdentical(initialPoint)) {
				formGroup(neighborIndex);
			} else if (nextPoint.isFreePoint()) {
				hasFreePoint = true;
				pointGroup.clear();
				return;
			}
		}
	}
}
