package goController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import goGUI.GTG;
import goGame.Board;
import goGame.CapturedRef;
import goGame.HumanPlayer;
import goGame.Player;
import goGame.StoneColor;

public class GoTUI implements Runnable {
	private GTG gtg;
	private Player[] players;
	private BufferedReader in;
	private Board board;
	private CapturedRef ref;

	public GoTUI(GTG gtg, Player p1, Player p2) {
		this.gtg = gtg;
		players = new Player[2];
		this.players[0] = p1;
		this.players[1] = p2;
		in = new BufferedReader(new InputStreamReader(System.in));
		this.board = new Board(5, gtg);
		ref = new CapturedRef(board);
	}

	public void run() {
		int i = 0;
		int row = 0;
		int col = 0;
		while (true) {
			Player p = players[i % 2];
			System.out.println(p.getName() + " turn is now");
			try {
				row = readInput("row");
				col = readInput("col");
			} catch (IOException e) {
				e.printStackTrace();
			}
			p.makeMove(row, col, board);
			board.placeStone(row, col, p.getColor());
			List<Integer[]> points = ref.capturedPoints(row, col, board);
			if (!points.isEmpty()) {
				board.removeStone(points, ref.getListColor(), ref.getListState());
			}
			i++;
		}
	}

	private void boardAttempt() {
		StoneColor w = StoneColor.WHITE;
		StoneColor b = StoneColor.BLACK;

//		board.placeStone(1, 2, StoneColor.BLACK);
//		board.placeStone(2, 1, StoneColor.BLACK);
//		board.placeStone(3, 2, StoneColor.BLACK);
//		board.placeStone(2, 3, StoneColor.BLACK);

//		board.placeStone(0, 0, w);
//		board.placeStone(1, 0, w);
//		board.placeStone(0, 1, w);

//		board.placeStone(0, 2, b);
		board.placeStone(2, 0, b);
		board.placeStone(1, 1, b);

//		board.placeStone(1, 2, w);
//		board.placeStone(2, 1, w);
//		board.placeStone(1, 1, b);
//		board.placeStone(1, 3, b);
//		board.placeStone(3, 1, b);
//		board.placeStone(2, 3, w);
//		board.placeStone(3, 2, w);
//		board.placeStone(3, 3, b);
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

	public int readInput(String requiredVal) throws NumberFormatException, IOException {
		System.out.println("Choose " + requiredVal);
		return Integer.parseInt(in.readLine());
	}

	public static void main(String[] a) {
		GoTUI tui = new GoTUI(new GTG(), new HumanPlayer("Black", StoneColor.BLACK),
				new HumanPlayer("White", StoneColor.WHITE));
		tui.gtg.initGUI(5);
		tui.boardAttempt();
		new Thread(tui).start();
	}
}
