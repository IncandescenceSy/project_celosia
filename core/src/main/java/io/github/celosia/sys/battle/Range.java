package io.github.celosia.sys.battle;

import java.util.ArrayList;
import java.util.List;

import static io.github.celosia.sys.battle.PosLib.getAcross;
import static io.github.celosia.sys.battle.PosLib.getTeamWithout;
import static io.github.celosia.sys.battle.PosLib.getUpDown;

// Range that skills can have
public class Range {

	private final String name;
	private final int rangeVertical;
	private final Side side;
	private final boolean canTargetSelf;
	private final int targetCount;
	private final Target[] targets;

	Range(String name, int rangeVertical, Side side, boolean canTargetSelf, int targetCount, Target... targets) {
		this.name = name;
		this.rangeVertical = rangeVertical;
		this.side = side;
		this.canTargetSelf = canTargetSelf;
		this.targetCount = targetCount;
		this.targets = targets;
	}

	Range(String name, int rangeVertical, Side side, boolean canTargetSelf, Target... targets) {
		this(name, rangeVertical, side, canTargetSelf, 1, targets);
	}

	Range(String name, int rangeVertical, Side side, int targetCount, Target... targets) {
		this(name, rangeVertical, side, false, targetCount, targets);
	}

	Range(String name, int rangeVertical, Side side, Target... targets) {
		this(name, rangeVertical, side, false, 1, targets);
	}

	public String getName() {
		return name;
	}

	public int getRangeVertical() {
		return rangeVertical;
	}

	public Side getSide() {
		return side;
	}

	public boolean isCanTargetSelf() {
		return canTargetSelf;
	}

	public int getTargetCount() {
		return targetCount;
	}

	public Target[] getTargets() {
		return targets;
	}

	public List<Integer> getTargetPositions(int posSelf, int posTarget) {
		List<Integer> pos = new ArrayList<>();

		for (Target target : targets) {
			switch (target) {
				case Target.SELF :
					pos.add(posSelf);
					break;
				case Target.SELF_UP :
					pos.add(getUpDown(posSelf, -1));
					break;
				case Target.SELF_DOWN :
					pos.add(getUpDown(posSelf, 1));
					break;
				case Target.SELF_ACROSS :
					pos.add(getAcross(posSelf));
					break;
				case Target.SELF_ACROSS_UP :
					pos.add(getUpDown(getAcross(posSelf), -1));
					break;
				case Target.SELF_ACROSS_DOWN :
					pos.add(getUpDown(getAcross(posSelf), 1));
					break;
				case Target.SELF_TEAM :
					pos.addAll(getTeamWithout(posSelf));
					break;
				case Target.TARGET :
					pos.add(posTarget);
					break;
				case Target.TARGET_UP :
					pos.add(getUpDown(posTarget, -1));
					break;
				case Target.TARGET_DOWN :
					pos.add(getUpDown(posTarget, 1));
					break;
				case Target.TARGET_TEAM :
					pos.addAll(getTeamWithout(posTarget));
					break;
			}
		}

		return pos;
	}
}
