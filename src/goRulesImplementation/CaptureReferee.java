package goRulesImplementation;

import java.util.ArrayList;
import java.util.List;

import goBoard.Board;
import goBoard.Point;
import goBoard.PointState;
import goBoard.StoneColor;

public class CaptureReferee extends Referee {

	private List<Integer> stonesToBeCaptured = new ArrayList<>();
	private List<Integer> libertiesToBeCaptured = new ArrayList<>();
	private StoneColor listColor;
	private PointState listState;

	public CaptureReferee(Board boardArg) {
		super(boardArg);
	}

	public List<Integer> getStoneList(int startPoint) {
		setCapturedListProperties(stonesToBeCaptured, startPoint);
		return stonesToBeCaptured;
	}

	public List<Integer> getLibertyList(int startPoint) {
		setCapturedListProperties(libertiesToBeCaptured, startPoint);
		return libertiesToBeCaptured;
	}

	public void markPointsForRemoval(int index) {
		clearLists();
		capturedPoints(index);
	}

	public void clearLists() {
		stonesToBeCaptured.clear();
		libertiesToBeCaptured.clear();
	}

	public StoneColor getListColor() {
		return listColor;
	}

	public PointState getListState() {
		return listState;
	}

	private void capturedPoints(int index) {
		Point startingPointGroupFormation = board.getPointFromIndex(index);
		if (startingPointGroupFormation.isFreePoint()) {
			findCapturedFields(new LibertyGroupFormer(index, board));
		} else {
			findCapturedStones(index, startingPointGroupFormation);
		}
	}

	public List<Integer> findCapturedFields(GroupFormer group) {
		group.startRecursiveGroupForming();
		return group.getGroup();
	}

	private void findCapturedStones(int index, Point initialPoint) {
		List<Integer> stoneChain = findCapturedFields(new StoneGroupFormer(index, board));
		List<Integer> coordinates = board.getNeighborCoordinates(index);
		List<Integer> neighborStoneChain = new ArrayList<>();

		if (stoneChain.isEmpty()) {
			for (Integer c : coordinates) {
				if (!libertiesToBeCaptured.contains(c) && board.getPointFromIndex(c).isFreePoint()) {
					libertiesToBeCaptured.addAll(findCapturedFields(new LibertyGroupFormer(c, board)));
				} else if (!board.getPointFromIndex(c).isFreePoint()) {
					neighborStoneChain.addAll(findCapturedFields(new StoneGroupFormer(c, board)));
				}
			}
		} else {
			for (Integer c : coordinates) {
				if (!neighborStoneChain.contains(c)) {
					neighborStoneChain.addAll(findCapturedFields(new StoneGroupFormer(c, board)));
				}
			}
		}

		if (stoneChain.isEmpty()) {
			stonesToBeCaptured = neighborStoneChain;
		} else if (neighborStoneChain.isEmpty()) {
			stonesToBeCaptured = stoneChain;
		} else {
			stonesToBeCaptured = neighborStoneChain;
		}
	}

	private void setCapturedListProperties(List<Integer> list, int index) {
		if (!list.isEmpty()) {
			Point listPoint = board.getPointFromIndex(list.get(0));
			StoneColor color = listPoint.getColor();
			PointState state = listPoint.getState();
			if (state == PointState.CONQUERED) {
				listColor = StoneColor.NONE;
				listState = PointState.FREE;
			} else if (state == PointState.OCCUPIED) {
				listColor = color.other();
				listState = PointState.CONQUERED;
			} else {
				listColor = board.getPointFromIndex(index).getColor();
				listState = PointState.CONQUERED;
			}

		}
	}

}
