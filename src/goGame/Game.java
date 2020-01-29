package goGame;

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

	protected void updateBoardConfiguration(int move, StoneColor color) throws FormerBoardException {
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
		capturedFields.markPointsForRemoval(index);
		board.removeStoneFromIndex(capturedFields.getStoneList(index), capturedFields.getListColor(),
				capturedFields.getListState());
		board.removeStoneFromIndex(capturedFields.getLibertyList(index), capturedFields.getListColor(),
				capturedFields.getListState());
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
