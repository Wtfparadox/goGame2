package goClient;

import goGUI.GTG;
import goGame.Board;
import goGame.HumanPlayer;
import goGame.Player;
import goGame.StoneColor;

public class ClientGame {

	private Board board;
	private Player player;
	private InputHandlerClient handler;

	public ClientGame(char color, InputHandlerClient handler) {
		player = new HumanPlayer("Thomas", stringToStoneColor(color));
		this.handler = handler;
		initGame(19);
	}

	private void initGame(int dim) {
		board = new Board(dim);
		GTG gui = new GTG();
		board.addObserver(gui);
		gui.initGUI(dim);
	}

	public void processOpponentsMove(int otherPlayerMove) {
		board.placeStoneFromIndex(otherPlayerMove, player.getColor().other());
	}

	public int processOwnMove() {
		int ownMove = player.determineMove(board);
		board.placeStoneFromIndex(ownMove, player.getColor());
		return ownMove;
	}

	private StoneColor stringToStoneColor(char color) {
		if (color == 'B') {
			return StoneColor.BLACK;
		} else {
			return StoneColor.WHITE;
		}
	}

}
