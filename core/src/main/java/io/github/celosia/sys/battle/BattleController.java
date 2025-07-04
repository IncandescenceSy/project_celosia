package io.github.celosia.sys.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.github.tommyettinger.textra.TextraLabel;
import io.github.celosia.sys.World;
import io.github.celosia.sys.menu.LabelStyles;
import io.github.celosia.sys.menu.MenuLib;
import io.github.celosia.sys.menu.MenuLib.MenuType;
import io.github.celosia.sys.settings.Keybinds;

import java.util.ArrayList;
import java.util.List;

public class BattleController {

    // Battle
    static Battle battle;

    // Time to wait in seconds
    static float wait = 0f;

    // Actions being made this turn
    static List<SkillTargeting> moves = new ArrayList<SkillTargeting>();

    // temp
    static Skill[] skills = new Skill[]{Skill.FIREBALL, Skill.ICE_BEAM, Skill.THUNDERBOLT, Skill.HEAL};

    static int selectingMove = 0; // Who's currently selecting their move. 0-4 = player; 5-9 = opponent; 10 = moves are executing
    static int usingMove = 0; // Who's currently using their move

    static Skill selectedSkill; // Currently selected skill

    // Stat displays for all combatants
    static List<Label> statsL = new ArrayList<>();

    // Move selection displays
    static List<Label> movesL = new ArrayList<>();

    // Skill menu display
    static List<Label> skillsL = new ArrayList<>();

    // Queue (move order) display
    static TextraLabel queue = new TextraLabel("", LabelStyles.KORURI_20_T);

    static int index = 5;
    static float cooldown = 0f;

    // Debug
    static Label battleLog = new Label("", LabelStyles.KORURI_20);

    public static void create(Stage stage) {
        // Setup teams (temp)
        Stats johnyStats = new Stats(100, 100, 100, 100, 100, 100, 100);
        CombatantType johny = new CombatantType("Johny", johnyStats, 0, 0, 0, 0 ,0, 0, 0);
        Stats jerryStats = new Stats(100, 100, 100, 100, 100, 100, 115);
        CombatantType jerry = new CombatantType("Jerry", jerryStats, 0, 0, 0, 0 ,0, 0, 0);
        CombatantType james = new CombatantType("James", jerryStats, 0, 0, 0, 0 ,0, 0, 0);
        CombatantType jacob = new CombatantType("Jacob", johnyStats, 0, 0, 0, 0 ,0, 0, 0);
        CombatantType julia = new CombatantType("Julia", johnyStats, 0, 0, 0, 0 ,0, 0, 0);
        CombatantType jude = new CombatantType("Jude", jerryStats, 0, 0, 0, 0 ,0, 0, 0);
        CombatantType josephine = new CombatantType("Josephine", jerryStats, 0, 0, 0, 0 ,0, 0, 0);
        CombatantType julian = new CombatantType("Julian", johnyStats, 0, 0, 0, 0 ,0, 0, 0);
        CombatantType jack = new CombatantType("Jack", johnyStats, 0, 0, 0, 0 ,0, 0, 0);
        CombatantType jane = new CombatantType("Jane", jerryStats, 0, 0, 0, 0 ,0, 0, 0);

        Team player = new Team(new Combatant[]{new Combatant(johny, 95, johnyStats.getRealStats(95), skills, 20, 0),
            new Combatant(james, 94, jerryStats.getRealStats(94), skills, 20, 1),
            new Combatant(julia, 93, johnyStats.getRealStats(93), skills, 20, 2),
            new Combatant(josephine, 92, jerryStats.getRealStats(92), skills, 20, 3),
            new Combatant(jack, 91, johnyStats.getRealStats(91), skills, 20, 4)});

        Team opponent = new Team(new Combatant[]{new Combatant(jerry, 100, jerryStats.getRealStats(100), skills, 20, 5),
            new Combatant(jacob, 99, johnyStats.getRealStats(99), skills, 20, 6),
            new Combatant(jude, 98, jerryStats.getRealStats(98), skills, 20, 7),
            new Combatant(julian, 97, johnyStats.getRealStats(97), skills, 20, 8),
            new Combatant(jane, 96, jerryStats.getRealStats(96), skills, 20, 9)});

        battle = new Battle(player, opponent);

        for(int i = 0; i < 10; i++) {
            int y = (i >= 5) ? World.HEIGHT - 400 - 200 * (i - 5) : World.HEIGHT - 400 - 200 * i;

            // Stat displays for all combatants
            statsL.add(new Label("", LabelStyles.KORURI_20));
            statsL.get(i).setPosition((i >= 5) ? World.WIDTH - 250 : 150, y);
            stage.addActor(statsL.get(i));

            // Move selection displays
            movesL.add(new Label("", LabelStyles.KORURI_20));
            movesL.get(i).setPosition((i >= 5) ? World.WIDTH - 550 : 400, y);
            stage.addActor(movesL.get(i));
        }

        // Skill menu display
        for(int i = 0; i < 4; i++) {
            skillsL.add(new Label("", LabelStyles.KORURI_20));
            stage.addActor(skillsL.get(i));
        }

        // Queue (move order) display
        queue.setPosition(50, World.HEIGHT - 200);
        stage.addActor(queue);

        // Debug
        battleLog.setPosition(World.WIDTH_2, 200, Align.center);
        stage.addActor(battleLog);
    }

