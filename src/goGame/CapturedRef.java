package goGame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CapturedRef extends Referee {
	private boolean[][] isVisited;
	private boolean clearPoints;
	private StoneColor listColor;
	private PointState listState;
	private StoneColor firstEntryColor;
	private Set<StoneColor> otherColorSet;
	private List<Integer[]> pointsToBeChanged;
	private List<Integer> neighborCoordinates = new ArrayList<>() {
		{
			add(1);
			add(-1);
			add(0);
			add(0);
		}
	};

	public CapturedRef(Board boardArg) {
		super(boardArg);
		isVisited = new boolean[boardArg.getBoardMatrix().length][boardArg.getBoardMatrix().length];
		otherColorSet = new HashSet<>();
	}

	public List<Integer[]> capturedPoints(int row, int col, Board board) {
		pointsToBeChanged = new ArrayList<>();
		firstEntryColor = board.getPoint(row, col).getColor();
		try {
			neighbors(row, col, board);
			if (clearPoints) {
				pointsToBeChanged.clear();
			}
			// isVisited = new boolean[isVisited.length][isVisited.length];
			formPointGroup(row, col, board);
			setCapturedListProperties();
			System.out.println(otherColorSet.size());
			// printGroup();
			return pointsToBeChanged;
		} finally {
			reset();
		}
	}

	private void formPointGroup(int row, int col, Board board) {
		int oldSize = 0;
		int otherColorChainSize = pointsToBeChanged.size();
		for (int[] c : neighborList(neighborCoordinates, row, col, board)) {
			if (isVisited[c[0]][c[1]]) {
				System.out.println("row " + c[0] + "col " + c[1]);
				continue;
			}
			System.out.println("visit for first time: row " + c[0] + "col " + c[1]);
			resetClearListConditions(row, col);
			oldSize = pointsToBeChanged.size();
			neighbors(c[0], c[1], board);
//			System.out.println(oldSize);
//			System.out.println(pointsToBeChanged.size());
//			System.out.println(cutListAddition());
			System.out.println(otherColorSet.size());
			listController(oldSize, pointsToBeChanged.size(), cutListAddition(board.getPoint(c[0], c[1])));
			System.out.println(pointsToBeChanged.size());
		}
		listController(0, otherColorChainSize, pointsToBeChanged.size() != otherColorChainSize);
	}

	private void resetClearListConditions(int row, int col) {
		otherColorSet.clear();
		// otherColorSet.add(firstEntryColor);
		clearPoints = false;
	}

	private void listController(int oldSize, int cutLimit, boolean toCut) {
		if (toCut) {
			int i = 0;
			Iterator<Integer[]> iter = pointsToBeChanged.listIterator(oldSize);
			while (iter.hasNext() && i < cutLimit) {
				iter.next();
				iter.remove();
				i++;
			}
		}
	}

	private void setCapturedListProperties() {
		if (!pointsToBeChanged.isEmpty()) {
			Point listPoint = board.getPoint(pointsToBeChanged.get(0)[0], pointsToBeChanged.get(0)[1]);
			StoneColor color = listPoint.getColor();
			PointState state = listPoint.getState();
			if (state == PointState.CONQUERED) {
				listColor = StoneColor.NONE;
				listState = PointState.FREE;
			} else if (state == PointState.OCCUPIED) {
				listColor = color.other();
				listState = PointState.CONQUERED;
			} else if (otherColorSet.size() < 2) {
				listColor = firstEntryColor;
				listState = PointState.CONQUERED;
			} else {
				pointsToBeChanged.clear();
			}
		}
	}

	public StoneColor getListColor() {
		return listColor;
	}

	public PointState getListState() {
		return listState;
	}

	private void neighbors(int row, int col, Board board) {
		if (clearPoints || isVisited[row][col]) {
			return;
		}
		markVisit(row, col);
		Point currentPoint = board.getPoint(row, col);
		pointsToBeChanged.add(new Integer[] { row, col });

		for (int[] c : neighborList(neighborCoordinates, row, col, board)) {
			Point neighborPoint = board.getPoint(c[0], c[1]);
			if (currentPoint.isIdentical(neighborPoint)) {
				neighbors(c[0], c[1], board);
			} else {
				if (isStopped(currentPoint, neighborPoint)) {
					clearPoints = true;
					return;
				}
			}
		}
	}

	private boolean isStopped(Point currentPoint, Point neighborPoint) {
		if (currentPoint.getState() == PointState.OCCUPIED) {
			return neighborPoint.isFreePoint();
		} else if (neighborPoint.getState() == PointState.OCCUPIED) {
//			System.out.println("hi im here");
//			System.out.println(neighborPoint.getColor());
			otherColorSet.add(neighborPoint.getColor());
			return false;
		} else {
			return false;
		}
	}

	private List<int[]> neighborList(List<Integer> neighbors, int row, int col, Board board) {
		int i = 0;
		int j = neighbors.size() - 1;
		List<int[]> list = new ArrayList<>();
//		System.out.println("main point " + row + " col " + col);
		while (i < neighbors.size()) {
			int nextRow = row + neighbors.get(i);
			int nextCol = col + neighbors.get(j);
			if (board.validPoint(nextRow, nextCol)) {
//				System.out.println("added row " + nextRow + " col " + nextCol);
				list.add(new int[] { nextRow, nextCol });
			}
			i++;
			j--;
		}
		return list;
	}

	private void reset() {
		clearPoints = false;
		otherColorSet.clear();
		isVisited = new boolean[isVisited.length][isVisited.length];
	}

	private boolean cutListAddition(Point point) {
		if (point.getState() == PointState.OCCUPIED) {
			return clearPoints;
		} else if (point.getState() == PointState.FREE) {
			return otherColorSet.size() == 2;
		} else {
			return otherColorSet.size() == 1;
		}

	}

	private void printGroup() {
		for (Integer[] i : pointsToBeChanged) {
			System.out.println(board.getPoint(i[0], i[1]).toString());
		}
	}

	private void markVisit(int row, int col) {
		isVisited[row][col] = true;
	}

}
