package io.github.celosia.sys.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.github.tommyettinger.textra.TypingLabel;
import io.github.celosia.sys.World;
import io.github.celosia.sys.menu.Fonts.FontType;
import io.github.celosia.sys.menu.MenuLib;
import io.github.celosia.sys.menu.MenuLib.MenuType;
import io.github.celosia.sys.settings.Keybinds;

import java.util.ArrayList;
import java.util.List;

import static io.github.celosia.sys.menu.MenuLib.setTextIfChanged;

public class BattleController {

    // Battle
    static Battle battle;

    // Time to wait in seconds
    static float wait = 0f;

    // Actions being made this turn
    static List<SkillTargeting> moves = new ArrayList<SkillTargeting>();

    // temp
    static Skill[] skills = new Skill[]{Skill.FIREBALL, Skill.HEAL, Skill.ATTACK_UP, Skill.ATTACK_DOWN};
    static Skill[] skills2 = new Skill[]{Skill.FIREBALL, Skill.ICE_BEAM, Skill.THUNDERBOLT, Skill.DEFENSE_DOWN};
    static Skill[] skills3 = new Skill[]{Skill.DEFENSE_UP, Skill.AGILITY_UP, Skill.AGILITY_DOWN, Skill.FAITH_DOWN};

    static int selectingMove = 0; // Who's currently selecting their move. 0-4 = player; 5-9 = opponent; 10 = moves are executing
    static int usingMove = 0; // Who's currently using their move

    static Skill selectedSkill; // Currently selected skill

    // Stat displays for all combatants
    static List<TypingLabel> statsL = new ArrayList<>();

    // Move selection displays
    static List<TypingLabel> movesL = new ArrayList<>();

    // Skill menu display
    static List<TypingLabel> skillsL = new ArrayList<>();

    // Queue (move order) display
    static TypingLabel queue = new TypingLabel("", FontType.KORURI.getSize20());

    static int index = 5;
    static float cooldown = 0f;

    // Affinity MP mult
    static final float[] affMp = {1.7f, 1.5f, 1.3f, 1.2f, 1.1f, 1f, 0.95f, 0.9f, 0.85f, 0.8f, 0.75f};

    // Debug
    static TypingLabel battleLog = new TypingLabel("", FontType.KORURI.getSize20());