    public static MenuType input(MenuType menuType) {
        if(wait > 0) {
            wait -= Gdx.graphics.getDeltaTime();
        } else if (menuType == MenuType.BATTLE) { // Selecting moves

            // Move stat display for currently selecting combatant
            for(int i = 0; i < 5; i++) {
                statsL.get(i).setX((i == selectingMove) ? 200 : 150);
            }

            if (selectingMove <= 4) { // Player's turn
                movesL.get(selectingMove).setText("Z: Attack\nX: Defend\nC: Skills");
                if (selectingMove < battle.getPlayerTeam().getCmbs().length) { // if there are more allies yet to act
                    if (Gdx.input.isKeyJustPressed(Keybinds.ATTACK.getKey())) {
                        selectedSkill = Skill.ATTACK;
                        movesL.get(selectingMove).setText(selectedSkill.getName());
                        wait += 0.15f;

                        // Prepare menus
                        index = 5;
                        cooldown = 0f;

                        return MenuType.TARGETING;
                    } else if (Gdx.input.isKeyJustPressed(Keybinds.DEFEND.getKey())) {
                        selectedSkill = Skill.DEFEND;
                        movesL.get(selectingMove).setText(selectedSkill.getName());
                        wait += 0.15f;

                        // Prepare menus
                        index = 5;
                        cooldown = 0f;

                        return MenuType.TARGETING;
                    } else if (Gdx.input.isKeyJustPressed(Keybinds.SKILL.getKey())) {
                        movesL.get(selectingMove).setText("Skill");
                        wait += 0.15f;

                        // Skill selection display
                        for(int i = 0; i < 4; i++) {
                            skillsL.get(i).setPosition(600, (World.HEIGHT - 400 - 200 * selectingMove) - ((i - 2) * 35));
                            skillsL.get(i).setText(battle.getPlayerTeam().getCmbs()[selectingMove].getSkills()[i].getName());
                        }

                        // Prepare menus
                        index = 0;
                        cooldown = 0f;

                        return MenuType.SKILLS;
                    }
                } else selectingMove = 5; // Jump to opponent move selection
            } else if (selectingMove <= 9) { // opponent's turn
                if ((selectingMove - 5) < battle.getOpponentTeam().getCmbs().length) { // if there are more opponents yet to act
                    Skill selectedSkill = skills[MathUtils.random(skills.length - 1)];
                    Combatant target = battle.getPlayerTeam().getCmbs()[MathUtils.random(battle.getPlayerTeam().getCmbs().length - 1)];
                    movesL.get(selectingMove).setText("Skill: " + selectedSkill.getName() + " -> " + target.getCmbType().getName());
                    moves.add(new SkillTargeting(selectedSkill, battle.getOpponentTeam().getCmbs()[selectingMove - 5], target)); // todo AI
                    selectingMove++;
                } else selectingMove = 10; // Jump to move execution
            } else if (selectingMove == 10) { // Moves play out

                // All moves have happened
                if (moves.isEmpty()) {
                    selectingMove = 0;
                    usingMove = 0;
                    battle.setTurn(battle.getTurn() + 1);

                    // Reset stat/move displays to normal
                    for(int i = 0; i < 10; i++) {
                        statsL.get(i).setX((i >= 5) ? World.WIDTH - 250 : 150);
                        movesL.get(i).setText("");
                        movesL.get(i).setColor(Color.WHITE);
                    }

                    // Increase MP and Bloom
                    for (Combatant cmb : battle.getPlayerTeam().getCmbs()) cmb.setMp(cmb.getMp() + 10);
                    for (Combatant cmb : battle.getOpponentTeam().getCmbs()) cmb.setMp(cmb.getMp() + 10);

                    battle.getPlayerTeam().setBloom(battle.getPlayerTeam().getBloom() + 5);
                    battle.getOpponentTeam().setBloom(battle.getOpponentTeam().getBloom() + 5);

                    return MenuType.BATTLE;
                }

                // Sort moves by Agi
                moves.sort((a, b) -> Integer.compare(
                    b.getSelf().getStatsCur().getAgi(),
                    a.getSelf().getStatsCur().getAgi()
                ));

                // The next move plays out
                SkillTargeting move = moves.get(0);

                if (isMoveValid(move)) {
                    // Execute move
                    if(move.getSkill().isBloom()) {
                        // todo
                    } else {
                        move.getSelf().setMp(move.getSelf().getMp() - move.getSkill().getCost());
                    }

                    for (SkillEffect effect : move.getSkill().getSkillEffects()) {
                        effect.apply(move.getSelf(), move.getTarget());
                    }

                    // Move stat display for currently acting combatant
                    for(int i = 0; i < 10; i++) {
                        statsL.get(i).setX(((move.getSelf().getPos()) == i) ? (i >= 5) ? World.WIDTH - 300 : 200 : (i >= 5) ? World.WIDTH - 250 : 150);
                        if(move.getSelf().getPos() == i) {
                            movesL.get(i).setColor(Color.PINK);
                        }
                    }

                    usingMove++;
                }

                // Wait 1s between moves
                wait++;

                moves.remove(0);
            }
        } else if(menuType == MenuType.TARGETING) { // Picking a target

            if(Gdx.input.isKeyJustPressed(Keybinds.BACK.getKey())) {
                // Reset for next time
                for(Label stat : statsL) stat.setColor(Color.WHITE);

                return MenuType.BATTLE;
            }

            // Handle menu navigation
            Number[] menuStats = MenuLib.handleMenuTargeting(index, cooldown);
            index = (int) menuStats[0];
            cooldown = (float) menuStats[1];

            // Handle option colors
            MenuLib.handleOptColor(statsL, index);

            // Add selection to move queue
            if(Gdx.input.isKeyJustPressed(Keybinds.CONFIRM.getKey())) {
                Combatant target = (index < 5) ? battle.getPlayerTeam().getCmbs()[index] : battle.getOpponentTeam().getCmbs()[index - 5];
                moves.add(new SkillTargeting(selectedSkill, battle.getPlayerTeam().getCmbs()[selectingMove], target));
                movesL.get(selectingMove).setText(movesL.get(selectingMove).getText() + " -> " + target.getCmbType().getName());

                selectingMove++;

                // Reset for next time
                for(Label stat : statsL) stat.setColor(Color.WHITE);

                return MenuType.BATTLE;
            } else return MenuType.TARGETING;
        } else if(menuType == MenuType.SKILLS) { // Picking a skill

            if(Gdx.input.isKeyJustPressed(Keybinds.BACK.getKey())) {
                // Reset for next time
                for(Label skill : skillsL) skill.setColor(Color.WHITE);

                return MenuType.BATTLE;
            }

            // Handle menu navigation
            Number[] menuStats = MenuLib.handleMenu1D(index, skillsL.size(), cooldown);
            index = (int) menuStats[0];
            cooldown = (float) menuStats[1];

            // Handle option colors
            MenuLib.handleOptColor(skillsL, index);

            // Move selected
            if(Gdx.input.isKeyJustPressed(Keybinds.CONFIRM.getKey())) {
                selectedSkill = skills[index];
                movesL.get(selectingMove).setText("Skill: " + selectedSkill.getName());

                // Reset for next time
                for(Label skill : skillsL) {
                    skill.setText("");
                    skill.setColor(Color.WHITE);
                }

                // Prepare menus
                index = 5;
                cooldown = 0f;

                return MenuType.TARGETING;
            }
        }

        return menuType;
    }

