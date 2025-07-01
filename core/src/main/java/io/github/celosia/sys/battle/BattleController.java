package io.github.celosia.sys.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import io.github.celosia.sys.World;
import io.github.celosia.sys.menu.LabelStyles;
import io.github.celosia.sys.settings.Keybinds;

import java.util.ArrayList;
import java.util.List;

public class BattleController {
    static Stats johnyStats = new Stats(100, 100, 100, 100, 100, 100, 100);
    static CombatantType johny = new CombatantType("Johny", johnyStats, 0, 0, 0, 0 ,0, 0, 0);
    static Stats jerryStats = new Stats(100, 100, 100, 100, 100, 100, 100);
    static CombatantType jerry = new CombatantType("Jerry", jerryStats, 0, 0, 0, 0 ,0, 0, 0);
    static Combatant p1cmb = new Combatant(johny, 100, johnyStats, 20);
    static Combatant o1cmb = new Combatant(jerry, 100, jerryStats, 20);
    static Team player = new Team(new Combatant[]{p1cmb});
    static Team opponent = new Team(new Combatant[]{o1cmb});
    static Battle battle = new Battle(player, opponent);

    // Time in seconds
    static float s = 0f;

    // Debug
    static Label battleLog = new Label("", LabelStyles.KORURI_20);;

    public static void create(Stage stage) {

        // Debug
        battleLog.setPosition(World.WIDTH - 300, World.HEIGHT - 200, Align.right);
        stage.addActor(battleLog);
    }

    public static void input() {
        s += Gdx.graphics.getDeltaTime();

        // todo check push instead of hold, take turns
        if(s >= 0.1f) {
            if (Gdx.input.isKeyPressed(Keybinds.Keybind.ATTACK.getKey())) {
                BattleController.useSkill(Skill.ATTACK);
            } else if (Gdx.input.isKeyPressed(Keybinds.Keybind.SKILL.getKey())) {
                BattleController.useSkill(Skill.FIREBALL);
            }
            s = 0f;
        }
    }

    public static void useSkill(Skill skill) {

        // todo
        Combatant self = battle.getPlayerTeam().getCmbs()[0];
        Combatant target = battle.getOpponentTeam().getCmbs()[0];

        String nameSelf = self.getCmbType().getName();
        String nameTarget = target.getCmbType().getName();

        self.setMp(self.getMp() - skill.getCost());

        // Apply skill effects
        for(SkillEffect effect : skill.getSkillEffects()) {
            effect.apply(self, target); // wont work when extended to more than 2 combatants
        }

        // todo lang and disambiguation + system to display all status updates
        battleLog.setText(nameSelf + "\nHP: " + self.getStats().getHp() + "/" + self.getCmbType().getStatsBase().getHp() + "\nMP: " + self.getMp() + "/100\n" + nameTarget + "\nHP: " + target.getStats().getHp() + "/" + target.getCmbType().getStatsBase().getHp() + "\nMP: " + target.getMp() + "/100\n");
    }
}
