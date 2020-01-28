package goController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import goExceptions.ClientUnavailableException;
import goGame.Board;
import goGame.Point;
import goGame.PointState;
import goGame.ServerPlayer;
import goGame.StoneColor;
import goProtocol.ProtocolMessages;
import goServer.InputHandlerServer;

public class GameController {

	private List<InputHandlerServer> clients;
	private boolean whiteTurn;
	private Board board;

	public GameController() {
		board = new Board(19);
		clients = new ArrayList<>();
	}

	public boolean moveHandler(StoneColor color, int index) throws IOException, ClientUnavailableException {
		if (isValidMove(index)) {
			try {
				board.placeStoneFromIndex(index, color);
				return true;
			} finally {
				manageTurn(String.valueOf(index));
			}
		} else {
			return false;
		}
	}

	private void manageTurn(String lastMove) throws ClientUnavailableException {
		whiteTurn ^= true;
		if (whiteTurn) {
			clients.get(1).notifyTurn(lastMove);
		} else {
			clients.get(0).notifyTurn(lastMove);
		}
	}

	private boolean isValidMove(int move) {
		return board.validPointFromIndex(move);
	}

	public void readyForGame() throws IOException, ClientUnavailableException {
		if (clients.size() == 2) {
			clients.get(0).setPlayer(new ServerPlayer("Player 1", StoneColor.BLACK));
			clients.get(1).setPlayer(new ServerPlayer("Player 2", StoneColor.WHITE));
			for (InputHandlerServer client : clients) {
				client.beginGame();
			}
			manageTurn(Character.toString(ProtocolMessages.PASS));
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
