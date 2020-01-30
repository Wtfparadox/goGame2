package goGUI;

import goBoard.Point;
import goBoard.PointState;
import goBoard.StoneColor;
import observers.BoardWatcher;
import observers.ObservableBoard;

public class GTG implements BoardWatcher {

	private GoGuiIntegrator gogui;

	public void initGUI(int boardSize) {
		gogui = new GoGuiIntegrator(false, false, boardSize);
		gogui.setBoardSize(boardSize);
		gogui.startGUI();
	}

	@Override
	public void update(ObservableBoard observable) {
		drawBoard(observable.getBoardMatrix());
	}

	public void drawBoard(Point[][] board) {
		gogui.clearBoard();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				Point p = board[i][j];
				if (p.getState() == PointState.OCCUPIED) {
					gogui.addStone(j, i, (p.getColor().equals(StoneColor.WHITE)));
				} else if (p.getState() == PointState.CONQUERED) {
					gogui.addAreaIndicator(j, i, (p.getColor().equals(StoneColor.WHITE)));
				}
			}
		}
	}

}
