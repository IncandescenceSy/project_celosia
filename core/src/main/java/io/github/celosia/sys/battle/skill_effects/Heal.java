package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.ResultType;
import io.github.celosia.sys.battle.SkillEffect;
import io.github.celosia.sys.battle.Unit;

import java.util.ArrayList;
import java.util.List;

import static io.github.celosia.sys.battle.BattleController.appendToLog;
import static io.github.celosia.sys.battle.BuffEffectLib.notifyOnGiveShield;
import static io.github.celosia.sys.battle.BuffEffectLib.notifyOnHeal;
import static io.github.celosia.sys.menu.TextLib.c_buff;
import static io.github.celosia.sys.menu.TextLib.c_hp;
import static io.github.celosia.sys.menu.TextLib.c_num;
import static io.github.celosia.sys.menu.TextLib.c_shield;
import static io.github.celosia.sys.menu.TextLib.formatName;
import static io.github.celosia.sys.menu.TextLib.getColor;
import static io.github.celosia.sys.settings.Lang.lang;

public class Heal implements SkillEffect {

    private final int pow;
    private final double overHeal; // Amount to heal over max HP. 1f = 100%
    private final int shieldTurns;
    private final boolean isInstant;
    private final boolean mainTargetOnly;

    public Heal(int pow, double overHeal, int shieldTurns, boolean isInstant, boolean mainTargetOnly) {
        this.pow = pow;
        this.overHeal = overHeal;
        this.shieldTurns = shieldTurns;
        this.isInstant = isInstant;
        this.mainTargetOnly = mainTargetOnly;
    }

    public Heal(int pow, int shieldTurns) {
        this(pow, 0, shieldTurns, false, false);
    }

    public Heal(int pow, double overHeal) {
        this(pow, overHeal, 0, false, false);
    }

    public Heal(int pow) {
        this(pow, 0, 0, false, false);
    }

    @Override
    public ResultType apply(Unit self, Unit target, boolean isMainTarget, ResultType resultPrev) {
        if(!mainTargetOnly || isMainTarget) {
            List<String> msg = new ArrayList<>();

            // Heals by pow% of user's Fth
            int heal = (int) (self.getFthWithStage() * (pow / 100d) * (Math.max(self.getMultHealingDealt(), 1000) / 10000d) * (Math.max(target.getMultHealingTaken(), 1000) / 10000d));

            // Adds shield (shield + defend cannot exceed max HP)
            if (shieldTurns > 0) {
                String str = "";

                // Apply self's durationMod
                int turnsMod = shieldTurns + self.getDurationModBuffDealt() + target.getDurationModBuffTaken();

                notifyOnGiveShield(self, target, turnsMod, heal);

                int hpMax = target.getStatsDefault().getHp();
                int shieldCur = target.getShield();
                int shieldNew = (shieldCur + target.getDefend() + heal > hpMax) ? hpMax - target.getDefend() : shieldCur + heal;
                int turnsCur = target.getShieldTurns();

                if (shieldNew > shieldCur) {
                    int shieldCurDisp = target.getDisplayShield();

                    target.setShield(shieldNew);

                    int hpMaxDisp = target.getStatsDefault().getDisplayHp();
                    int shieldNewDisp = target.getDisplayShield();

                    str = formatName(target.getUnitType().getName(), self.getPos()) + " " + c_buff + lang.get("shield") + " " + c_shield + String.format("%,d", (shieldCurDisp + target.getDisplayDefend())) + "[WHITE] → " + c_shield + String.format("%,d", (shieldNewDisp + target.getDisplayDefend())) + "[WHITE]/" + c_shield + String.format("%,d", hpMaxDisp) + getColor(shieldNewDisp - shieldCurDisp) + " (+" + String.format("%,d", (shieldNewDisp - shieldCurDisp)) + ")";
                }

                if (turnsMod > turnsCur) {
                    target.setShieldTurns(turnsMod);
                    if (shieldNew > shieldCur)
                        msg.add(str + "[WHITE], " + lang.get("turns") + " " + c_num + turnsCur + "[WHITE] → " + c_num + turnsMod);
                    else
                        msg.add(formatName(target.getUnitType().getName(), self.getPos()) + " " + lang.get("shield") + " " + lang.get("turns") + " " + c_num + turnsCur + "[WHITE] → " + c_num + turnsMod);
                }

                // Effect block message
                if(shieldCur == 0 && shieldNew > 0 && target.isEffectBlock() && target.getDefend() == 0) {
                    msg.add(self.getUnitType().getName() + " " + lang.get("log.is_now") + " " + c_buff + lang.get("log.effect_block"));
                }

            } else { // Heals
                notifyOnHeal(self, target, heal, overHeal);

                int hpCur = target.getStatsCur().getHp();
                int hpMax = target.getStatsDefault().getHp();
                // Picks the lower of (current HP + heal amount) and (maximum allowed overHeal of this skill), and then the higher between that and current HP
                int hpNew = (int) Math.max(hpCur, Math.min(hpCur + heal, hpMax * (1 + overHeal)));

                if (hpNew > hpCur) {
                    int hpCurDisp = target.getStatsCur().getDisplayHp();

                    target.getStatsCur().setHp(hpNew);

                    int hpNewDisp = target.getStatsCur().getDisplayHp();
                    int hpMaxDisp = target.getStatsDefault().getDisplayHp();

                    msg.add(formatName(target.getUnitType().getName(), self.getPos()) + " " + lang.get("hp") + " " + c_hp + String.format("%,d", hpCurDisp) + "[WHITE] → " + c_hp + String.format("%,d", hpNewDisp) + "[WHITE]/" + c_hp + String.format("%,d", hpMaxDisp) + getColor(hpNewDisp - hpCurDisp) + " (+" + String.format("%,d", (hpNewDisp - hpCurDisp)) + ")");
                }
            }

            appendToLog(msg);
        }

        return ResultType.SUCCESS;
    }

    @Override
    public boolean isInstant() {
        return isInstant;
    }
}
