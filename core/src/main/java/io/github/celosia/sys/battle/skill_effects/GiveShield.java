package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.BuffInstance;
import io.github.celosia.sys.battle.Buffs;
import io.github.celosia.sys.battle.Mod;
import io.github.celosia.sys.battle.ResultType;
import io.github.celosia.sys.battle.Unit;
import org.jetbrains.annotations.Nullable;

import static io.github.celosia.sys.battle.BattleControllerLib.appendToLog;
import static io.github.celosia.sys.battle.BattleLib.STAT_MULT_HIDDEN;
import static io.github.celosia.sys.save.Lang.lang;
import static io.github.celosia.sys.util.TextLib.C_HP;
import static io.github.celosia.sys.util.TextLib.C_SHIELD;
import static io.github.celosia.sys.util.TextLib.formatName;
import static io.github.celosia.sys.util.TextLib.formatNum;

public class GiveShield extends GiveBuff {

    public GiveShield(GiveBuff.Builder builder) {
        super(builder);
        if(builder.getBuff() != Buffs.SHIELD) {
            throw new IllegalArgumentException("GiveShield must have Buff SHIELD");
        }
    }

    public static class Builder extends GiveBuff.Builder {
        public Builder(int turns) {
            super(Buffs.SHIELD, turns);
        }

        @Override
        public GiveShield build() {
            return new GiveShield(this);
        }
    }

    @Override
    public ResultType apply(Unit self, Unit target, boolean isMainTarget, ResultType resultPrev) {
        if (resultPrev.ordinal() < this.getMinResult().ordinal() || (this.isMainTargetOnly() && !isMainTarget)) {
            return ResultType.SUCCESS;
        }

        Unit unit = (this.isGiveToSelf()) ? self : target;

        int turnsMod = this.getTurns() + self.getMod(Mod.DURATION_BUFF_DEALT) + unit.getMod(Mod.DURATION_BUFF_TAKEN);

        @Nullable BuffInstance buffInstance = unit.getBuffInstance(Buffs.SHIELD);

        if(buffInstance == null) {
            unit.addBuffInstances(new BuffInstance(Buffs.SHIELD, turnsMod, 1));
        }

        unit.setShield(calcShieldAmt(this.getStacks(), self, unit));

        return ResultType.SUCCESS;
    }

    public long calcShieldAmt(int stacks, Unit self, Unit target) {
        long hpMax = target.getMaxHp();
        // Add shield (shield + defend cannot exceed max HP)
        long shieldOld = target.getShield();
        long shieldPerStack = self.getFthWithStage() / 100;

        long shieldNew = ((self.getDefend() + shieldOld + (shieldPerStack * stacks)) > hpMax) ?
                hpMax - self.getDefend() : shieldOld + (shieldPerStack * stacks);

        long shieldOldDisp = self.getDisplayShield();
        long shieldNewDisp = shieldNew / STAT_MULT_HIDDEN;

        long defendDisp = self.getDisplayDefend();

        appendToLog(lang.format("log.change_shield", formatName(self.getUnitType().getName(), self.getPos()),
                C_SHIELD + formatNum((defendDisp + shieldOldDisp)), C_SHIELD + formatNum((defendDisp + shieldNewDisp)),
                C_HP + formatNum(self.getDisplayMaxHp()),
                C_SHIELD + "+" + formatNum(((defendDisp + shieldNewDisp) - (defendDisp + shieldOldDisp)))));

        return shieldNew;
    }
}
