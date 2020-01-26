package goController;

import java.util.ArrayList;
import java.util.List;

import goGame.Player;

public class Game {

	private List<Player> players;
	private String name;

	public Game() {
		players = new ArrayList<>();
	}

	public void addPlayer(Player p) {
		players.add(p);
	}

	public String getName() {
		if (players.isEmpty()) {
			return "Empty game";
		} else {
			return "";
		}
	}
}
