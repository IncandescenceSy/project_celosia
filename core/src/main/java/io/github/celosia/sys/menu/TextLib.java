package io.github.celosia.sys.menu;

import io.github.celosia.sys.battle.Mod;
import io.github.celosia.sys.battle.Mult;
import io.github.celosia.sys.battle.Side;
import io.github.celosia.sys.battle.StageType;
import io.github.celosia.sys.battle.Stat;
import io.github.celosia.sys.battle.Unit;

import java.text.DecimalFormat;

import static io.github.celosia.sys.battle.PosLib.getSide;

public class TextLib {
	public static String tags = "{FADE}{SLIDE}";

	// Colors
	public static String c_ally = "[#2d74f5]";
	public static String c_ally_l = "[#528cf5]";
	public static String c_opp = "[#ff4545]";
	public static String c_opp_l = "[#ff6060]";
	public static String c_turn = "[#a034ff]";

	public static String c_pos = "[#00ff00]"; // Positive numbers
	public static String c_neg = "[#ff0000]"; // Negative numbers
	public static String c_num = "[#ffff00]"; // General numbers (turns, stacks)

	public static String c_hp = "[#1ae132]";
	public static String c_sp = "[#bb00ff]";
	public static String c_shield = "[#00ffff]";
	public static String c_bloom = "[#ff00ff]";

	public static String c_buff = "[#c6a1ff]";
	public static String c_skill = "[#95c9ff]";
	public static String c_passive = "[#c6a1ff]";
	public static String c_stat = "[#deff81]";
	public static String c_exp = "[#a034ff]"; // Calculated exponent parenthesis

	// Decimal formatter with up to 2 decimal places
	public static DecimalFormat df = new DecimalFormat("#.##");

	public static String formatName(String name, int pos, boolean possessive) {
		String suffix = (possessive) ? name.toLowerCase().endsWith("s") ? "'" : "'s" : "";
		String color = (getSide(pos) == Side.ALLY) ? c_ally : c_opp;
		return color + String.format("%s%s", name, suffix) + "[WHITE]";
	}

	public static String formatName(String name, int pos) {
		return formatName(name, pos, true);
	}

	// Returns the color a number should be displayed based on if it's positive or
	// negative
	public static String getColor(double num) {
		return (num > 0) ? c_pos : (num < 0) ? c_neg : c_num;
	}

	public static String getMultColor(int num, Mult multType) {
		String c1 = multType.isPositive() ? c_pos : c_neg;
		String c2 = multType.isPositive() ? c_neg : c_pos;

		return (num > 1000) ? c1 : (num < 1000) ? c2 : c_num;
	}

	public static String getExpColor(int num, Mult multType) {
		String c1 = multType.isPositive() ? c_pos : c_neg;
		String c2 = multType.isPositive() ? c_neg : c_pos;

		return (num > 100) ? c1 : (num < 100) ? c2 : c_num;
	}

	public static String getMultChangeColor(int num, Mult multType) {
		String c1 = multType.isPositive() ? c_pos : c_neg;
		String c2 = multType.isPositive() ? c_neg : c_pos;

		return (num > 0) ? c1 : (num < 0) ? c2 : c_num;
	}

	public static String getMultWithExpColor(double num, Mult multType) {
		String c1 = multType.isPositive() ? c_pos : c_neg;
		String c2 = multType.isPositive() ? c_neg : c_pos;

		return (num > 1) ? c1 : (num < 1) ? c2 : c_num;
	}

	public static String getMultWithExpChangeColor(double num, Mult multType) {
		String c1 = multType.isPositive() ? c_pos : c_neg;
		String c2 = multType.isPositive() ? c_neg : c_pos;

		return (num > 0) ? c1 : (num < 0) ? c2 : c_num;
	}

	public static String getModColor(int num, Mod modType) {
		String c1 = modType.isPositive() ? c_pos : c_neg;
		String c2 = modType.isPositive() ? c_neg : c_pos;

		return (num > 0) ? c1 : (num < 0) ? c2 : c_num;
	}

	public static String formatMod(int num, Mod modType) {
		return getModColor(num, modType) + ((num > 0) ? "+" : "") + num;
	}

	public static String getStatColor(long num, long base) {
		return (num > base) ? c_pos : (num < base) ? c_neg : c_num;
	}

	public static String getStageStatString(Unit unit, StageType stageType, int stageNew) {
		StringBuilder builder = new StringBuilder();

		builder.append("[WHITE] (");
		int statCount = stageType.getStats().length;
		for (int i = 0; i < statCount; i++) {
			Stat stat = stageType.getStats()[i];
			long statDefault = unit.getStatsDefault().getDisplayStat(stat);
			long statOld = unit.getDisplayStatWithStage(stat);
			long statNew = unit.getDisplayStatWithStage(stat, stageNew);
			long change = statNew - statOld;
			builder.append(c_stat).append(stat.getName()).append(" ").append(getStatColor(statOld, statDefault))
					.append(String.format("%,d", statOld)).append("[WHITE] → ")
					.append(getStatColor(statNew, statDefault)).append(String.format("%,d", statNew)).append("[WHITE]")
					.append("/").append(c_num).append(String.format("%,d", statDefault)).append("[WHITE] (")
					.append(getColor(change)).append((change >= 0) ? "+" : "")
					.append(String.format("%,d", (statNew - statOld))).append("[WHITE])");

			if (i == statCount - 1)
				builder.append(")");
			else
				builder.append(", ");
		}

		return builder.toString();
	}
}
