package io.github.celosia.sys.menu;

import io.github.celosia.sys.battle.Mod;
import io.github.celosia.sys.battle.Move;
import io.github.celosia.sys.battle.Mult;
import io.github.celosia.sys.battle.Side;
import io.github.celosia.sys.battle.Skill;
import io.github.celosia.sys.battle.StageType;
import io.github.celosia.sys.battle.Stat;
import io.github.celosia.sys.battle.Unit;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import static io.github.celosia.sys.battle.BattleControllerLib.battle;
import static io.github.celosia.sys.battle.PosLib.getSide;
import static io.github.celosia.sys.settings.Lang.lang;

public class TextLib {
	public static String tags = "{FADE}{SLIDE}";

	/// Colors
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
	public static String c_cd = "[#1898ff]"; // Cooldown

	/// Formatters
	// Decimal formatter for scientific notation
	public static DecimalFormat sf = new DecimalFormat("0.###E0");

	// Regional number formatter with up to 2 decimal places (set elsewhere)
	// todo use language setting
	public static NumberFormat rf = NumberFormat.getInstance(Locale.getDefault());

	public static String formatNum(long num) {
		int digitCount = String.valueOf(Math.abs(num)).length();

		// Use scientific notation
		// todo is this a good threshold?
		if (digitCount >= 9)
			return sf.format(num);
		else
			return rf.format(num);
	}

	public static String formatNum(double num) {
		int digitCount = String.valueOf(Math.abs(num)).length();

		// Use scientific notation
		if (digitCount >= 9)
			return sf.format(num);
		else
			return rf.format(num);
	}

	// todo other language support
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
	public static String getColor(long num) {
		return (num > 0) ? c_pos : (num < 0) ? c_neg : c_num;
	}

	public static String getSign(double num) {
		return (num > 0) ? "+" : "";
	}

	// Returns the color a mult should be based on its relation to 100% and whether
	// higher is better
	public static String getMultColor(int mult, Mult multType) {
		String c1; // Increased
		String c2; // Decreased

		if (multType.isPositive()) {
			c1 = c_pos;
			c2 = c_neg;
		} else {
			c1 = c_neg;
			c2 = c_pos;
		}

		return (mult > 1000) ? c1 : (mult < 1000) ? c2 : c_num;
	}

	// Returns the color a mult exponent should be based on its relation to 1,
	// whether higher is better, and whether mult is above or below 100%
	public static String getExpColor(int exp, int mult, Mult multType) {
		String c1;
		String c2;

		if (multType.isPositive()) {
			c1 = c_pos;
			c2 = c_neg;
		} else {
			c1 = c_neg;
			c2 = c_pos;
		}

		if (mult > 1000) {
			if (exp > 100)
				return c1;
			else if (exp < 100)
				return c2;
			else
				return c_num;
		} else if (mult < 1000) {
			if (exp > 100)
				return c2;
			else if (exp < 100)
				return c1;
			else
				return c_num;
		} else
			return c_num;
	}

	public static String getMultChangeColor(int multChange, Mult multType) {
		String c1;
		String c2;

		if (multType.isPositive()) {
			c1 = c_pos;
			c2 = c_neg;
		} else {
			c1 = c_neg;
			c2 = c_pos;
		}

		return (multChange > 0) ? c1 : (multChange < 0) ? c2 : c_num;
	}

	public static String getExpChangeColor(int expChange, int mult, Mult multType) {
		String c1;
		String c2;

		if (multType.isPositive()) {
			c1 = c_pos;
			c2 = c_neg;
		} else {
			c1 = c_neg;
			c2 = c_pos;
		}

		if (mult > 1000) {
			if (expChange > 0) {
				return c1;
			} else if (expChange < 0) {
				return c2;
			} else {
				return c_num;
			}
		} else if (mult < 1000) {
			if (expChange > 0) {
				return c2;
			} else if (expChange < 0) {
				return c1;
			} else {
				return c_num;
			}
		} else {
			return c_num;
		}
	}

	public static String getMultWithExpColor(double multWithExp, Mult multType) {
		String c1;
		String c2;

		if (multType.isPositive()) {
			c1 = c_pos;
			c2 = c_neg;
		} else {
			c1 = c_neg;
			c2 = c_pos;
		}

		return (multWithExp > 1) ? c1 : (multWithExp < 1) ? c2 : c_num;
	}

	public static String getMultWithExpChangeColor(double multWithExpChange, Mult multType) {
		String c1;
		String c2;

		if (multType.isPositive()) {
			c1 = c_pos;
			c2 = c_neg;
		} else {
			c1 = c_neg;
			c2 = c_pos;
		}

		return (multWithExpChange > 0) ? c1 : (multWithExpChange < 0) ? c2 : c_num;
	}

	public static String getModColor(int mod, Mod modType) {
		String c1;
		String c2;

		if (modType.isPositive()) {
			c1 = c_pos;
			c2 = c_neg;
		} else {
			c1 = c_neg;
			c2 = c_pos;
		}

		return (mod > 0) ? c1 : (mod < 0) ? c2 : c_num;
	}

	public static String formatMod(int mod, Mod modType) {
		return getModColor(mod, modType) + getSign(mod) + mod;
	}

	public static String getStatColor(long stat, long statBase) {
		return (stat > statBase) ? c_pos : (stat < statBase) ? c_neg : c_num;
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
			builder.append(lang.format("log.stage_stat", c_stat + stat.getName(),
					getStatColor(statOld, statDefault) + formatNum(statOld),
					getStatColor(statNew, statDefault) + formatNum(statNew), c_num + formatNum(statDefault),
					getColor(change) + ((change >= 0) ? "+" : "") + formatNum(statNew - statOld)));

			if (i == statCount - 1) {
				builder.append(")");
			} else {
				builder.append(", ");
			}
		}

		return builder.toString();
	}

	public static String getTriesToUseString(Move move) {
		Skill skill = move.skillInstance().getSkill();
		Unit self = move.self();

		String msg = lang.format("log.tries_to_use.1", formatName(self.getUnitType().name(), self.getPos(), false),
				c_skill + skill.getName());

		if (!skill.isRangeSelf()) {
			msg += lang.format("log.tries_to_use.2",
					formatName(battle.getUnitAtPos(move.targetPos()).getUnitType().name(), move.targetPos(), false));
		}

		return msg;
	}

	public static String mergeGlyphs(String glyphs) {
		return mergeDpadGlyphs(mergeDpadGlyphs(mergeDpadGlyphs(glyphs, "XB"), "PS"), "NSW");
	}

	public static String mergeDpadGlyphs(String glyphs, String type) {
		// Replace Dpad UpDownLeftRight With Dpad
		glyphs = glyphs
				.replace("[+" + type + "_DU]/[+" + type + "_DD]/[+" + type + "_DL]/[+" + type + "_DR]",
						"[+" + type + "_D]")
				// Replace Dpad UpDown with Dpad Y
				.replace("[+" + type + "_DU]/[+" + type + "_DD]", "[+" + type + "_DY]")
				// Replace Dpad LeftRight with Dpad X
				.replace("[+" + type + "_DL]/[+" + type + "_DR]", "[+" + type + "_DX]");
		return glyphs;
	}
}
