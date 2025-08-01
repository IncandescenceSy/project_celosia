package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.*;

import static io.github.celosia.sys.settings.Lang.lang;

// Todo heavily limit %-based damage on bosses
public class ChangeHP implements BuffEffect {

    private final float change; // Amount to change HP by. 1f = +100%

    public ChangeHP(float change) {
        this.change = change;
    }

    @Override
    public String onGive(Combatant self) {
        return "";
    }

    @Override
    public String onRemove(Combatant self) {
        return "";
    }

    @Override
    public String onUseSkill(Combatant self, Combatant target) {
        return "";
    }

    @Override
    public String onTakeDamage(Combatant self) {
        return "";
    }

    @Override
    public String onTurnEnd(Combatant self) {
        if(change < 0) { // Damage
            String hp = (self.getBarrier() + self.getDefend() > 0) ? lang.get("barrier") : lang.get("hp");
            Result result = self.damage((int) (self.getStatsDefault().getHp() * (change * (Math.max(self.getMultDoTDmgTaken(), 10) / 100f))));

            String[] msgs = result.getMessages();
            String[] str1 = new String[]{""};
            String[] str2 = new String[]{""};
            if (msgs[0] != null) str1 = msgs[0].split("[.*HP|.*Barrier]");
            if (msgs.length > 1 && msgs[1] != null) str2 = msgs[1].split("[.*HP|.*Barrier]");

            return (hp + str1[str1.length - 1] + str2[str2.length - 1]);
        } else { // Healing
            int hpOld = self.getStatsCur().getHp();
            int hpMax = self.getStatsDefault().getHp();
            int hpNew = (int) Math.max(hpOld, Math.min(hpOld + ((hpMax * change) * (Math.max(self.getMultHealingTaken(), 10) / 100f)), hpMax));
            self.getStatsCur().setHp(hpNew);
            return lang.get("hp") + " " + hpOld + " -> " + hpNew + " (+" + Math.max(hpNew - hpOld, 0) + ")";
        }
    }
}
