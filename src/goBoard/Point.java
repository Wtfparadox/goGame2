package goBoard;

public class Point {
	private PointState state;
	private StoneColor color;

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
