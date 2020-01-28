package goTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import goGame.Board;
import goGame.CapturedRef;
import goGame.StoneColor;

class CapturedRefTest {
	private CapturedRef ref;
	private Board board;
	private StoneColor b;
	private StoneColor w;

	@BeforeEach
	void setUp() throws Exception {

		board = new Board(9);
		ref = new CapturedRef(board);
		b = StoneColor.BLACK;
		w = StoneColor.WHITE;
	}

	@Test
	void testCaptureScenario1() {
		board.placeStone(1, 2, StoneColor.BLACK);
		board.placeStone(2, 1, StoneColor.BLACK);
		board.placeStone(3, 2, StoneColor.BLACK);
		board.placeStone(2, 3, StoneColor.BLACK);

		board.placeStone(2, 2, StoneColor.WHITE);

		assertEquals(1, ref.capturedPoints(2, 2, board).size());
	}

	@Test
	void testCaptureScenario2() {
		board.placeStone(0, 0, w);
		board.placeStone(1, 0, w);
		board.placeStone(0, 1, w);

		board.placeStone(0, 2, b);
		board.placeStone(2, 0, b);
		board.placeStone(1, 1, b);

		assertEquals(3, ref.capturedPoints(0, 0, board).size());

	}

	@Test
	void testCaptureScenario3() {
		board.placeStone(2, 2, b);
		board.placeStone(3, 2, b);
		board.placeStone(3, 3, b);
		board.placeStone(2, 3, b);

		board.placeStone(1, 1, w);
		board.placeStone(2, 1, w);
		board.placeStone(1, 2, w);
		board.placeStone(1, 3, w);
		board.placeStone(3, 1, w);
		board.placeStone(1, 4, w);
		board.placeStone(4, 1, w);
		board.placeStone(4, 2, w);
		board.placeStone(2, 4, w);
		board.placeStone(4, 3, w);
		board.placeStone(3, 4, w);
		board.placeStone(4, 4, w);

		assertEquals(4, ref.capturedPoints(2, 2, board).size());

	}

	@Test
	void testCaptureScenario4() {
		board.placeStone(2, 2, w);
		board.placeStone(3, 2, w);
		board.placeStone(3, 3, w);
		board.placeStone(2, 3, w);
		board.placeStone(1, 1, w);
		board.placeStone(2, 1, w);
		board.placeStone(1, 2, w);
		board.placeStone(1, 3, w);
		board.placeStone(3, 1, w);
		board.placeStone(1, 4, w);
		board.placeStone(4, 1, w);
		board.placeStone(4, 2, w);
		board.placeStone(2, 4, w);
		board.placeStone(4, 3, w);
		board.placeStone(3, 4, w);
		board.placeStone(4, 4, w);

		assertEquals(0, ref.capturedPoints(2, 2, board).size());

	}

	@Test
	void captureBySuicideScenario1() {
		board.placeStone(1, 2, w);
		board.placeStone(2, 1, w);
		board.placeStone(1, 1, w);
		board.placeStone(1, 3, w);
		board.placeStone(3, 1, w);
		board.placeStone(2, 3, w);
		board.placeStone(3, 2, w);
		board.placeStone(3, 3, w);

		board.placeStone(2, 2, b);

		board.placeStone(0, 0, b);
		board.placeStone(1, 0, b);
		board.placeStone(0, 1, b);
		board.placeStone(2, 0, b);
		board.placeStone(0, 2, b);
		board.placeStone(3, 0, b);
		board.placeStone(0, 3, b);
		board.placeStone(4, 0, b);
		board.placeStone(0, 4, b);
		board.placeStone(1, 4, b);
		board.placeStone(4, 1, b);
		board.placeStone(2, 4, b);
		board.placeStone(4, 2, b);
		board.placeStone(3, 4, b);
		board.placeStone(4, 3, b);
		board.placeStone(4, 4, b);

		assertEquals(8, ref.capturedPoints(2, 2, board).size());
	}

}
