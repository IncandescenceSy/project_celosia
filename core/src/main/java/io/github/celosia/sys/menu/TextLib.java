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
	public static final String TAGS = "{FADE}{SLIDE}";

	/// Colors
	public static final String C_ALLY = "[#2d74f5]";
	public static final String C_ALLY_L = "[#528cf5]";
	public static final String C_OPP = "[#ff4545]";
	public static final String C_OPP_L = "[#ff6060]";
	public static final String C_TURN = "[#a034ff]";

	public static final String C_POS = "[#00ff00]"; // Positive numbers
	public static final String C_NEG = "[#ff0000]"; // Negative numbers
	public static final String C_NUM = "[#ffff00]"; // General numbers (turns, stacks)

	public static final String C_HP = "[#1ae132]";
	public static final String C_SP = "[#bb00ff]";
	public static final String C_SHIELD = "[#00ffff]";
	public static final String C_BLOOM = "[#ff00ff]";

	public static final String C_BUFF = "[#c6a1ff]";
	public static final String C_SKILL = "[#95c9ff]";
	public static final String C_PASSIVE = "[#c6a1ff]";
	public static final String C_STAT = "[#deff81]";
	public static final String C_EXP = "[#a034ff]"; // Calculated exponent parenthesis
	public static final String C_CD = "[#1898ff]"; // Cooldown

    public static final String SHIELD_ICON = C_SHIELD + "[+vibrating-shield]";

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
		String color = (getSide(pos) == Side.ALLY) ? C_ALLY : C_OPP;
		return color + String.format("%s%s", name, suffix) + "[WHITE]";
	}

	public static String formatName(String name, int pos) {
		return formatName(name, pos, true);
	}

	// Returns the color a number should be displayed based on if it's positive or
	// negative
	public static String getColor(long num) {
		return (num > 0) ? C_POS : (num < 0) ? C_NEG : C_NUM;
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
			c1 = C_POS;
			c2 = C_NEG;
		} else {
			c1 = C_NEG;
			c2 = C_POS;
		}

		return (mult > 1000) ? c1 : (mult < 1000) ? c2 : C_NUM;
	}

	// Returns the color a mult exponent should be based on its relation to 1,
	// whether higher is better, and whether mult is above or below 100%
	public static String getExpColor(int exp, int mult, Mult multType) {
		String c1;
		String c2;

		if (multType.isPositive()) {
			c1 = C_POS;
			c2 = C_NEG;
		} else {
			c1 = C_NEG;
			c2 = C_POS;
		}

		if (mult > 1000) {
			if (exp > 100)
				return c1;
			else if (exp < 100)
				return c2;
			else
				return C_NUM;
		} else if (mult < 1000) {
			if (exp > 100)
				return c2;
			else if (exp < 100)
				return c1;
			else
				return C_NUM;
		} else
			return C_NUM;
	}

	public static String getMultChangeColor(int multChange, Mult multType) {
		String c1;
		String c2;

		if (multType.isPositive()) {
			c1 = C_POS;
			c2 = C_NEG;
		} else {
			c1 = C_NEG;
			c2 = C_POS;
		}

		return (multChange > 0) ? c1 : (multChange < 0) ? c2 : C_NUM;
	}

	public static String getExpChangeColor(int expChange, int mult, Mult multType) {
		String c1;
		String c2;

		if (multType.isPositive()) {
			c1 = C_POS;
			c2 = C_NEG;
		} else {
			c1 = C_NEG;
			c2 = C_POS;
		}

		if (mult > 1000) {
			if (expChange > 0) {
				return c1;
			} else if (expChange < 0) {
				return c2;
			} else {
				return C_NUM;
			}
		} else if (mult < 1000) {
			if (expChange > 0) {
				return c2;
			} else if (expChange < 0) {
				return c1;
			} else {
				return C_NUM;
			}
		} else {
			return C_NUM;
		}
	}

	public static String getMultWithExpColor(double multWithExp, Mult multType) {
		String c1;
		String c2;

		if (multType.isPositive()) {
			c1 = C_POS;
			c2 = C_NEG;
		} else {
			c1 = C_NEG;
			c2 = C_POS;
		}

		return (multWithExp > 1) ? c1 : (multWithExp < 1) ? c2 : C_NUM;
	}

	public static String getMultWithExpChangeColor(double multWithExpChange, Mult multType) {
		String c1;
		String c2;

		if (multType.isPositive()) {
			c1 = C_POS;
			c2 = C_NEG;
		} else {
			c1 = C_NEG;
			c2 = C_POS;
		}

		return (multWithExpChange > 0) ? c1 : (multWithExpChange < 0) ? c2 : C_NUM;
	}

	public static String getModColor(int mod, Mod modType) {
		String c1;
		String c2;

		if (modType.isPositive()) {
			c1 = C_POS;
			c2 = C_NEG;
		} else {
			c1 = C_NEG;
			c2 = C_POS;
		}

		return (mod > 0) ? c1 : (mod < 0) ? c2 : C_NUM;
	}

	public static String formatMod(int mod, Mod modType) {
		return getModColor(mod, modType) + getSign(mod) + mod;
	}

	public static String getStatColor(long stat, long statBase) {
		return (stat > statBase) ? C_POS : (stat < statBase) ? C_NEG : C_NUM;
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
			builder.append(lang.format("log.stage_stat", C_STAT + stat.getName(),
					getStatColor(statOld, statDefault) + formatNum(statOld),
					getStatColor(statNew, statDefault) + formatNum(statNew), C_NUM + formatNum(statDefault),
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
				C_SKILL + skill.getName());

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
