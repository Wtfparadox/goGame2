package goBoard;

public enum StoneColor {
	BLACK, WHITE, NONE;

	public StoneColor other() {
		if (this.equals(BLACK)) {
			return WHITE;
		} else if (this.equals(WHITE)) {
			return BLACK;
		} else {
			return NONE;
		}
	}

}
