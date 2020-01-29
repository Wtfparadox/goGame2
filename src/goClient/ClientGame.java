package goClient;

import java.io.IOException;
import java.util.TreeSet;

import goExceptions.ServerUnavailableException;
import goGUI.GTG;
import goGame.Board;
import goGame.CaptureReferee;
import goGame.Player;
import goGame.PointState;
import goGame.StoneColor;

public class ClientGame {

	private Board board;
	private Player player;
	private InputHandlerClient handler;
	private CaptureReferee capturedFields;

	public ClientGame(char color, InputHandlerClient handler, Player player) throws IOException {
		this.player = player;
		this.handler = handler;
		handler.setPlayer(player);
		initGame(5);
	}

	private void initGame(int dim) {
		board = new Board(dim);
		capturedFields = new CaptureReferee(board);
		GTG gui = new GTG();
		board.addObserver(gui);
		gui.initGUI(dim);
	}

	public void giveTurn() throws ServerUnavailableException {
		int move = player.handleTurn(board);
		if (move >= 0) {
			board.placeStoneFromIndex(move, player.getColor());
			processMove(move);
		}
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

	public void processOpponentsMove(int otherPlayerMove) {
		board.placeStoneFromIndex(otherPlayerMove, player.getColor().other());
		processMove(otherPlayerMove);
	}

	public int processOwnMove() {
		int ownMove = player.determineMove(board);
		board.placeStoneFromIndex(ownMove, player.getColor());
		return ownMove;
	}

}
