package io.github.celosia.sys.battle;

import io.github.celosia.sys.battle.buff_effects.ChangeDefend;
import io.github.celosia.sys.battle.buff_effects.ChangeEffectBlock;
import io.github.celosia.sys.battle.buff_effects.ChangeExtraActions;
import io.github.celosia.sys.battle.buff_effects.ChangeHp;
import io.github.celosia.sys.battle.buff_effects.ChangeMult;
import io.github.celosia.sys.battle.buff_effects.ChangeStat;

import static io.github.celosia.sys.settings.Lang.lang;

public class Buffs {
	// Basic
	static Buff DEFEND = new Buff(lang.get("skill.defend"), lang.get("buff.defend.desc"), BuffType.BUFF, 1,
			new ChangeDefend(0.2), new ChangeMult(Mult.DMG_TAKEN, -2000));

	// Decreases multDmgTaken by way more than necessary to be absolutely certain
	// this minimizes it
	static Buff PROTECT = new Buff(lang.get("buff.protect"), lang.get("buff.protect.desc"), BuffType.BUFF, 1,
			new ChangeMult(Mult.DMG_TAKEN, -10_000_000), new ChangeEffectBlock(1));

	// todo: affinity should convey immunity to these
	// Elemental debuffs
	static Buff BURN = new Buff(lang.get("buff.burn"), lang.get("buff.burn.desc"), BuffType.DEBUFF, 5,
			new ChangeHp.Builder(-200).build(), new ChangeStat(Stat.STR, -500));
	static Buff FROSTBITE = new Buff(lang.get("buff.frostbite"), lang.get("buff.frostbite.desc"), BuffType.DEBUFF, 5,
			new ChangeHp.Builder(-200).build(), new ChangeStat(Stat.MAG, -500));
	static Buff SHOCK = new Buff(lang.get("buff.shock"), lang.get("buff.shock.desc"), BuffType.DEBUFF, 5,
			new ChangeHp.Builder(-200).build(), new ChangeStat(Stat.AGI, -500));
	static Buff WINDSWEPT = new Buff(lang.get("buff.windswept"), lang.get("buff.windswept.desc"), BuffType.DEBUFF, 5,
			new ChangeMult(Mult.SP_GAIN, -500), new ChangeStat(Stat.RES, -500));
	static Buff TREMOR = new Buff(lang.get("buff.tremor"), lang.get("buff.tremor.desc"), BuffType.DEBUFF, 5,
			new ChangeMult(Mult.SP_GAIN, -500), new ChangeStat(Stat.AMR, -500));
	static Buff DAZZLED = new Buff(lang.get("buff.dazzled"), lang.get("buff.dazzled.desc"), BuffType.DEBUFF, 5,
			new ChangeMult(Mult.SP_GAIN, -500), new ChangeStat(Stat.FTH, -500));
	static Buff CURSE = new Buff(lang.get("buff.curse"), lang.get("buff.curse.desc"), BuffType.DEBUFF, 5,
			new ChangeHp.Builder(-250).build(), new ChangeStat(Stat.FTH, -500));
	static Buff POISON = new Buff(lang.get("buff.poison"), lang.get("buff.poison.desc"), BuffType.DEBUFF, 5,
			new ChangeHp.Builder(-250).build(), new ChangeMult(Mult.HEALING_TAKEN, -500));
	static Buff RADIATION = new Buff(lang.get("buff.radiation"), lang.get("buff.radiation.desc"), BuffType.DEBUFF, 5,
			new ChangeHp.Builder(-300).build(), new ChangeMult(Mult.DMG_TAKEN, 5));

	// Superior elemental debuffs
	static Buff BURNT_OUT = new Buff(lang.get("buff.burnt_out"), lang.get("buff.burnt_out.desc"), BuffType.DEBUFF, 1,
			new ChangeMult(Mult.IGNIS_DMG_TAKEN, 2000), new ChangeStat(Stat.STR, -1000));
	static Buff FROSTBOUND = new Buff(lang.get("buff.frostbound"), lang.get("buff.frostbound.desc"), BuffType.DEBUFF, 1,
			new ChangeMult(Mult.GLACIES_DMG_TAKEN, 2000), new ChangeStat(Stat.MAG, -1000));
	static Buff OVERLOADED = new Buff(lang.get("buff.overloaded"), lang.get("buff.overloaded.desc"), BuffType.DEBUFF, 1,
			new ChangeMult(Mult.FULGUR_DMG_TAKEN, 2000), new ChangeStat(Stat.AGI, -1000));
	static Buff TEMPEST = new Buff(lang.get("buff.tempest"), lang.get("buff.tempest.desc"), BuffType.DEBUFF, 1,
			new ChangeMult(Mult.VENTUS_DMG_TAKEN, 2000), new ChangeStat(Stat.RES, -1000));
	static Buff SHATTERED = new Buff(lang.get("buff.shattered"), lang.get("buff.shattered.desc"), BuffType.DEBUFF, 1,
			new ChangeMult(Mult.TERRA_DMG_TAKEN, 2000), new ChangeStat(Stat.AMR, -1000));
	static Buff STARSTRUCK = new Buff(lang.get("buff.starstruck"), lang.get("buff.starstruck.desc"), BuffType.DEBUFF, 1,
			new ChangeMult(Mult.LUX_DMG_TAKEN, 2000), new ChangeStat(Stat.FTH, -1000));
	static Buff DOOMED = new Buff(lang.get("buff.doomed"), lang.get("buff.doomed.desc"), BuffType.DEBUFF, 1,
			new ChangeMult(Mult.MALUM_DMG_TAKEN, 2000), new ChangeStat(Stat.FTH, -1000));
	static Buff BLIGHTED = new Buff(lang.get("buff.blighted"), lang.get("buff.blighted.desc"), BuffType.DEBUFF, 1,
			new ChangeMult(Mult.DOT_DMG_TAKEN, 2000), new ChangeMult(Mult.HEALING_TAKEN, -1000));
	static Buff DECAY = new Buff(lang.get("buff.decay"), lang.get("buff.decay.desc"), BuffType.DEBUFF, 1,
			new ChangeMult(Mult.IGNIS_DMG_TAKEN, 1500), new ChangeMult(Mult.DOT_DMG_TAKEN, 1500),
			new ChangeMult(Mult.HEALING_TAKEN, -1000));

	// Other
	static Buff REGENERATION = new Buff(lang.get("buff.regeneration"), lang.get("buff.regeneration.desc"),
			BuffType.BUFF, 5, new ChangeHp.Builder(500).build());
	static Buff EXTRA_ACTION = new Buff(lang.get("buff.extra_action"), lang.get("buff.extra_action.desc"),
			BuffType.BUFF, 1, new ChangeExtraActions(1));
}
