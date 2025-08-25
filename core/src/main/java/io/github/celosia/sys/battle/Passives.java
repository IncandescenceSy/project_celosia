package io.github.celosia.sys.battle;

import io.github.celosia.sys.battle.buff_effects.ChangeDurationModBuffTypeDealt;
import io.github.celosia.sys.battle.buff_effects.ChangeHP;

import static io.github.celosia.sys.settings.Lang.lang;

public class Passives {
    static Passive RESTORATION = new Passive(lang.get("passive.restoration"), lang.get("passive.restoration.desc"), new ChangeHP(0.1));
    static Passive BUFF_DURATION_UP = new Passive(lang.get("passive.buff_duration_up"), lang.get("passive.buff_duration_up.desc"), new ChangeDurationModBuffTypeDealt(BuffType.BUFF,  1));
    static Passive DEBUFF_DURATION_UP = new Passive(lang.get("passive.debuff_duration_up"), lang.get("passive.debuff_duration_up.desc"), new ChangeDurationModBuffTypeDealt(BuffType.DEBUFF, 1));
}
