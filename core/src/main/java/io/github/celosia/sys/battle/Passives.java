package io.github.celosia.sys.battle;

import io.github.celosia.sys.battle.passive_effects.ChangeDurationModBuffTypeGiven;

import static io.github.celosia.sys.settings.Lang.lang;

public class Passives {
    static Passive BUFF_DURATION_UP = new Passive(lang.get("passive.buff_duration_up"), lang.get("passive.buff_duration_up.desc"), new ChangeDurationModBuffTypeGiven(BuffType.BUFF,  1));
    static Passive DEBUFF_DURATION_UP = new Passive(lang.get("passive.debuff_duration_up"), lang.get("passive.debuff_duration_up.desc"), new ChangeDurationModBuffTypeGiven(BuffType.DEBUFF, 1));
}
