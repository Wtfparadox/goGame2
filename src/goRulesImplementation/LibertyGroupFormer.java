package goRulesImplementation;

import java.util.HashSet;
import java.util.Set;

import goBoard.Board;
import goBoard.Point;
import goBoard.StoneColor;

public class LibertyGroupFormer extends GroupFormer {

	private boolean[] isVisited;
	private Set<StoneColor> colorSet;

	public LibertyGroupFormer(int boardIndex, Board board) {
		super(boardIndex, board);
		isVisited = new boolean[board.getSize() * board.getSize()];
		colorSet = new HashSet<>();
	}

	public void findFreeFields(int index) {
		if (isVisited[index]) {
			return;
		}

		isVisited[index] = true;
		pointGroup.add(index);
		for (int neighborIndex : board.getNeighborCoordinates(index)) {
			Point nextPoint = board.getPointFromIndex(neighborIndex);
			if (nextPoint.isFreePoint()) {
				findFreeFields(neighborIndex);
			} else if (bothColorsFound(nextPoint)) {
				if (initialPoint.getColor() == StoneColor.NONE) {
					pointGroup.clear();
					return;
				}
			}
		}
	}

	@Override
	public void formGroup(int index) {
		findFreeFields(index);
		if (oneColorFound() && initialPoint.getColor() != StoneColor.NONE) {
			pointGroup.clear();
		}
	}

	private boolean oneColorFound() {
		return colorSet.size() == 1;
	}

	private boolean bothColorsFound(Point nextPoint) {
		colorSet.add(nextPoint.getColor());
		return colorSet.size() == 2;
	}

}
