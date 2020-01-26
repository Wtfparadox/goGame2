package goTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import goGame.Board;
import goGame.Point;

class BoardTest {

	private Board board;

	@BeforeEach
	void setUp() throws Exception {
		board = new Board(5, null);
	}

	@Test
	void testCorners() {
		Point[][] testMatrix = board.getBoardMatrix();
		assertTrue(testMatrix[0][0].getCorner());
		assertTrue(testMatrix[0][board.getSize() - 1].getCorner());
		assertTrue(testMatrix[board.getSize() - 1][0].getCorner());
		assertTrue(testMatrix[board.getSize() - 1][testMatrix.length - 1].getCorner());

		assertFalse(testMatrix[3][3].getCorner());
		assertFalse(testMatrix[0][3].getCorner());
	}

	@Test
	void testEdges() {
		Point[][] testMatrix = board.getBoardMatrix();
		assertTrue(testMatrix[0][2].getEdge());
		assertTrue(testMatrix[0][board.getSize() - 2].getEdge());
		assertTrue(testMatrix[board.getSize() - 1][board.getSize() - 2].getEdge());
		assertTrue(testMatrix[board.getSize() - 2][board.getSize() - 1].getEdge());

		assertFalse(testMatrix[3][3].getEdge());
		assertFalse(testMatrix[1][2].getEdge());
	}

	@Test
	void testIsWithinBoardDim() {
		Point[][] testMatrix = board.getBoardMatrix();
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
