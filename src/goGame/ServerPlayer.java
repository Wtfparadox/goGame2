package goGame;

import java.util.Scanner;

public class ServerPlayer extends Player {

	public ServerPlayer(String nameArg, StoneColor stoneArg) {
		super(nameArg, stoneArg);
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
