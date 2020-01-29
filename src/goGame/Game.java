package goGame;

import java.util.TreeSet;

import goBoard.Board;
import goBoard.BoardRef;
import goBoard.Point;
import goBoard.PointState;
import goBoard.StoneColor;
import goExceptions.FormerBoardException;
import goRulesImplementation.CaptureReferee;

public abstract class Game {

	protected Board board;
	protected CaptureReferee capturedFields;
	protected BoardRef boardReferee;

	public Game(int boardDimension) {
		board = new Board(boardDimension);
		capturedFields = new CaptureReferee(board);
		boardReferee = new BoardRef(board);
	}

	protected void checkBoardConfiguration(int move, StoneColor color) throws FormerBoardException {
		Board copyBoard = board.deepCopy(board);
		copyBoard.placeStoneFromIndex(move, color);
		processMove(move);
		registerBoard();
		board = copyBoard;
	}

	public boolean isValidMove(int move) {
		return true;
	}

	protected void registerBoard() throws FormerBoardException {
		boardReferee.processBoard();
	}

	protected char determineWinnerAndSetScores() {
		return 'a';
	}

	protected void processMove(int index) {
		TreeSet<Integer> set1 = capturedFields.getAndSetLibertySet(index);
		if (!set1.isEmpty()) {
			StoneColor color = capturedFields.getListColor();
			PointState state = capturedFields.getListState();
			if (state == PointState.CONQUERED && color == StoneColor.NONE) {
				color = board.getPointFromIndex(index).getColor();
			}
			board.removeStoneFromIndex(set1, color, state);
		}
		TreeSet<Integer> set2 = capturedFields.getAndSetStoneSet(index);
		if (!set2.isEmpty()) {
			StoneColor color = capturedFields.getListColor();
			PointState state = capturedFields.getListState();
			if (state == PointState.CONQUERED && color == StoneColor.NONE) {
				System.out.println("reached");
				color = board.getPointFromIndex(index).getColor();
			}
			board.removeStoneFromIndex(set2, color, state);
		}
	}

	public String boardToString() {
		StringBuilder boardString = new StringBuilder(board.getSize() * board.getSize() - 1);
		for (int i = 0; i < board.getSize(); i++) {
			for (int j = 0; j < board.getSize(); j++) {
				boardString.append(pointToChar(board.getBoardMatrix()[i][j]));
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
