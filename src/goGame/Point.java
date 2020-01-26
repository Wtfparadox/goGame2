package goGame;

public class Point {
	private PointState state;
	private StoneColor color;
	private boolean isEdge;
	private boolean isCorner;

	public Point() {
		state = PointState.FREE;
		color = StoneColor.NONE;
	}

	public void setState(PointState fsArg) {
		state = fsArg;
	}

	public PointState getState() {
		return state;
	}

	public void setColor(StoneColor colorArg) {
		this.color = colorArg;
	}

	public StoneColor getColor() {
		return color;
	}

	public void setEdge() {
		this.isEdge = true;
	}

	public boolean getEdge() {
		return isEdge;
	}

	public void setCorner() {
		this.isCorner = true;
	}

	public boolean getCorner() {
		return isCorner;
	}

	public boolean isIdentical(Point point) {
		if (this.state == PointState.OCCUPIED) {
			return this.color == point.color && point.state == PointState.OCCUPIED;
		} else {
			return point.isFreePoint();
		}
	}

	public boolean isFreePoint() {
		return this.state != PointState.OCCUPIED;
	}

	@Override
	public String toString() {
		return "This intersection is " + state.toString() + " by " + color.toString();
	}
}
