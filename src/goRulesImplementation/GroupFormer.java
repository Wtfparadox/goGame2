package goRulesImplementation;

import java.util.ArrayList;
import java.util.List;

import goBoard.Board;
import goBoard.Point;
import goBoard.PointState;
import goBoard.StoneColor;

public abstract class GroupFormer {

	protected List<Integer> pointGroup;
	protected StoneColor color;
	protected Board board;
	protected Point initialPoint;
	protected int index;

	public GroupFormer(int boardIndex, Point initialPoint, Board board) {
		pointGroup = new ArrayList<>();
		this.board = board;
		this.initialPoint = initialPoint;
		index = boardIndex;
	}

	public abstract void formGroup(int index);

	public List<Integer> getGroup() {
		return pointGroup;
	}

	public void startRecursiveGroupForming() {
		formGroup(index);
	}

	public StoneColor getColor() {
		return initialPoint.getColor();
	}

	public PointState getState() {
		return initialPoint.getState();
	}

}
