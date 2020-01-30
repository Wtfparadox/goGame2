package goGame;

import goBoard.Board;
import goBoard.ScoreBoard;
import goBoard.StoneColor;
import goExceptions.FormerBoardException;
import goProtocol.ProtocolMessages;
import goRulesImplementation.BoardRef;
import goRulesImplementation.CaptureReferee;

public abstract class Game {

	public static final double KOMI = 0.5;

	protected Board board;
	protected CaptureReferee capturedFields;
	protected BoardRef boardReferee;
	protected ScoreBoard score;

	public Game(int boardDimension) {
		board = new Board(boardDimension);
		capturedFields = new CaptureReferee(board);
		boardReferee = new BoardRef(board);
		score = new ScoreBoard(KOMI, board);
	}

	protected void updateBoardConfiguration(int move, StoneColor color) throws FormerBoardException {
		Board copyBoard = board.deepCopy(board);
		copyBoard.placeStoneFromIndex(move, color);
		processMove(move);
		registerBoard();
		board = copyBoard;
	}

	public boolean isValidMove(int move) {
		return boardReferee.validMove(move);
	}

	protected void registerBoard() throws FormerBoardException {
		boardReferee.processBoard();
	}

	public char determineWinner() {
		score.calculateScore(board.getBoardMatrix());
		if (score.getBlackScore() > score.getWhiteScore()) {
			return ProtocolMessages.BLACK;
		} else {
			return ProtocolMessages.WHITE;
		}
	}

	public ScoreBoard getScoreBoard() {
		return score;
	}

	protected void processMove(int index) {
		capturedFields.markPointsForRemoval(index);
		board.removeStoneFromIndex(capturedFields.getStoneList(index), capturedFields.getListColor(),
				capturedFields.getListState());
		board.removeStoneFromIndex(capturedFields.getLibertyList(index), capturedFields.getListColor(),
				capturedFields.getListState());
	}

	public String boardToString() {
		return board.toString();
	}
}