    public static void create(Stage stage) {
        // Setup teams (temp)
        Stats johnyStats = new Stats(100, 100, 100, 100, 100, 100, 100);
        CombatantType johny = new CombatantType("Johny", johnyStats, 5, 0, 0, 0 ,0, 0, 0);
        Stats jerryStats = new Stats(100, 100, 100, 100, 100, 100, 115);
        CombatantType jerry = new CombatantType("Jerry", jerryStats, 5, -4, 0, 0 ,0, 0, 0);
        CombatantType james = new CombatantType("James", jerryStats, -4, 5, 0, 0 ,0, 0, 0);
        CombatantType jacob = new CombatantType("Jacob", johnyStats, 0, 0, 5, -4 ,0, 0, 0);
        CombatantType julia = new CombatantType("Julia", johnyStats, 0, 0, -4, 5 ,0, 0, 0);
        CombatantType jude = new CombatantType("Jude", jerryStats, 0, 0, -3, -3 ,5, 0, 0);
        CombatantType josephine = new CombatantType("Josephine", jerryStats, 0, 0, 0, 0 ,0, 5, -4);
        CombatantType julian = new CombatantType("Julian", johnyStats, 0, 0, 0, 0 ,0, -4, 5);
        CombatantType jack = new CombatantType("Jack", johnyStats, 3, -2, 0, 0 ,-3, 3, 0);
        CombatantType jane = new CombatantType("Jane", jerryStats, -2, 3, -2, 3 ,0, 0, -1);

        Team player = new Team(new Combatant[]{new Combatant(johny, 95, johnyStats.getRealStats(95), skills, 20, 0),
            new Combatant(james, 94, jerryStats.getRealStats(94), skills2, 20, 1),
            new Combatant(julia, 93, johnyStats.getRealStats(93), skills3, 20, 2),
            new Combatant(josephine, 92, jerryStats.getRealStats(92), skills2, 20, 3),
            new Combatant(jack, 91, johnyStats.getRealStats(91), skills, 20, 4)});

        Team opponent = new Team(new Combatant[]{new Combatant(jerry, 100, jerryStats.getRealStats(100), skills2, 20, 5),
            new Combatant(jacob, 99, johnyStats.getRealStats(99), skills2, 20, 6),
            new Combatant(jude, 98, jerryStats.getRealStats(98), skills2, 20, 7),
            new Combatant(julian, 97, johnyStats.getRealStats(97), skills2, 20, 8),
            new Combatant(jane, 96, jerryStats.getRealStats(96), skills2, 20, 9)});

        battle = new Battle(player, opponent);

        for(int i = 0; i < 10; i++) {
            int y = (i >= 5) ? World.HEIGHT - 400 - 200 * (i - 5) : World.HEIGHT - 400 - 200 * i;

            // Stat displays for all combatants
            statsL.add(new TypingLabel("", FontType.KORURI.getSize20()));
            statsL.get(i).setPosition((i >= 5) ? World.WIDTH - 250 : 150, y);
            stage.addActor(statsL.get(i));

            // Move selection displays
            movesL.add(new TypingLabel("", FontType.KORURI.getSize20()));
            movesL.get(i).setPosition((i >= 5) ? World.WIDTH - 550 : 400, y);
            stage.addActor(movesL.get(i));
        }

        // Skill menu display
        for(int i = 0; i < 4; i++) {
            skillsL.add(new TypingLabel("", FontType.KORURI.getSize20()));
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
                setTextIfChanged(movesL.get(selectingMove), "Z: Attack\nX: Defend\nC: Skills");
                if (selectingMove < battle.getPlayerTeam().getCmbs().length) { // if there are more allies yet to act
                    if (Gdx.input.isKeyJustPressed(Keybinds.ATTACK.getKey())) {
                        selectedSkill = Skill.ATTACK;
                        setTextIfChanged(movesL.get(selectingMove), selectedSkill.getName());
                        wait += 0.15f;

                        // Prepare menus
                        index = 5;
                        cooldown = 0f;

                        return MenuType.TARGETING;
                    } else if (Gdx.input.isKeyJustPressed(Keybinds.DEFEND.getKey())) {
                        selectedSkill = Skill.DEFEND;
                        setTextIfChanged(movesL.get(selectingMove), selectedSkill.getName());
                        wait += 0.15f;

                        // Prepare menus
                        index = 5;
                        cooldown = 0f;

                        return MenuType.TARGETING;
                    } else if (Gdx.input.isKeyJustPressed(Keybinds.SKILL.getKey())) {
                        setTextIfChanged(movesL.get(selectingMove), "Skill");
                        wait += 0.15f;

                        // Skill selection display
                        for(int i = 0; i < 4; i++) {
                            skillsL.get(i).setPosition(600, (World.HEIGHT - 400 - 200 * selectingMove) - ((i - 2) * 35));
                            setTextIfChanged(skillsL.get(i), battle.getPlayerTeam().getCmbs()[selectingMove].getSkills()[i].getName());
                        }

                        // Prepare menus
                        index = 0;
                        cooldown = 0f;

                        return MenuType.SKILLS;
                    }
                } else selectingMove = 5; // Jump to opponent move selection
            } else if (selectingMove <= 9) { // opponent's turn
                if ((selectingMove - 5) < battle.getOpponentTeam().getCmbs().length) { // if there are more opponents yet to act
                    Skill selectedSkill = skills2[MathUtils.random(skills.length - 1)];
                    Combatant target = battle.getPlayerTeam().getCmbs()[MathUtils.random(battle.getPlayerTeam().getCmbs().length - 1)];
                    setTextIfChanged(movesL.get(selectingMove), selectedSkill.getName() + " -> " + target.getCmbType().getName());
                    moves.add(new SkillTargeting(selectedSkill, battle.getOpponentTeam().getCmbs()[selectingMove - 5], target)); // todo AI
                    selectingMove++;
                } else selectingMove = 10; // Jump to move execution
            } else if (selectingMove == 10) { // Moves play out

                // All moves have happened; end turn
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

                    for (Combatant cmb : battle.getAllCombatants()) {
                        // Increase MP
                        cmb.setMp(cmb.getMp() + 10);

                        // Apply buff turn end effects
                        for(BuffInstance buffInstance : cmb.getBuffInstances()) {
                            for(BuffEffect buffEffect : buffInstance.getBuff().getBuffEffects()) {
                                buffEffect.onTurnEnd(cmb);
                            }
                        }

                        // Decrement buff turns and remove expired buffs
                        cmb.decrementBuffTurns();
                        cmb.decrementStageTurns();
                    }

                    // Increase bloom
                    battle.getPlayerTeam().setBloom(battle.getPlayerTeam().getBloom() + 5);
                    battle.getOpponentTeam().setBloom(battle.getOpponentTeam().getBloom() + 5);

                    // todo check if battle is over

                    return MenuType.BATTLE;
                }

                // Sort moves by Agi
                moves.sort((a, b) -> Integer.compare(
                    b.getSelf().getAgiWithStage(),
                    a.getSelf().getAgiWithStage()
                ));

                // The next move plays out
                SkillTargeting move = moves.get(0);

                if (isMoveValid(move)) {
                    // Execute move
                    if(move.getSkill().isBloom()) {
                        // todo
                    } else { // Use MP
                        Element element = move.getSkill().getElement();
                        move.getSelf().setMp(move.getSelf().getMp() - (int) (move.getSkill().getCost() * ((element == Element.VIS) ? 1 : affMp[move.getSelf().getCmbType().getAffs()[element.ordinal() - 1] + 5])));
                    }

                    // Apply all SkillEffects
                    for (SkillEffect effect : move.getSkill().getSkillEffects()) {
                        if(!effect.apply(move.getSelf(), move.getTarget())) break; // Stop executing Skill if any SkillEffect fails
                    }

                    // Apply on-skill use buffs
                    for(BuffInstance buffInstance : move.getSelf().getBuffInstances()) {
                        for(BuffEffect buffEffect : buffInstance.getBuff().getBuffEffects()) {
                            buffEffect.onUseSkill(move.getSelf(), move.getTarget());
                        }
                    }

                    // Move stat display for currently acting combatant (temp)
                    for(int i = 0; i < 10; i++) {
                        statsL.get(i).setX(((move.getSelf().getPos()) == i) ? (i >= 5) ? World.WIDTH - 300 : 200 : (i >= 5) ? World.WIDTH - 250 : 150);
                        if(move.getSelf().getPos() == i) {
                            movesL.get(i).setColor(Color.PINK);
                        }
                    }

                    // todo delete killed combatants

                    usingMove++;
                }

                // Wait 1s between moves
                wait++;

                moves.remove(0);
            }
        } else if(menuType == MenuType.TARGETING) { // Picking a target

            if(Gdx.input.isKeyJustPressed(Keybinds.BACK.getKey())) {
                // Reset for next time
                for(TypingLabel stat : statsL) stat.setColor(Color.WHITE);

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
                setTextIfChanged(movesL.get(selectingMove), movesL.get(selectingMove).getOriginalText() + " -> " + target.getCmbType().getName());

                selectingMove++;

                // Reset for next time
                for(TypingLabel stat : statsL) stat.setColor(Color.WHITE);

                return MenuType.BATTLE;
            } else return MenuType.TARGETING;
        } else if(menuType == MenuType.SKILLS) { // Picking a skill

            if(Gdx.input.isKeyJustPressed(Keybinds.BACK.getKey())) {
                // Reset for next time
                for(TypingLabel skill : skillsL) {
                    skill.setText("");
                    skill.setColor(Color.WHITE);
                }

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
                selectedSkill = battle.getPlayerTeam().getCmbs()[selectingMove].getSkills()[index];
                setTextIfChanged(movesL.get(selectingMove), "Skill: " + selectedSkill.getName());

                // Reset for next time
                for(TypingLabel skill : skillsL) {
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
        for (int i = 0; i < 10; i++) {
            Combatant cmb = (i >= 5) ? battle.getOpponentTeam().getCmbs()[i - 5] : battle.getPlayerTeam().getCmbs()[i]; // todo can use battle.getAllCmbs
            if (cmb != null) {
                StringBuilder text = new StringBuilder(cmb.getCmbType().getName() + "\nHP: " + cmb.getStatsCur().getHp() + "/" + cmb.getStatsDefault().getHp() + "\nMP: " + cmb.getMp() +
                    "/100\nStr: " + cmb.getStrWithStage() + "/" + cmb.getStatsDefault().getStr() + "\nMag:" + cmb.getMagWithStage() + "/" + cmb.getStatsDefault().getMag() +
                    "\nAmr: " + cmb.getAmrWithStage() + "/" + cmb.getStatsDefault().getAmr() + "\nRes: " + cmb.getResWithStage() + "/" + cmb.getStatsDefault().getRes() +"\n");

                // List stage changes
                for(StageType stageType : StageType.values()) {
                    int stage = cmb.getStage(stageType);
                    if(stage != 0) {
                        text.append(stageType.getName()).append((stage >= 1) ? "+" : "").append(stage).append("(").append(cmb.getStageTurns(stageType)).append(") ");
                    }
                }

                // List buffs
                List<BuffInstance> buffInstances = cmb.getBuffInstances();
                if(!buffInstances.isEmpty()) {
                    for (BuffInstance buffInstance : buffInstances) {
                        text.append(buffInstance.getBuff().getName()).append("x").append(buffInstance.getStacks()).append("(").append(buffInstance.getTurns()).append(") ");
                    }
                }
                setTextIfChanged(statsL.get(i), text.toString());
            } else statsL.get(i).setText("");
        }

        // Queue
        // Sort combatants by Agi
        List<Combatant> cmbsAll = battle.getAllCombatants();

        cmbsAll.sort((a, b) -> Integer.compare(
            b.getAgiWithStage(),
            a.getAgiWithStage()
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
        queue.skipToTheEnd();
        //setTextIfChanged(queue, queueText.toString());

        // Debug
        setTextIfChanged(battleLog, "index = " + index + "\nturn = " + battle.getTurn() + "\nmoves = " + moves + "\nselectingMove = " + selectingMove);
    }

    // todo
    public static boolean isMoveValid(SkillTargeting move) {
        return true;
    }
}
