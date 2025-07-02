package io.github.celosia.sys.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
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
    static Stats jerryStats = new Stats(80, 80, 80, 80, 80, 80, 80);
    static CombatantType jerry = new CombatantType("Jerry", jerryStats, 0, 0, 0, 0 ,0, 0, 0);
    static Combatant p1cmb = new Combatant(johny, 100, johnyStats.getRealStats(100), 20);
    static Combatant o1cmb = new Combatant(jerry, 100, jerryStats.getRealStats(100), 20);
    static Team player = new Team(new Combatant[]{p1cmb});
    static Team opponent = new Team(new Combatant[]{o1cmb});
    static Battle battle = new Battle(player, opponent);

    // Time to wait in seconds
    static float wait = 0f;

    // actions being made this turn
    static List<SkillTargeting> moves = new ArrayList<SkillTargeting>();

    // temp
    static Skills[] oppSkills = {Skills.ATTACK, Skills.FIREBALL};

    static int selectingMove = 0; // Who's currently selecting their move. 0-4 = player; 5-9 = opponent; 10 = moves are exeucting

    // Debug
    static Label battleLog = new Label("", LabelStyles.KORURI_20);;

    public static void create(Stage stage) {
        // Debug
        battleLog.setPosition(World.WIDTH_2, World.HEIGHT - 200, Align.right);
        stage.addActor(battleLog);
    }

    public static void input() {
        if(wait > 0) {
            wait -= Gdx.graphics.getDeltaTime();
        } else if(selectingMove <= 4) { // Player's turn
            if(selectingMove < player.getCmbs().length) { // if there are more allies yet to act
                if (Gdx.input.isKeyJustPressed(Keybinds.ATTACK.getKey())) {
                    moves.add(new SkillTargeting(Skills.ATTACK, player.getCmbs()[selectingMove], opponent.getCmbs()[0])); // todo opponent targeting
                    selectingMove++;
                } else if (Gdx.input.isKeyJustPressed(Keybinds.SKILL.getKey())) {
                    moves.add(new SkillTargeting(Skills.FIREBALL, player.getCmbs()[selectingMove], opponent.getCmbs()[0]));
                    selectingMove++;
                }
            } else selectingMove = 5; // Jump to opponent move selection
        } else if(selectingMove <= 9) { // opponent's turn
            if((selectingMove - 5) < opponent.getCmbs().length) { // if there are more opponents yet to act
                moves.add(new SkillTargeting(oppSkills[MathUtils.random(0, oppSkills.length - 1)], opponent.getCmbs()[selectingMove - 5], player.getCmbs()[0])); // todo AI
                selectingMove++;
            } else selectingMove = 10; // Jump to move execution
        } else if(selectingMove == 10) { // Moves play out
            wait++;
            if(moves.isEmpty()){
                selectingMove = 0;
                battle.setTurn(battle.getTurn() + 1);
                return;
            }

            // Sort moves by Agi
            moves.sort((a, b) -> Integer.compare(
                b.getSelf().getStatsCur().getAgi(),
                a.getSelf().getStatsCur().getAgi()
            ));

            // The next move plays out
            SkillTargeting move = moves.get(0);

            if(isMoveValid(move)) {
                move.getSelf().setMp(move.getSelf().getMp() - move.getSkill().getCost());
                for (SkillEffect effect : move.getSkill().getSkillEffects()) {
                    effect.apply(move.getSelf(), move.getTarget());
                }
            }

            moves.remove(0);
        }

        // todo lang and disambiguation + system to display all status updates
        battleLog.setText("turn = " + battle.getTurn() + "\nselectingMove = " + selectingMove + "\nwait = " + wait + "\nmoves = " + moves + "\n" + p1cmb.getCmbType().getName() + "\nHP: " + p1cmb.getStatsCur().getHp() + "/" + p1cmb.getStatsDefault().getHp() +
            "\nMP: " + p1cmb.getMp() + "/100\n" + o1cmb.getCmbType().getName() + "\nHP: " + o1cmb.getStatsCur().getHp() + "/" +
            o1cmb.getStatsDefault().getHp() + "\nMP: " + o1cmb.getMp() + "/100\n");
    }

    // todo
    public static boolean isMoveValid(SkillTargeting move) {
        return true;
    }
}
