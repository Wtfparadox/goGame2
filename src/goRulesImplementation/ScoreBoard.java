package goRulesImplementation;

import goBoard.Board;
import goBoard.Point;
import goBoard.PointState;
import goBoard.StoneColor;

public class ScoreBoard {
	private double blackScore;
	private double whiteScore;
	private double komi;

	public ScoreBoard(double komiArg, Board boardArg) {
		komi = komiArg;
		resetScore();
	}

	public void calculateScore(Point[][] board) {
		resetScore();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				Point p = board[i][j];
				if (p.getState() == PointState.CONQUERED) {
					if (p.getColor() == StoneColor.BLACK) {
						blackScore++;
					} else {
						whiteScore++;
					}
				}
			}
		}
	}

	private void resetScore() {
		blackScore = 0.0;
		whiteScore = komi;
	}

	public double getWhiteScore() {
		return whiteScore;
	}

	public double getBlackScore() {
		return blackScore;
	}

}
