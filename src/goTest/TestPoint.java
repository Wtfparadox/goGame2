package goTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import goGame.Point;
import goGame.PointState;
import goGame.StoneColor;

class TestPoint {
	private Point pointA, pointB, pointC;
	private StoneColor b, w, n;
	private PointState o, c, f;

	@BeforeEach
	void setUp() throws Exception {
		pointA = new Point();
		pointB = new Point();
		pointC = new Point();
		b = StoneColor.BLACK;
		w = StoneColor.WHITE;
		n = StoneColor.NONE;
		o = PointState.OCCUPIED;
		c = PointState.CONQUERED;
		f = PointState.FREE;
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
