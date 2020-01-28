package goGame;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class CapturedRefV2 extends Referee {

	private TreeSet<Integer> fieldsToBeCaptured;
	private TreeSet<Integer> stoneSet;
	private TreeSet<Integer> libertySet;

	private List<Integer> stonesToBeCaptured = new ArrayList<>();
	private List<Integer> libertiesToBeCaptured = new ArrayList<>();
	private StoneColor listColor;
	private PointState listState;

	public CapturedRefV2(Board boardArg) {
		super(boardArg);
	}

//	public Set<Integer> getFields(int index) {
//		getAndSetStoneSet(index);
//		getAndSetLibertySet(index);
//		stoneSet.addAll(libertySet);
//		fieldsToBeCaptured = stoneSet;
//		this.setCapturedListProperties(fieldsToBeCaptured);
//		return fieldsToBeCaptured;
//	}

	public TreeSet<Integer> getAndSetStoneSet(int index) {
		capturedPoints(index);
		stoneSet = uniquePointsForRemoval(stonesToBeCaptured);
		setCapturedListProperties(stoneSet);
		return stoneSet;
	}

	public TreeSet<Integer> getAndSetLibertySet(int index) {
		capturedPoints(index);
		libertySet = uniquePointsForRemoval(libertiesToBeCaptured);
		setCapturedListProperties(libertySet);
		return libertySet;
	}

	public StoneColor getListColor() {
		return listColor;
	}

	public PointState getListState() {
		return listState;
	}

	private TreeSet<Integer> uniquePointsForRemoval(List<Integer> list) {
		TreeSet<Integer> set = new TreeSet<>();
		list.size();
		for (Integer field : list) {
			set.add(field);
		}
		return set;
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
		List<Integer> neighborStoneChain = new ArrayList<>();
		List<Integer> neighborLibertyChain = new ArrayList<>();
		if (stoneChain.isEmpty()) {
			for (Integer c : coordinates) {
				Point neighborPoint = board.getPointFromIndex(c);
//				System.out.println(neighborPoint.getColor() + " at coo " + c);
				if (!neighborLibertyChain.contains(c) && neighborPoint.isFreePoint()) {
					neighborLibertyChain.addAll(findCapturedFields(new LibertyGroupFormer(c, neighborPoint, board)));
				} else if (!neighborPoint.isFreePoint()) {
					neighborStoneChain.addAll(findCapturedFields(new StoneGroupFormer(c, neighborPoint, board)));
				}
			}
		} else {
			for (Integer c : coordinates) {
				Point neighborPoint = board.getPointFromIndex(c);
				if (!neighborStoneChain.contains(c)) {
					neighborStoneChain.addAll(findCapturedFields(new StoneGroupFormer(c, neighborPoint, board)));
				}
			}
		}
		if (stoneChain.isEmpty()) {
			libertiesToBeCaptured = neighborLibertyChain;
			stonesToBeCaptured = neighborStoneChain;
			return neighborLibertyChain;
		} else if (neighborStoneChain.isEmpty()) {
			stonesToBeCaptured = stoneChain;
			return stoneChain;
		} else {
			stonesToBeCaptured = neighborStoneChain;
			return neighborStoneChain;
		}
	}

	private void setCapturedListProperties(TreeSet<Integer> set) {
		if (!set.isEmpty()) {
			Point listPoint = board.getPointFromIndex(set.first());
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
