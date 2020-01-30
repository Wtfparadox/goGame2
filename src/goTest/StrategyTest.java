package goTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import goBoard.Board;
import goBoard.StoneColor;
import goPlayers.FreeFieldStrategy;

class StrategyTest {

	private Board board;
	private FreeFieldStrategy strategy;

	@BeforeEach
	void setUp() throws Exception {
		board = new Board(5);
		strategy = new FreeFieldStrategy();
	}

	@Test
	void testSize() {
		assertEquals(strategy.getFreeFields(board).size(), 25);
		board.placeStoneFromIndex(12, StoneColor.BLACK);
		assertEquals(strategy.getFreeFields(board).size(), 24);
	}

}
