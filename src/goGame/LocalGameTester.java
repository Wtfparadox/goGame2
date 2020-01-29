package goGame;

import java.util.TreeSet;

import goBoard.Board;
import goBoard.PointState;
import goBoard.StoneColor;
import goExceptions.ConnectionLostException;
import goGUI.GTG;
import goPlayers.Player;
import goPlayers.ServerPlayer;
import goRulesImplementation.CaptureReferee;

public class LocalGameTester implements Runnable {
	private GTG gtg;
	private Player[] players;
	private Board board;
	private CaptureReferee ref;

	public LocalGameTester(GTG gtg, Player p1, Player p2) {
		this.gtg = gtg;
		players = new Player[2];
		this.players[0] = p1;
		this.players[1] = p2;
		this.board = new Board(5);
		board.addObserver(this.gtg);
		ref = new CaptureReferee(board);
	}

	public void run() {
		int i = 0;
		while (true) {
			Player p = players[i % 2];
			int index = 0;
			try {
				index = p.handleTurn(board);
			} catch (ConnectionLostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			processMove(index);
			i++;
		}
	}

	private void processMove(int index) {
		TreeSet<Integer> set1 = ref.getAndSetLibertySet(index);
		for (Integer i : set1) {
			System.out.println(board.getPointFromIndex(i).getColor());
			System.out.println(i);
		}
		if (!set1.isEmpty()) {
			StoneColor color = ref.getListColor();
			PointState state = ref.getListState();
			if (state == PointState.CONQUERED && color == StoneColor.NONE) {
				color = board.getPointFromIndex(index).getColor();
			}
			board.removeStoneFromIndex(set1, color, state);
		}
		TreeSet<Integer> set2 = ref.getAndSetStoneSet(index);
		if (!set2.isEmpty()) {
			StoneColor color = ref.getListColor();
			PointState state = ref.getListState();
			if (state == PointState.CONQUERED && color == StoneColor.NONE) {
				System.out.println("reached");
				color = board.getPointFromIndex(index).getColor();
			}
			board.removeStoneFromIndex(set2, color, state);
		}
	}

	private void boardAttempt() {
		StoneColor w = StoneColor.WHITE;
		StoneColor b = StoneColor.BLACK;

		board.placeStoneFromIndex(0, b);
		board.placeStoneFromIndex(1, b);
		board.placeStoneFromIndex(2, b);
		board.placeStoneFromIndex(3, b);
		board.placeStoneFromIndex(5, b);
		board.placeStoneFromIndex(8, b);
		board.placeStoneFromIndex(10, b);
		board.placeStoneFromIndex(11, b);
		board.placeStoneFromIndex(12, b);
		board.placeStoneFromIndex(14, b);
//		board.placeStoneFromIndex(18, b);

		board.placeStoneFromIndex(13, w);

//		board.placeStone(1, 2, w);
//		board.placeStone(2, 1, w);
//		board.placeStone(1, 1, w);
//		board.placeStone(1, 3, w);
//		board.placeStone(3, 1, w);
//		board.placeStone(2, 3, w);
//		board.placeStone(3, 2, w);
//		board.placeStone(3, 3, w);
//
////		board.placeStone(2, 2, b);
//
//		board.placeStone(0, 0, b);
//		board.placeStone(1, 0, b);
//		board.placeStone(0, 1, b);
//		board.placeStone(2, 0, b);
//		board.placeStone(0, 2, b);
//		board.placeStone(3, 0, b);
//		board.placeStone(0, 3, b);
//		board.placeStone(4, 0, b);
//		board.placeStone(0, 4, b);
//		board.placeStone(1, 4, b);
//		board.placeStone(4, 1, b);
//		board.placeStone(2, 4, b);
//		board.placeStone(4, 2, b);
//		board.placeStone(3, 4, b);
//		board.placeStone(4, 3, b);
//		board.placeStone(4, 4, b);
	}

	public static void main(String[] a) {
		LocalGameTester tui = new LocalGameTester(new GTG(), new ServerPlayer("Black", StoneColor.BLACK),
				new ServerPlayer("White", StoneColor.WHITE));
		tui.gtg.initGUI(5);
		tui.boardAttempt();
		new Thread(tui).start();
	}
}
