package goGame;

import java.util.ArrayList;
import java.util.List;

import goGUI.GTG;
import observers.BoardWatcher;
import observers.ObservableBoard;

public class Board implements ObservableBoard {

	public int dim;
	private Point[][] boardMatrix;
	private List<BoardWatcher> observers = new ArrayList<BoardWatcher>();

	public Board(int boardDimension, GTG gtg) {
		dim = boardDimension;
		boardMatrix = new Point[dim][dim];
		initializeBoard();
		addObserver(gtg);
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

	public void removeStone(List<Integer[]> removeList, StoneColor color, PointState state) {
		for (Integer[] coordinates : removeList) {
			Point p = boardMatrix[coordinates[0]][coordinates[1]];
			p.setState(state);
			p.setColor(color);
		}
		notifyAllObservers();
	}

	public boolean validPoint(int row, int col) {
		return row < boardMatrix.length && col < boardMatrix.length && row >= 0 && col >= 0;
	}

	public Point getPoint(int row, int col) {
		return boardMatrix[row][col];
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

	public String toString() {
		StringBuilder boardString = new StringBuilder(boardMatrix.length * boardMatrix.length - 1);
		for (int i = 0; i < boardMatrix.length; i++) {
			for (int j = 0; j < boardMatrix.length; j++) {
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

//	public static void main(String[] a) {
//		Board board = new Board(5, new GTG(5));
//		System.out.println(Arrays.toString(board.getBoardState()[0]));
//		board.initializeBoardEdge(0, 0, true);
//		System.out.println(Arrays.toString(board.getBoardState()[0]));
//
//	}

}
