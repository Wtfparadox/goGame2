package goGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class CapturedRefV2 extends Referee {

	private TreeSet<Integer> fieldsToBeCaptured;
	private StoneColor listColor;
	private PointState listState;

	public CapturedRefV2(Board boardArg) {
		super(boardArg);
	}

	public Set<Integer> getFields(int index) {
		uniquePointsForRemoval(capturedPoints(index));
		this.setCapturedListProperties();
		return fieldsToBeCaptured;
	}

	public StoneColor getListColor() {
		return listColor;
	}

	public PointState getListState() {
		return listState;
	}

	private void uniquePointsForRemoval(List<Integer> list) {
		fieldsToBeCaptured = new TreeSet<>();
		for (Integer field : list) {
			fieldsToBeCaptured.add(field);
		}
	}

	private List<Integer> capturedPoints(int index) {
		Point initialPoint = board.getPointFromIndex(index);
		if (initialPoint.isFreePoint()) {
			return findCapturedFields(new LibertyGroupFormer(index, initialPoint, board));
		} else {
			return findCapturedStones(index, initialPoint);
		}
	}

	public List<Integer> findCapturedFields(GroupFormer group) {
		group.startRecursiveGroupForming();
		return group.getGroup();
	}

	private List<Integer> findCapturedStones(int index, Point initialPoint) {
		List<Integer> stoneChain = findCapturedFields(new StoneGroupFormer(index, initialPoint, board));
		List<Integer> coordinates = board.getNeighborCoordinates(index);
		List<Integer> nextChain = new ArrayList<>();
		if (stoneChain.isEmpty()) {
			for (Integer c : coordinates) {
				Point neighborPoint = board.getPointFromIndex(c);
				if (!nextChain.contains(c) && neighborPoint.isFreePoint()) {
					nextChain.addAll(findCapturedFields(new LibertyGroupFormer(c, neighborPoint, board)));
				}
			}
		} else {
			for (Integer c : coordinates) {
				Point neighborPoint = board.getPointFromIndex(c);
				if (!nextChain.contains(c)) {
					nextChain.addAll(findCapturedFields(new StoneGroupFormer(c, neighborPoint, board)));
				}
			}
		}
		if (nextChain.isEmpty()) {
			return stoneChain;
		} else {
			return nextChain;
		}
	}

	private void setCapturedListProperties() {
		if (!fieldsToBeCaptured.isEmpty()) {
			Point listPoint = board.getPointFromIndex(fieldsToBeCaptured.first());
			StoneColor color = listPoint.getColor();
			PointState state = listPoint.getState();
			System.out.println(color);
			System.out.println(state);
			if (state == PointState.CONQUERED) {
				listColor = StoneColor.NONE;
				listState = PointState.FREE;
			} else if (state == PointState.OCCUPIED) {
				listColor = color.other();
				listState = PointState.CONQUERED;
			} else {
				listColor = StoneColor.NONE;
				listState = PointState.CONQUERED;
			}

		}
	}

}
