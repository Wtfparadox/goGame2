package goBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import observers.BoardWatcher;
import observers.ObservableBoard;

public class Board implements ObservableBoard {

	// NEEDS REVISION
	public int dim;
	private int[] neighbors;
	private Point[][] boardMatrix;
	private List<BoardWatcher> observers = new ArrayList<BoardWatcher>();

	public Board(int boardDimension) {
		dim = boardDimension;
		neighbors = new int[] { -dim, dim, 1, -1 };
		boardMatrix = new Point[dim][dim];
		initializeBoard();
	}

	public Board(Board board) {
		dim = board.dim;
		neighbors = board.neighbors;
		boardMatrix = new Point[dim][dim];
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				boardMatrix[i][j] = board.getPoint(i, j);
			}
		}
	}

	public Board deepCopy(Board board) {
		Board copyBoard = new Board(this);
		return copyBoard;
	}

	public Point[] buildEdgeRow() {
		Point[] edge = new Point[boardMatrix.length];
		for (int a = 0; a < boardMatrix.length; a++) {
			edge[a] = new Point();
			edge[a].setEdge();
		}
		edge[0].setCorner();
		edge[boardMatrix.length - 1].setCorner();
		return edge;
	}

	public Point[] buildRow() {
		Point[] edge = new Point[boardMatrix.length];
		for (int a = 0; a < boardMatrix.length; a++) {
			edge[a] = new Point();
		}
		edge[0].setEdge();
		edge[boardMatrix.length - 1].setEdge();
		return edge;
	}

	public void initializeBoard() {
		for (int i = 1; i < boardMatrix.length - 1; i++) {
			boardMatrix[i] = buildRow();
		}
		boardMatrix[0] = buildEdgeRow();
		boardMatrix[boardMatrix.length - 1] = buildEdgeRow();
	}

	public void placeStone(int row, int col, StoneColor c) {
		boardMatrix[row][col].setState(PointState.OCCUPIED);
		boardMatrix[row][col].setColor(c);
		notifyAllObservers();
	}

	public void placeStoneFromIndex(int index, StoneColor c) {
		placeStone(getRowFromIndex(index), getColFromIndex(index), c);
	}

	public void removeStone(List<Integer[]> removeList, StoneColor color, PointState state) {
		for (Integer[] coordinates : removeList) {
			Point p = boardMatrix[coordinates[0]][coordinates[1]];
			p.setState(state);
			p.setColor(color);
		}
		notifyAllObservers();
	}

	public void removeStoneFromIndex(Set<Integer> removeSet, StoneColor color, PointState state) {
		for (Integer i : removeSet) {
			Point p = boardMatrix[getRowFromIndex(i)][getColFromIndex(i)];
			p.setState(state);
			p.setColor(color);
		}
		notifyAllObservers();
	}

	public boolean validPoint(int row, int col) {
		return row < boardMatrix.length && col < boardMatrix.length && row >= 0 && col >= 0;
	}

	public boolean validPointFromIndex(int index) {
		return index >= 0 && index < dim * dim;
	}

	public Point getPoint(int row, int col) {
		return boardMatrix[row][col];
	}

	public Point getPointFromIndex(int index) {
		return boardMatrix[getRowFromIndex(index)][getColFromIndex(index)];
	}

	@Override
	public Point[][] getBoardMatrix() {
		return boardMatrix;
	}

	@Override
	public void addObserver(BoardWatcher observer) {
		observers.add(observer);
	}

	@Override
	public void removeObserver(BoardWatcher observer) {
		observers.remove(observer);

	}

	public void notifyAllObservers() {
		for (BoardWatcher b : observers) {
			b.update(this);
		}
	}

	public int getSize() {
		return dim;
	}

	public int getRowFromIndex(int index) {
		return index / dim;
	}

	public int getColFromIndex(int index) {
		return index % dim;
	}

	public List<Integer> getNeighborCoordinates(int index) {
		List<Integer> neighborCoordinates = new ArrayList<>();
		int length = neighbors.length;
		if (index % dim == 0) {
			length = neighbors.length - 1;
			neighbors[2] = 1;
			neighbors[3] = -1;
		} else if ((index + 1) % dim == 0) {
			length = neighbors.length - 1;
			neighbors[2] = -1;
			neighbors[3] = 1;
		}

		for (int i = 0; i < length; i++) {
			int neighborIndex = neighbors[i] + index;
			if (validPointFromIndex(neighborIndex)) {
				neighborCoordinates.add(neighborIndex);
			}
		}
		return neighborCoordinates;
	}

	public String toString() {
		StringBuilder boardString = new StringBuilder(dim * dim - 1);
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				boardString.append(pointToChar(boardMatrix[i][j]));
			}
		}
		return boardString.toString();
	}

	public Character pointToChar(Point point) {
		if (point.getState() == PointState.FREE || point.getState() == PointState.CONQUERED) {
			return 'U';
		} else if (point.getColor() == StoneColor.BLACK) {
			return 'B';
		} else {
			return 'W';
		}
	}

}
