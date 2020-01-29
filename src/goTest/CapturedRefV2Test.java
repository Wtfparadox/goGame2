package goTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import goBoard.Board;
import goBoard.StoneColor;
import goRulesImplementation.CaptureReferee;

class CapturedRefV2Test {
	private CaptureReferee ref;
	private Board board;
	private StoneColor b;
	private StoneColor w;
	private int dim;

	@BeforeEach
	void setUp() throws Exception {
		dim = 5;
		board = new Board(dim);
		ref = new CaptureReferee(board);
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

		assertEquals(1, ref.getStoneList(12).size());

	}

	@Test
	void testCaptureScenario2() {
		board.placeStone(0, 0, w);
		board.placeStone(1, 0, w);
		board.placeStone(0, 1, w);

		board.placeStone(0, 2, b);
		board.placeStone(2, 0, b);
		board.placeStone(1, 1, b);

		for (Integer i : ref.getStoneList()) {
			System.out.println(board.getPointFromIndex(i).getColor());
		}
		System.out.println("");

		assertEquals(3, ref.getStoneList().size());

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

		assertEquals(4, ref.getStoneList().size());

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

		for (Integer i : ref.getStoneList()) {
			System.out.println(board.getPointFromIndex(i).getColor());
		}

		assertEquals(0, ref.getStoneList().size());
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

		for (Integer i : ref.getStoneList()) {
			System.out.println(board.getPointFromIndex(i).getColor());
		}
		System.out.println("");

		assertEquals(8, ref.getStoneList().size());
	}

	@Test
	void capturedLiberties1() {
		board.placeStone(0, 2, b);
		board.placeStone(2, 0, b);
		board.placeStone(1, 1, b);

		board.placeStone(4, 4, w);

		for (Integer i : ref.getLibertyList()) {
			System.out.println(board.getPointFromIndex(i).getColor());
		}
		System.out.println("");

		assertEquals(3, ref.getLibertyList().size());
	}

	@Test
	void capturedLiberties2() {
		board.placeStoneFromIndex(0, b);
		board.placeStoneFromIndex(1, b);
		board.placeStoneFromIndex(2, b);
		board.placeStoneFromIndex(3, b);
		board.placeStoneFromIndex(5, b);
		board.placeStoneFromIndex(8, b);
		board.placeStoneFromIndex(10, b);
		board.placeStoneFromIndex(11, b);
		board.placeStoneFromIndex(12, b);
		board.placeStoneFromIndex(13, b);

		ref.markPointsForRemoval(5);
		assertEquals(2, ref.getLibertyList().size());
		assertEquals(0, ref.getStoneList().size());

		ref.markPointsForRemoval(12);

		assertEquals(15, ref.getLibertyList().size());

		board.placeStoneFromIndex(20, w);

		ref.markPointsForRemoval(12);
		assertEquals(2, ref.getLibertyList().size());
	}

	@Test
	void capturedLiberties3() {
		board.placeStoneFromIndex(2, w);
		board.placeStoneFromIndex(6, w);
		board.placeStoneFromIndex(12, w);
		board.placeStoneFromIndex(18, w);
		board.placeStoneFromIndex(14, w);

		board.placeStoneFromIndex(0, b);

		ref.markPointsForRemoval(6);

		assertEquals(6, ref.getLibertyList().size());

	}

	@Test
	void captureStoneAndLiberty1() {
		board.placeStoneFromIndex(0, b);
		board.placeStoneFromIndex(1, b);
		board.placeStoneFromIndex(2, b);
		board.placeStoneFromIndex(3, b);
		board.placeStoneFromIndex(5, b);
		board.placeStoneFromIndex(8, b);
		board.placeStoneFromIndex(10, b);
		board.placeStoneFromIndex(11, b);
		board.placeStoneFromIndex(12, b);
		board.placeStoneFromIndex(14, b);
		board.placeStoneFromIndex(18, b);

		board.placeStoneFromIndex(13, w);

		ref.markPointsForRemoval(18);

		assertEquals(9, ref.getLibertyList().size());
		assertEquals(1, ref.getStoneList().size());

	}

}
