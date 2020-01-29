package goGame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import goBoard.Point;
import goBoard.PointState;
import goBoard.StoneColor;
import goExceptions.ConnectionLostException;
import goExceptions.FormerBoardException;
import goPlayers.ServerPlayer;
import goProtocol.ProtocolMessages;
import goServer.InputHandlerServer;

public class ServerGame extends Game {

	private static final String PASSSTRING = Character.toString(ProtocolMessages.PASS);

	private List<InputHandlerServer> clients;
	private boolean whiteTurn;
	private String lastMove, currentMove;

	public ServerGame(int boardDimension) {
		super(boardDimension);
		clients = new ArrayList<>();
		lastMove = "";
	}

	public void setCurrentMove(String move) {
		currentMove = move;
	}

	public void doBoardMove(StoneColor color, String index)
			throws IOException, FormerBoardException, ConnectionLostException {
		int move = Integer.parseInt(index);
		checkBoardConfiguration(move, color);
		lastMove = currentMove;
		manageTurn(lastMove);
	}

	public void doPassMove() throws ConnectionLostException {
		lastMove = PASSSTRING;
		manageTurn(lastMove);
	}

	public boolean gameOver() {
		return lastMove.contentEquals(PASSSTRING) && currentMove.contentEquals(PASSSTRING);
	}

	private void manageTurn(String lastMove) throws ConnectionLostException {
		whiteTurn ^= true;
		if (whiteTurn) {
			clients.get(1).notifyTurn(lastMove);
		} else {
			clients.get(0).notifyTurn(lastMove);
		}
	}

	public void readyForGame() throws IOException, ConnectionLostException {
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
