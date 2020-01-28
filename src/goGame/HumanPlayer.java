package goGame;

import java.util.Scanner;

public class HumanPlayer extends Player {

	public HumanPlayer(String nameArg, StoneColor colorArg) {
		super(nameArg, colorArg);
	}

	@Override
	public int determineMove(Board board) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("your move please");
		int input = scanner.nextInt();
		board.placeStoneFromIndex(input, this.stone);
		return input;
	}

}
