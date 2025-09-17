package io.github.celosia.sys.battle;

import io.github.celosia.sys.battle.buff_effects.ChangeHp;
import io.github.celosia.sys.battle.buff_effects.ChangeInfiniteSp;
import io.github.celosia.sys.battle.buff_effects.ChangeMod;
import io.github.celosia.sys.battle.buff_effects.ChangeMult;

import static io.github.celosia.sys.settings.Lang.lang;

public class Passives {
	static Passive RESTORATION = new Passive(lang.get("passive.restoration"), lang.get("passive.restoration.desc"),
			new ChangeHp.Builder(1000).build());
	static Passive BUFF_DURATION_UP = new Passive(lang.get("passive.buff_duration_up"),
			lang.get("passive.buff_duration_up.desc"), new ChangeMod(Mod.DURATION_BUFF_DEALT, 1));
	static Passive DEBUFF_DURATION_UP = new Passive(lang.get("passive.debuff_duration_up"),
			lang.get("passive.debuff_duration_up.desc"), new ChangeMod(Mod.DURATION_DEBUFF_DEALT, 1));
	static Passive ETERNAL_WELLSPRING = new Passive(lang.get("passive.eternal_wellspring"),
			lang.get("passive.eternal_wellspring.desc"), new ChangeInfiniteSp(1));
    static Passive PERCENTAGE_DMG_TAKEN_DOWN_50 = new Passive(lang.get("passive.percentage_dmg_down_50"), lang.get("passive.percentage_dmg_down_50.desc"), new ChangeMult(Mult.PERCENTAGE_DMG_TAKEN, -500));
    static Passive PERCENTAGE_DMG_TAKEN_DOWN_999 = new Passive(lang.get("passive.percentage_dmg_down_999"), lang.get("passive.percentage_dmg_down_999.desc"), new ChangeMult(Mult.PERCENTAGE_DMG_TAKEN, -999));
}
