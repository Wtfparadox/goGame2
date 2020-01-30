package goPlayers;

import goBoard.Board;
import goBoard.StoneColor;
import goExceptions.ConnectionLostException;

public abstract class Player {
	protected String name;
	protected StoneColor stone;
	protected boolean turn;

	public Player(String nameArg) {
		name = nameArg;
	}

	public void setColor(StoneColor color) {
		this.stone = color;
	}

	public String getName() {
		return name;
	}

	public StoneColor getColor() {
		return stone;
	}

	public abstract int handleTurn(Board board) throws ConnectionLostException;

}
