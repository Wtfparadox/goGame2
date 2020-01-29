package goController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import goExceptions.ClientUnavailableException;
import goExceptions.FormerBoardException;
import goGame.Board;
import goGame.BoardRef;
import goGame.CaptureReferee;
import goGame.Point;
import goGame.PointState;
import goGame.ServerPlayer;
import goGame.StoneColor;
import goProtocol.ProtocolMessages;
import goServer.InputHandlerServer;

public class GameController {

	private static final String PASSSTRING = Character.toString(ProtocolMessages.PASS);

	private List<InputHandlerServer> clients;
	private boolean whiteTurn;
	private Board board;
	private String lastMove, currentMove;
	private CaptureReferee capturedFields;
	private BoardRef boardReferee;

	public GameController() {
		board = new Board(5);
		clients = new ArrayList<>();
		lastMove = "";
		capturedFields = new CaptureReferee(board);
		boardReferee = new BoardRef(board);
	}

	public void setCurrentMove(String move) {
		currentMove = move;
	}

	public void doBoardMove(StoneColor color, String index)
			throws IOException, ClientUnavailableException, FormerBoardException {
		int move = Integer.parseInt(index);
		checkBoardConfiguration(move, color);
		lastMove = currentMove;
		manageTurn(lastMove);
	}

	public void doPassMove() throws ClientUnavailableException {
		lastMove = PASSSTRING;
		manageTurn(lastMove);
	}

	private void checkBoardConfiguration(int move, StoneColor color) throws FormerBoardException {
		Board copyBoard = board.deepCopy(board);
		copyBoard.placeStoneFromIndex(move, color);
		processMove(move);
		registerBoard();
		board = copyBoard;
	}

	public boolean isValidMove(int move) {
		return true;
	}

	private void registerBoard() throws FormerBoardException {
		boardReferee.processBoard();
	}

	private void processMove(int index) {
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

	public boolean gameOver() {
		return lastMove.contentEquals(PASSSTRING) && currentMove.contentEquals(PASSSTRING);
	}

	private char determineWinnerAndSetScores() {
		return 'a';
	}

	private void manageTurn(String lastMove) throws ClientUnavailableException {
		whiteTurn ^= true;
		if (whiteTurn) {
			clients.get(1).notifyTurn(lastMove);
		} else {
			clients.get(0).notifyTurn(lastMove);
		}
	}

	public void readyForGame() throws IOException, ClientUnavailableException {
		if (clients.size() == 2) {
			clients.get(0).setPlayer(new ServerPlayer("Player 1", StoneColor.BLACK));
			clients.get(1).setPlayer(new ServerPlayer("Player 2", StoneColor.WHITE));
			for (InputHandlerServer client : clients) {
				client.beginGame();
			}
			manageTurn(PASSSTRING);
		}
	}

	public void addInputHandler(InputHandlerServer handler) {
		clients.add(handler);
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
