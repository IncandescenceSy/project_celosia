package io.github.celosia.sys.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.github.tommyettinger.textra.TypingLabel;
import io.github.celosia.sys.InputHandler;
import io.github.celosia.sys.World;
import io.github.celosia.sys.menu.Fonts.FontType;
import io.github.celosia.sys.menu.InputLib;
import io.github.celosia.sys.menu.MenuLib;
import io.github.celosia.sys.menu.MenuLib.MenuType;
import io.github.celosia.sys.settings.Keybind;

import java.util.ArrayList;
import java.util.List;

import static io.github.celosia.sys.menu.MenuLib.setTextIfChanged;
import static io.github.celosia.sys.settings.Lang.lang;

public class BattleController {

    // Battle
    static Battle battle;

    // Time to wait in seconds
    static float wait = 0f;

    // Affinity SP mult
    static final float[] affSp = {1.7f, 1.5f, 1.3f, 1.2f, 1.1f, 1f, 0.95f, 0.9f, 0.85f, 0.8f, 0.75f};

    // Actions being made this turn
    static List<SkillTargeting> moves = new ArrayList<>();

    static int selectingMove = 0; // Who's currently selecting their move. 0-3 = player; 4-8 = opponent; 100 = moves are executing
    static int usingMove = 0; // Who's currently using their move

    // Menu navigation
    static int index = 5;

    static Skill selectedSkill; // Currently selected skill

    // Display
    // Turn display
    static TypingLabel turn = new TypingLabel("{SPEED=0.1}{FADE}{SLIDE}" + lang.get("turn") + "1", FontType.KORURI.getSize60());

    // Bloom displays for both teams
    static List<TypingLabel> bloomL = new ArrayList<>();

    // Queue (move order) display
    static TypingLabel queue = new TypingLabel("", FontType.KORURI.getSize30());

    // Stat displays for all combatants
    static List<TypingLabel> statsL = new ArrayList<>();

    // Move selection displays
    static List<TypingLabel> movesL = new ArrayList<>();

    // Skill menu display
    static List<TypingLabel> skillsL = new ArrayList<>();

    // temp
    static Skill[] skills = new Skill[]{Skill.FIREBALL, Skill.HEAL, Skill.OVERHEAL, Skill.DEMON_SCYTHE};
    static Skill[] skills2 = new Skill[]{Skill.FIREBALL, Skill.ICE_BEAM, Skill.BARRIER, Skill.PROTECT};
    static Skill[] skills3 = new Skill[]{Skill.THUNDERBOLT, Skill.BARRIER, Skill.ZEPHYR_LANCE, Skill.JET_STREAM};

    // Debug
    //static TypingLabel battleLog = new TypingLabel("", FontType.KORURI.getSize30());

