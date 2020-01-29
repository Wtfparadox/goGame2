package observers;

import goBoard.Point;

public interface ObservableBoard {

	public Point[][] getBoardMatrix();

	public void addObserver(BoardWatcher observer);

	public void removeObserver(BoardWatcher observer);
}
