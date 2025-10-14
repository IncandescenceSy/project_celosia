package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.BuffInstance;
import io.github.celosia.sys.battle.Buffs;
import io.github.celosia.sys.battle.Mod;
import io.github.celosia.sys.battle.ResultType;
import io.github.celosia.sys.battle.SkillEffect;
import io.github.celosia.sys.battle.Unit;
import io.github.celosia.sys.entity.IconEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static io.github.celosia.sys.battle.BattleControllerLib.appendToLog;
import static io.github.celosia.sys.battle.BattleLib.STAT_MULT_HIDDEN;
import static io.github.celosia.sys.save.Lang.lang;
import static io.github.celosia.sys.util.TextLib.C_HP;
import static io.github.celosia.sys.util.TextLib.C_SHIELD;
import static io.github.celosia.sys.util.TextLib.formatName;
import static io.github.celosia.sys.util.TextLib.formatNum;

public record GiveShield(int turns, int pow, ResultType minResult, boolean giveToSelf, boolean mainTargetOnly,
                         boolean isInstant)
        implements SkillEffect {

    public GiveShield(Builder builder) {
        this(builder.turns, builder.pow, builder.minResult, builder.giveToSelf, builder.mainTargetOnly,
                builder.isInstant);
    }

    public static class Builder {

        private final int turns;
        private final int pow;

        private ResultType minResult = ResultType.SUCCESS;
        private boolean giveToSelf = false;
        private boolean mainTargetOnly = false;
        private boolean isInstant = true;

        public Builder(int turns, int pow) {
            this.turns = turns;
            this.pow = pow;
        }

        public Builder minResult(ResultType minResult) {
            this.minResult = minResult;
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

        public Builder notInstant() {
            isInstant = false;
            return this;
        }

        public GiveShield build() {
            return new GiveShield(this);
        }
    }

    @Override
    public ResultType apply(Unit self, Unit target, boolean isMainTarget, ResultType resultPrev) {
        if (resultPrev.ordinal() < minResult.ordinal() || (this.mainTargetOnly() && !isMainTarget)) {
            return ResultType.SUCCESS;
        }

        Unit unit = (this.giveToSelf()) ? self : target;

        int turnsMod = this.turns() + self.getMod(Mod.DURATION_BUFF_DEALT) + unit.getMod(Mod.DURATION_BUFF_TAKEN);

        @Nullable BuffInstance buffInstance = unit.getBuffInstance(Buffs.SHIELD);

        if (buffInstance == null) {
            unit.addBuffInstances(new BuffInstance(Buffs.SHIELD, turnsMod, 1));
        }

        unit.setShield(calcShieldAmt(pow, self, unit));

        return ResultType.SUCCESS;
    }

    @Override
    public boolean isInstant() {
        return isInstant;
    }

    @Override
    public @NotNull IconEntity getDescInclusion() {
        return Buffs.SHIELD;
    }

    public long calcShieldAmt(int pow, Unit self, Unit target) {
        long hpMax = target.getMaxHp();
        // Add shield (shield + defend cannot exceed max HP)
        long shieldOld = target.getShield();
        long shieldPerPow = self.getFthWithStage() / 100;

        long shieldNew = ((target.getDefend() + shieldOld + (shieldPerPow * pow)) > hpMax) ?
                hpMax - target.getDefend() : shieldOld + (shieldPerPow * pow);

        long shieldOldDisp = target.getDisplayShield();
        long shieldNewDisp = shieldNew / STAT_MULT_HIDDEN;

        long defendDisp = target.getDisplayDefend();

        appendToLog(lang.format("log.change_shield", formatName(target.getUnitType().getName(), target.getPos()),
                C_SHIELD + formatNum((defendDisp + shieldOldDisp)), C_SHIELD + formatNum((defendDisp + shieldNewDisp)),
                C_HP + formatNum(target.getDisplayMaxHp()),
                C_SHIELD + "+" + formatNum(((defendDisp + shieldNewDisp) - (defendDisp + shieldOldDisp)))));

        return shieldNew;
    }
}