    public static void create(Stage stage) {
        // Setup teams (temp)
        Stats johnyStats = new Stats(100, 100, 100, 100, 100, 100, 100);
        CombatantType johny = new CombatantType("Johny", johnyStats, 4, -4, 0, 0 ,0, 0, 0);
        Stats jerryStats = new Stats(100, 100, 100, 100, 100, 100, 115);
        CombatantType jerry = new CombatantType("Jerry", jerryStats, 5, -4, 0, 5 ,0, 0, 0);
        CombatantType james = new CombatantType("James", jerryStats, -4, 5, 0, 0 ,0, 0, 0);
        CombatantType jacob = new CombatantType("Jacob", johnyStats, 0, 0, 5, -4 ,0, 0, 0);
        CombatantType julia = new CombatantType("Julia", johnyStats, 0, 0, -4, 5 ,0, 0, 0);
        CombatantType jude = new CombatantType("Jude", jerryStats, 0, 0, -3, -3 ,5, 0, 0);
        CombatantType josephine = new CombatantType("Josephine", jerryStats, 0, 0, 0, 0 ,0, 5, -4);
        CombatantType julian = new CombatantType("Julian", johnyStats, 0, 0, 0, 0 ,0, -4, 5);

        Team player = new Team(new Combatant[]{new Combatant(johny, 95, johnyStats.getRealStats(95), skills, 20, 0),
            new Combatant(james, 94, jerryStats.getRealStats(94), skills2, 20, 1),
            new Combatant(julia, 93, johnyStats.getRealStats(93), skills3, 20, 2),
            new Combatant(josephine, 92, jerryStats.getRealStats(92), skills, 20, 3)});

        Team opponent = new Team(new Combatant[]{new Combatant(jerry, 100, jerryStats.getRealStats(100), skills2, 20, 5),
            new Combatant(jacob, 99, johnyStats.getRealStats(99), skills2, 20, 6),
            new Combatant(jude, 98, jerryStats.getRealStats(98), skills2, 20, 7),
            new Combatant(julian, 97, johnyStats.getRealStats(97), skills2, 20, 8)});

        battle = new Battle(player, opponent);

        // Turn display
        turn.setPosition(World.WIDTH_2, World.HEIGHT - 60, Align.center);
        stage.addActor(turn);

        for(int i = 0; i < 2; i++) {
            // Bloom displays for both teams
            TypingLabel bloom = new TypingLabel(lang.get("bloom") + ": 0/100", FontType.KORURI.getSize40());
            bloomL.add(bloom);
            bloom.setY(World.HEIGHT - 90);
            stage.addActor(bloom);
        }

        // Queue (move order) display
        stage.addActor(queue);

        for(int i = 0; i < 8; i++) {
            int y = (i >= 4) ? World.HEIGHT - 300 - 300 * (i - 4) : World.HEIGHT - 300 - 300 * i;

            // Stat displays for all combatants
            TypingLabel stats = new TypingLabel("", FontType.KORURI.getSize30());
            statsL.add(stats);
            stats.setPosition((i >= 4) ? World.WIDTH - 350 : 50, y);
            stage.addActor(stats);

            // Move selection displays
            TypingLabel moves = new TypingLabel("", FontType.KORURI.getSize30());
            movesL.add(moves);
            moves.setPosition((i >= 4) ? World.WIDTH - 550 : 400, y);
            stage.addActor(moves);
        }

        // Skill menu display
        for(int i = 0; i < 4; i++) {
            skillsL.add(new TypingLabel("", FontType.KORURI.getSize30()));
            stage.addActor(skillsL.get(i));
        }

        // Debug
        //battleLog.setPosition(World.WIDTH_2, 200, Align.center);
        //stage.addActor(battleLog);
    }

