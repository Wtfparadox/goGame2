package goGame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import goBoard.StoneColor;
import goExceptions.ConnectionLostException;
import goExceptions.FormerBoardException;
import goPlayers.ServerPlayer;
import goProtocol.ProtocolMessages;
import goServer.InputHandlerServer;

public class ServerGame extends Game {

	private static final String PASS = Character.toString(ProtocolMessages.PASS);

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
		updateBoardConfiguration(move, color);
		lastMove = currentMove;
		manageTurn(lastMove);
	}

	public void doPassMove() throws ConnectionLostException {
		lastMove = PASS;
		manageTurn(lastMove);
	}

	public boolean gameOver() {
		return lastMove.contentEquals(PASS) && currentMove.contentEquals(PASS);
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
			manageTurn(PASS);
		}
	}

	public void gameHasEnded(char reason) {
		for (InputHandlerServer client : clients) {
			try {
				client.endGame(reason);
			} catch (ConnectionLostException e) {
				e.printStackTrace();
			}
		}
	}

	public void rematch(boolean doRematch) throws IOException, ConnectionLostException {
		if (doRematch) {
			readyForGame();
		}
	}

	public void addInputHandler(InputHandlerServer handler) {
		clients.add(handler);
	}

}
