package goGame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HumanPlayer extends Player {

	public HumanPlayer(String nameArg, StoneColor colorArg) {
		super(nameArg, colorArg);
	}

	public int determineMove(Board board) {
		System.out.println(this.getName() + " playing with " + this.getColor().toString() + " it's your turn.");
		int row = 0;
		int col = 0;
		try {
			row = readInput("row");
			col = readInput("col");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return row * board.getSize() + col;
	}

	public int readInput(String requiredVal) throws NumberFormatException, IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Choose " + requiredVal);
		return Integer.parseInt(in.readLine());
	}

}
