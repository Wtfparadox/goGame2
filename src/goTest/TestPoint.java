package goTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import goBoard.Point;
import goBoard.PointState;
import goBoard.StoneColor;

class TestPoint {
	private Point pointA, pointB, pointC;
	private StoneColor b, w;
	private PointState o, c;

	@BeforeEach
	void setUp() throws Exception {
		pointA = new Point();
		pointB = new Point();
		pointC = new Point();
		b = StoneColor.BLACK;
		w = StoneColor.WHITE;
		o = PointState.OCCUPIED;
		c = PointState.CONQUERED;
	}

	@Test
	void testIsIdentical() {
		pointA.setColor(b);
		pointA.setState(o);
		pointB.setColor(b);
		pointB.setState(o);
		pointC.setColor(w);
		pointC.setState(c);

		assertTrue(pointA.isIdentical(pointB));
		assertFalse(pointB.isIdentical(pointC));

	}

}
