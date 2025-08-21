package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.*;

import java.util.List;

import static io.github.celosia.sys.settings.Lang.lang;

// Todo heavily limit %-based damage on bosses
public class ChangeHP implements BuffEffect {

    private final double change; // Amount to change HP by. 1 = +100%
    private final boolean pierce;

    public ChangeHP(double change, boolean pierce) {
        this.change = change;
        this.pierce = pierce;
    }

    public ChangeHP(double change) {
        this(change, false);
    }

    @Override
    public String[] onTurnEnd(Combatant self) {
        if(change < 0) { // Damage
            //String hp = (self.getShield() + self.getDefend() > 0) ? lang.get("shield") : lang.get("hp");
            Result result = self.damage((int) Math.abs((self.getStatsDefault().getHp() * change) * (Math.max(self.getMultDmgTaken(), 10) / 100d) * (Math.max(self.getMultDoTDmgTaken(), 10) / 100d)), pierce, false);

            /*List<String> msgs = result.getMessages();
            String[] str1 = new String[]{""};
            String[] str2 = new String[]{""};
            // todo fix "Jacob's Burn: HPcob" (how does this even happen? I know this was working fine earlier too)
            if (msgs.get(0) != null) str1 = msgs.get(0).split("[.*" + lang.get("hp") + "|.*" + lang.get("shield") + "]");
            if (msgs.size() > 1 && msgs.get(1) != null) str2 = msgs.get(1).split("[.*" + lang.get("hp") + "|.*" + lang.get("shield") + "]");*/

            return result.getMessages().toArray(String[]::new);
        } else { // Healing
            int hpOld = self.getStatsCur().getHp();
            int hpMax = self.getStatsDefault().getHp();
            int hpNew = (int) Math.max(hpOld, Math.min(hpOld + ((hpMax * change) * (Math.max(self.getMultHealingTaken(), 10) / 100d)), hpMax));
            self.getStatsCur().setHp(hpNew);
            return new String[]{lang.get("hp") + " " + hpOld + " -> " + hpNew + " (+" + Math.max(hpNew - hpOld, 0) + ")"};
        }
    }
}