    public static MenuType input(MenuType menuType, Controller controller) {
        if (wait > 0f) {
            wait -= Gdx.graphics.getDeltaTime();
        } else if (menuType == MenuType.BATTLE) { // Selecting moves

            if (selectingMove <= 3) { // Player's turn
                setTextIfChanged(movesL.get(selectingMove), ((InputHandler.isLastUsedController()) ? Keybind.CONFIRM.getButton().getName() : Input.Keys.toString(Keybind.CONFIRM.getKey())) + ": " + lang.get("skill.attack") +
                    "\n" + ((InputHandler.isLastUsedController()) ? Keybind.BACK.getButton().getName() : Input.Keys.toString(Keybind.BACK.getKey())) + ": " + lang.get("skill.defend") +
                    "\n" + ((InputHandler.isLastUsedController()) ? Keybind.MENU.getButton().getName() : Input.Keys.toString(Keybind.MENU.getKey())) + ": " + lang.get("skills"));
                if (selectingMove < battle.getPlayerTeam().getCmbs().length) { // if there are more allies yet to act
                    if (InputLib.checkInput(controller, Keybind.CONFIRM)) {
                        selectedSkill = Skill.ATTACK;
                        setTextIfChanged(movesL.get(selectingMove), selectedSkill.getName());

                        // Prepare menus
                        index = 4;

                        return MenuType.TARGETING;
                    } else if (InputLib.checkInput(controller, Keybind.BACK)) {
                        selectedSkill = Skill.DEFEND;
                        setTextIfChanged(movesL.get(selectingMove), selectedSkill.getName());

                        // Prepare menus
                        index = 4;

                        return MenuType.TARGETING;
                    } else if (InputLib.checkInput(controller, Keybind.MENU)) {
                        setTextIfChanged(movesL.get(selectingMove), lang.get("skill"));

                        // Skill selection display
                        for(int i = 0; i < 4; i++) {
                            skillsL.get(i).setPosition(600, (World.HEIGHT - 400 - 200 * selectingMove) - ((i - 2) * 35));
                            setTextIfChanged(skillsL.get(i), battle.getPlayerTeam().getCmbs()[selectingMove].getSkills()[i].getName());
                        }

                        // Prepare menus
                        index = 0;

                        return MenuType.SKILLS;
                    }
                } else selectingMove = 4; // Jump to opponent move selection
            } else if (selectingMove <= 8) { // opponent's turn
                if ((selectingMove - 4) < battle.getOpponentTeam().getCmbs().length) { // if there are more opponents yet to act
                    //Skill selectedSkill = skills2[MathUtils.random(skills.length - 1)];
                    Skill selectedSkill = Skill.NOTHING;
                    Combatant target = battle.getPlayerTeam().getCmbs()[MathUtils.random(battle.getPlayerTeam().getCmbs().length - 1)];
                    setTextIfChanged(movesL.get(selectingMove), selectedSkill.getName() + " -> " + target.getCmbType().getName());
                    moves.add(new SkillTargeting(selectedSkill, battle.getOpponentTeam().getCmbs()[selectingMove - 4], target)); // todo AI
                    selectingMove++;
                } else selectingMove = 100; // Jump to move execution
            } else if (selectingMove == 100) { // Moves play out

                // All moves have happened; end turn
                if (moves.isEmpty()) {
                    selectingMove = 0;
                    usingMove = 0;
                    battle.setTurn(battle.getTurn() + 1);

                    // Update turn display
                    turn.setText("Turn " + (battle.getTurn() + 1));

                    // Reset stat/move displays to normal
                    for(int i = 0; i < 8; i++) {
                        //statsL.get(i).setX((i >= 4) ? World.WIDTH - 250 : 150);
                        movesL.get(i).setText("");
                        movesL.get(i).setColor(Color.WHITE);
                    }

                    for (Combatant cmb : battle.getAllCombatants()) {
                        // Increase MP
                        cmb.setSp(cmb.getSp() + 10);

                        // Apply buff turn end effects
                        for(BuffInstance buffInstance : cmb.getBuffInstances()) {
                            for(BuffEffect buffEffect : buffInstance.getBuff().getBuffEffects()) {
                                buffEffect.onTurnEnd(cmb);
                            }
                        }

                        // Decrement stage/barrier/buff turns and remove expired stages/barriers/buffs
                        cmb.decrementTurns();
                    }

                    // Increase bloom
                    battle.getPlayerTeam().setBloom(battle.getPlayerTeam().getBloom() + 5);
                    battle.getOpponentTeam().setBloom(battle.getOpponentTeam().getBloom() + 5);

                    // todo check if battle is over

                    return MenuType.BATTLE;
                }

                // Sort moves by Agi
                // todo priority
                moves.sort((a, b) -> Integer.compare(
                    b.getSelf().getAgiWithStage(),
                    a.getSelf().getAgiWithStage()
                ));

                // The next move plays out
                SkillTargeting move = moves.get(0);

                if (isMoveValid(move)) {
                    // Execute move
                    if (move.getSkill().isBloom()) {
                        // todo
                    } else { // Use MP
                        Element element = move.getSkill().getElement();
                        move.getSelf().setSp(move.getSelf().getSp() - (int) (move.getSkill().getCost() * ((element == Element.VIS) ? 1 : affSp[move.getSelf().getCmbType().getAffs()[element.ordinal() - 1] + 5])));
                    }

                    // Apply all SkillEffects
                    // todo apply over a period of time
                    Result result = Result.SUCCESS;
                    for (SkillEffect effect : move.getSkill().getSkillEffects()) {
                        result = effect.apply(move.getSelf(), move.getTarget(), result);
                        if (result == Result.FAIL) break; // Stop executing skill if any SkillEffect fails entirely
                    }

                    // Apply on-skill use buffs
                    for(BuffInstance buffInstance : move.getSelf().getBuffInstances()) {
                        for(BuffEffect buffEffect : buffInstance.getBuff().getBuffEffects()) {
                            buffEffect.onUseSkill(move.getSelf(), move.getTarget());
                        }
                    }

                    // Move stat display for currently acting combatant (temp)
                    for(int i = 0; i < 8; i++) {
                        //statsL.get(i).setX(((move.getSelf().getPos()) == i) ? (i >= 4) ? World.WIDTH - 300 : 200 : (i >= 4) ? World.WIDTH - 250 : 150);
                        if (move.getSelf().getPos() == i) {
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
        } else if (menuType == MenuType.TARGETING) { // Picking a target

            if (InputLib.checkInput(controller, Keybind.BACK)) {
                // Reset for next time
                for(TypingLabel stat : statsL) stat.setColor(Color.WHITE);

                return MenuType.BATTLE;
            }

            // Handle menu navigation
            index = MenuLib.checkMovementTargeting(index, controller);

            // Handle option colors
            MenuLib.handleOptColor(statsL, index);

            // Add selection to move queue
            if (InputLib.checkInput(controller, Keybind.CONFIRM)) {
                Combatant target = (index < 4) ? battle.getPlayerTeam().getCmbs()[index] : battle.getOpponentTeam().getCmbs()[index - 4];
                moves.add(new SkillTargeting(selectedSkill, battle.getPlayerTeam().getCmbs()[selectingMove], target));
                setTextIfChanged(movesL.get(selectingMove), movesL.get(selectingMove).getOriginalText() + " -> " + target.getCmbType().getName());

                selectingMove++;

                // Reset for next time
                for(TypingLabel stat : statsL) stat.setColor(Color.WHITE);

                return MenuType.BATTLE;
            } else return MenuType.TARGETING;
        } else if (menuType == MenuType.SKILLS) { // Picking a skill

            if (InputLib.checkInput(controller, Keybind.BACK)) {
                // Reset for next time
                for(TypingLabel skill : skillsL) {
                    skill.setText("");
                    skill.setColor(Color.WHITE);
                }

                return MenuType.BATTLE;
            }

            // Handle menu navigation
            index = MenuLib.checkMovement1D(index, skillsL.size(), controller);

            // Handle option colors
            MenuLib.handleOptColor(skillsL, index);

            // Move selected
            if (InputLib.checkInput(controller, Keybind.CONFIRM)) {
                selectedSkill = battle.getPlayerTeam().getCmbs()[selectingMove].getSkills()[index];
                setTextIfChanged(movesL.get(selectingMove), lang.get("skill") + ": " + selectedSkill.getName());

                // Reset for next time
                for(TypingLabel skill : skillsL) {
                    skill.setText("");
                    skill.setColor(Color.WHITE);
                }

                // Prepare menus
                index = 4;

                return MenuType.TARGETING;
            }
        }

        return menuType;
    }

    public static void updateStatDisplay() {
        // Bloom displays
        for(int i = 0; i < 2; i++) {
            setTextIfChanged(bloomL.get(i), "{SPEED=0.1}{FADE}{SLIDE}" + lang.get("bloom") + ": " + battle.getTeam(i).getBloom() + "/100");

            if (i == 0) {
                bloomL.get(i).setX(bloomL.get(i).getWidth() / 3, Align.left);
            } else bloomL.get(i).setX(World.WIDTH - bloomL.get(i).getWidth() / 3, Align.right);
        }

        // Queue
        // Sort combatants by Agi
        List<Combatant> cmbsAll = battle.getAllCombatants();

        cmbsAll.sort((a, b) -> Integer.compare(
            b.getAgiWithStage(),
            a.getAgiWithStage()
        ));

        StringBuilder queueText = new StringBuilder().append(lang.get("queue") + ": ");

        for(int i = 0; i < cmbsAll.size(); i++) {
            boolean active = (i + 1) == usingMove || cmbsAll.get(i).getPos() == selectingMove;
            if (active) queueText.append("[PINK][[");
            queueText.append(cmbsAll.get(i).getCmbType().getName());
            if (active) {
                queueText.append("][WHITE]");
            }
            if (i != cmbsAll.size() - 1) queueText.append(", ");
        }

        queue.setText(queueText.toString());
        queue.setPosition(World.WIDTH_2, World.HEIGHT - 120, Align.center); // Mustt be set here so it aligns properly
        queue.skipToTheEnd();

        // Update combatant stat display
        // todo lang and disambiguation + system to display all status updates
        for (int i = 0; i < 8; i++) {
            Combatant cmb = (i >= 4) ? battle.getOpponentTeam().getCmbs()[i - 4] : battle.getPlayerTeam().getCmbs()[i]; // todo can use battle.getAllCmbs
            if (cmb != null) {
                int barrier = cmb.getBarrier() + cmb.getDefend();
                String barrierStr = (barrier > 0) ? "[CYAN](" + barrier + ")[WHITE]" : "";
                StringBuilder text = new StringBuilder(cmb.getCmbType().getName() + "\n" + lang.get("hp") + ": " + cmb.getStatsCur().getHp() + barrierStr + "/" + cmb.getStatsDefault().getHp() + "\n" + lang.get("sp") + ": " + cmb.getSp() + "/100" +
                    //"\nStr: " + cmb.getStrWithStage() + "/" + cmb.getStatsDefault().getStr() + "\nMag:" + cmb.getMagWithStage() + "/" + cmb.getStatsDefault().getMag() +
                    //"\nAmr: " + cmb.getAmrWithStage() + "/" + cmb.getStatsDefault().getAmr() + "\nRes: " + cmb.getResWithStage() + "/" + cmb.getStatsDefault().getRes() +
                     "\n");

                // List stage changes
                for(StageType stageType : StageType.values()) {
                    int stage = cmb.getStage(stageType);
                    if (stage != 0) {
                        text.append(stageType.getName()).append((stage >= 1) ? "+" : "").append(stage).append("(").append(cmb.getStageTurns(stageType)).append(") ");
                    }
                }

                // Barrier
                barrier = cmb.getBarrier();
                if (barrier > 0) {
                    text.append(lang.get("barrier")).append(" x").append(barrier).append("(").append(cmb.getBarrierTurns()).append(") ");
                }

                // List buffs
                List<BuffInstance> buffInstances = cmb.getBuffInstances();
                if (!buffInstances.isEmpty()) {
                    for (BuffInstance buffInstance : buffInstances) {
                        if (buffInstance.getBuff() == Buff.DEFEND) {
                            text.append(buffInstance.getBuff().getName()).append("x").append(cmb.getDefend()).append("(").append(buffInstance.getTurns()).append(") ");
                        } else text.append(buffInstance.getBuff().getName()).append(" x").append(buffInstance.getStacks()).append("(").append(buffInstance.getTurns()).append(") ");
                    }
                }
                setTextIfChanged(statsL.get(i), text.toString());
            } else statsL.get(i).setText("");
        }

        // Debug
        //setTextIfChanged(battleLog, "index = " + index + "\nturn = " + battle.getTurn() + "\nmoves = " + moves + "\nselectingMove = " + selectingMove);
    }

    // todo
    public static boolean isMoveValid(SkillTargeting move) {
        return true;
    }
}
