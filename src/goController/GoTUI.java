package goController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Set;

import goGUI.GTG;
import goGame.Board;
import goGame.CapturedRefV2;
import goGame.Player;
import goGame.PointState;
import goGame.ServerPlayer;
import goGame.StoneColor;

public class GoTUI implements Runnable {
	private GTG gtg;
	private Player[] players;
	private BufferedReader in;
	private Board board;
	private CapturedRefV2 ref;

	public GoTUI(GTG gtg, Player p1, Player p2) {
		this.gtg = gtg;
		players = new Player[2];
		this.players[0] = p1;
		this.players[1] = p2;
		in = new BufferedReader(new InputStreamReader(System.in));
		this.board = new Board(5);
		board.addObserver(this.gtg);
		ref = new CapturedRefV2(board);
	}

	public void run() {
		int i = 0;
		int row = 0;
		int col = 0;
		while (true) {
			Player p = players[i % 2];
			int index = p.determineMove(board);
			Set<Integer> points = ref.getFields(index);
			if (!points.isEmpty()) {
				StoneColor color = ref.getListColor();
				PointState state = ref.getListState();
				if (state == PointState.CONQUERED && color == StoneColor.NONE) {
					color = board.getPointFromIndex(index).getColor();
				}
				board.removeStoneFromIndex(points, color, state);
			}
			i++;
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
//		board.placeStoneFromIndex(13, b);
	}

	public static void main(String[] a) {
		GoTUI tui = new GoTUI(new GTG(), new ServerPlayer("Black", StoneColor.BLACK),
				new ServerPlayer("White", StoneColor.WHITE));
		tui.gtg.initGUI(5);
		tui.boardAttempt();
		new Thread(tui).start();
	}
}
