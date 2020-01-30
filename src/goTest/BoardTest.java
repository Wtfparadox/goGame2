package goTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import goBoard.Board;

class BoardTest {

	private Board board;

	@BeforeEach
	void setUp() throws Exception {
		board = new Board(5);
	}

	@Test
	void testIsWithinBoardDim() {
		assertFalse(board.validPoint(-1, 0));
		assertFalse(board.validPoint(board.getSize(), 0));
		assertTrue(board.validPoint(board.getSize() - 1, board.getSize() - 1));
		assertFalse(board.validPoint(0, board.getSize()));
		assertFalse(board.validPoint(board.getSize(), board.getSize()));

	}

	@Test
	void testPlaceStone() {

	}

	@Test
	void testRemoveStone() {

	}

}
