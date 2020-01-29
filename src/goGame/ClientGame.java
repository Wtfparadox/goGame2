package goGame;

import java.io.IOException;

import goClient.InputHandlerClient;
import goExceptions.ConnectionLostException;
import goGUI.GTG;
import goPlayers.Player;

public class ClientGame extends Game {

	private Player player;

	public ClientGame(int boardDimension, char color, InputHandlerClient handler, Player player) throws IOException {
		super(boardDimension);
		this.player = player;
		handler.setPlayer(player);
		initGame(5);
	}

	private void initGame(int dim) {
		GTG gui = new GTG();
		board.addObserver(gui);
		gui.initGUI(dim);
	}

	public void giveTurn() throws ConnectionLostException {
		int move = player.handleTurn(board);
		if (move >= 0) {
			board.placeStoneFromIndex(move, player.getColor());
			processMove(move);
		}
	}

	public void processOpponentsMove(int otherPlayerMove) {
		board.placeStoneFromIndex(otherPlayerMove, player.getColor().other());
		processMove(otherPlayerMove);
	}

//	public int processOwnMove() {
//		int ownMove = player.determineMove(board);
//		board.placeStoneFromIndex(ownMove, player.getColor());
//		return ownMove;
//	}

}
