package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.Mult;
import io.github.celosia.sys.battle.ResultType;
import io.github.celosia.sys.battle.SkillEffect;
import io.github.celosia.sys.battle.SkillType;
import io.github.celosia.sys.battle.Unit;

import java.util.ArrayList;
import java.util.List;

import static io.github.celosia.sys.battle.BattleControllerLib.appendToLog;
import static io.github.celosia.sys.save.Lang.lang;
import static io.github.celosia.sys.util.TextLib.C_HP;
import static io.github.celosia.sys.util.TextLib.C_POS;
import static io.github.celosia.sys.util.TextLib.formatName;
import static io.github.celosia.sys.util.TextLib.formatNum;

/**
 * @param overheal Amount to heal over max HP in 10ths of a % (1000 = 100%)
 */
public record Heal(int pow, int overheal, boolean isInstant, boolean giveToSelf, boolean mainTargetOnly)
        implements SkillEffect {

    public Heal(Builder builder) {
        this(builder.pow, builder.overHeal, builder.isInstant, builder.giveToSelf, builder.mainTargetOnly);
    }

    public static class Builder {

        private final int pow;

        private int overHeal = 0;
        private boolean isInstant = false;
        private boolean giveToSelf = false;
        private boolean mainTargetOnly = false;

        public Builder(int pow) {
            this.pow = pow;
        }

        public Builder overHeal(int overHeal) {
            this.overHeal = overHeal;
            return this;
        }

        public Builder instant() {
            isInstant = true;
            return this;
        }

        public Builder giveToSelf() {
            giveToSelf = true;
            return this;
        }

        public Builder mainTargetOnly() {
            mainTargetOnly = true;
            return this;
        }

        public Heal build() {
            return new Heal(this);
        }
    }

    @Override
    public ResultType apply(Unit self, Unit target, boolean isMainTarget, ResultType resultPrev) {
        if (mainTargetOnly && !isMainTarget) {
            return ResultType.SUCCESS;
        }

        List<String> msg = new ArrayList<>();

        Unit unit = (giveToSelf) ? self : target;

        // Heals by pow% of user's Fth
        long heal = (long) (self.getFthWithStage() * (pow / 100d) * self.getMultWithExp(Mult.HEALING_DEALT) *
                unit.getMultWithExp(Mult.HEALING_TAKEN));

        self.onDealHeal(unit, heal, overheal);
        unit.onTakeHeal(self, heal, overheal);

        long hpCur = unit.getHp();
        long hpMax = unit.getMaxHp();
        // Picks the lower of (current HP + heal amount) and (maximum allowed overHeal
        // of this skill), and then the higher between that and current HP
        long hpNew = Math.max(hpCur, Math.min(hpCur + heal, (long) (hpMax * (1 + (overheal / 1000d)))));

        if (hpNew > hpCur) {
            long hpOldDisp = unit.getDisplayHp();

            unit.setHp(hpNew);

            long hpNewDisp = unit.getDisplayHp();
            long hpMaxDisp = unit.getDisplayMaxHp();

            msg.add(lang.format("log.change_hp", formatName(unit.getUnitType().getName(), self.getPos()),
                    C_HP + formatNum(hpOldDisp), C_HP + formatNum(hpNewDisp), C_HP + formatNum(hpMaxDisp),
                    C_POS + "+" + formatNum((hpNewDisp - hpOldDisp))));
        }

        appendToLog(msg);
        return ResultType.SUCCESS;
    }

    @Override
    public boolean isInstant() {
        return isInstant;
    }

    @Override
    public SkillType getSkillType() {
        return SkillType.FTH;
    }
}
