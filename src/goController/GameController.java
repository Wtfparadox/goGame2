package goController;

import java.util.List;

import goGame.Board;
import goGame.Point;
import goGame.PointState;
import goGame.ServerPlayer;
import goGame.StoneColor;
import goServer.InputHandlerServer;

public class GameController {

	private List<InputHandlerServer> clients;
	private boolean whiteTurn;
	private Board board;

	public GameController() {
		board = new Board(19);
	}

	public void turnHandler(StoneColor color) {

	}

	private boolean isValidMove(int move) {
		return false;
	}

	public boolean startGame() {
		if (clients.size() == 2) {
			clients.get(0).setPlayer(new ServerPlayer("Player 1", StoneColor.BLACK));
			clients.get(1).setPlayer(new ServerPlayer("Player 2", StoneColor.WHITE));
			return true;
		} else {
			return false;
		}
	}

	public void addInputHandlerServer(InputHandlerServer ihs) {
		clients.add(ihs);
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