    public static void updateStatDisplay() {
        // Update combatant stat display
        // todo lang and disambiguation + system to display all status updates
        // Player
        for (int i = 0; i < 5; i++) {
            Combatant cmb = battle.getPlayerTeam().getCmbs()[i];
            if (cmb != null) {
                statsL.get(i).setText(cmb.getCmbType().getName() + "\nHP: " + cmb.getStatsCur().getHp() + "/" + cmb.getStatsDefault().getHp() + "\nMP: " + cmb.getMp() + "/100");
            } else statsL.get(i).setText("");
        }
        // Opponent (todo merge)
        for (int i = 5; i < 10; i++) {
            Combatant cmb = battle.getOpponentTeam().getCmbs()[i - 5];
            if (cmb != null) {
                statsL.get(i).setText(cmb.getCmbType().getName() + "\nHP: " + cmb.getStatsCur().getHp() + "/" + cmb.getStatsDefault().getHp() + "\nMP: " + cmb.getMp() + "/100");
            } else statsL.get(i).setText("");
        }

        // Queue
        // Sort combatants by Agi
        List<Combatant> cmbsAll = battle.getAllCombatants();

        cmbsAll.sort((a, b) -> Integer.compare(
            b.getStatsCur().getAgi(),
            a.getStatsCur().getAgi()
        ));

        StringBuilder queueText = new StringBuilder().append("Queue: ");

        for(int i = 0; i < cmbsAll.size(); i++) {
            boolean active = (i + 1) == usingMove || cmbsAll.get(i).getPos() == selectingMove;
            if(active) queueText.append("[PINK][[");
            queueText.append(cmbsAll.get(i).getCmbType().getName());
            if(active) {
                queueText.append("][WHITE]");
            }
            if(i != cmbsAll.size() - 1) queueText.append(", ");
        }

        queue.setText(queueText.toString());

        // Debug
        battleLog.setText("index = " + index + "\nturn = " + battle.getTurn() + "\nmoves = " + moves + "\nselectingMove = " + selectingMove);
    }

    // todo
    public static boolean isMoveValid(SkillTargeting move) {
        return true;
    }
}
