package goTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import goBoard.Board;
import goBoard.StoneColor;
import goRulesImplementation.GroupFormer;
import goRulesImplementation.LibertyGroupFormer;
import goRulesImplementation.StoneGroupFormer;

class GroupTest {

	private Board board;
	private int dim;
	private StoneColor b;
	private StoneColor w;
	private GroupFormer group;

	@BeforeEach
	void setUp() throws Exception {
		dim = 5;
		board = new Board(dim);
		w = StoneColor.WHITE;
		b = StoneColor.BLACK;
	}

	@Test
	void testCaptureScenario2() {
		board.placeStone(0, 0, w);
		board.placeStone(1, 0, w);
		board.placeStone(0, 1, w);

		board.placeStone(0, 2, b);
		board.placeStone(2, 0, b);
		board.placeStone(1, 1, b);

		group = new StoneGroupFormer(0, board.getPoint(0, 0), board);
		group.formGroup(0);
		assertEquals(group.getGroup().size(), 3);

	}

	@Test
	void testChain3() {
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

		group = new StoneGroupFormer(2 * dim + 2, board.getPoint(2, 2), board);
		group.formGroup(2 * dim + 2);
		assertEquals(group.getGroup().size(), 0);
	}

	@Test
	void testChain4() {
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

		group = new StoneGroupFormer(5, board.getPointFromIndex(5), board);
		group.formGroup(5);
		assertEquals(group.getGroup().size(), 0);

		group = new LibertyGroupFormer(6, board.getPointFromIndex(6), board);
		group.formGroup(6);
		assertEquals(group.getGroup().size(), 2);
		group = new LibertyGroupFormer(24, board.getPointFromIndex(24), board);
		group.formGroup(24);
		assertEquals(group.getGroup().size(), 13);
	}

	@Test
	void testLibertiesScenario() {
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

		board.placeStoneFromIndex(20, w);

		group = new StoneGroupFormer(5, board.getPointFromIndex(5), board);
		group.formGroup(5);
		assertEquals(group.getGroup().size(), 0);

		group = new LibertyGroupFormer(6, board.getPointFromIndex(6), board);
		group.formGroup(6);
		assertEquals(group.getGroup().size(), 2);
		group = new LibertyGroupFormer(24, board.getPointFromIndex(24), board);
		group.formGroup(24);
		assertEquals(group.getGroup().size(), 0);
	}

}
