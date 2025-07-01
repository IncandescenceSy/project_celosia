package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.Combatant;
import io.github.celosia.sys.battle.Element;
import io.github.celosia.sys.battle.SkillEffect;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Damage implements SkillEffect {

    private int pow;
    private Element element;

    // todo damage types

    public Damage(int pow, Element element) {
        this.pow = pow;
        this.element = element;
    }

    @Override
    public void apply(Combatant self, Combatant target) {
        Logger logger = Logger.getLogger("a"); // Fix base HP somehow being updated
        logger.log(new LogRecord(Level.INFO, "HP: " + target.getStats().getHp() + "; Base HP: " + target.getCmbType().getStatsBase().getHp()));
        target.getStats().setHp(target.getStats().getHp() - pow);
        logger.log(new LogRecord(Level.INFO, "HP: " + target.getStats().getHp() + "; Base HP: " + target.getCmbType().getStatsBase().getHp()));
    }
}
