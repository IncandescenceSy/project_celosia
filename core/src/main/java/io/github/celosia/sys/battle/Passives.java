package io.github.celosia.sys.battle;

import io.github.celosia.sys.battle.buff_effects.ChangeDurationModBuffTypeDealt;
import io.github.celosia.sys.battle.buff_effects.ChangeHp;
import io.github.celosia.sys.battle.buff_effects.ChangeInfiniteSp;

import static io.github.celosia.sys.settings.Lang.lang;

public class Passives {
	static Passive RESTORATION = new Passive(lang.get("passive.restoration"), lang.get("passive.restoration.desc"),
			new ChangeHp.Builder(1000).build());
	static Passive BUFF_DURATION_UP = new Passive(lang.get("passive.buff_duration_up"),
			lang.get("passive.buff_duration_up.desc"), new ChangeDurationModBuffTypeDealt(BuffType.BUFF, 1));
	static Passive DEBUFF_DURATION_UP = new Passive(lang.get("passive.debuff_duration_up"),
			lang.get("passive.debuff_duration_up.desc"), new ChangeDurationModBuffTypeDealt(BuffType.DEBUFF, 1));
	static Passive ETERNAL_WELLSPRING = new Passive(lang.get("passive.eternal_wellspring"),
			lang.get("passive.eternal_wellspring.desc"), new ChangeInfiniteSp(1));
}
