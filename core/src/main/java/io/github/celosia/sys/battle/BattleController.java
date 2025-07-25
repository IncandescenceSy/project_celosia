package io.github.celosia.sys.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.github.tommyettinger.textra.TextraLabel;
import com.github.tommyettinger.textra.TypingLabel;
import io.github.celosia.sys.Debug;
import io.github.celosia.sys.InputHandler;
import io.github.celosia.sys.World;
import io.github.celosia.sys.menu.Fonts.FontType;
import io.github.celosia.sys.menu.InputLib;
import io.github.celosia.sys.menu.MenuLib;
import io.github.celosia.sys.menu.MenuLib.MenuType;
import io.github.celosia.sys.settings.Keybind;
import io.github.celosia.sys.settings.Settings;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    // Copy for queue
    static List<SkillTargeting> moves2 = new ArrayList<>();

    static int selectingMove = 0; // Who's currently selecting their move. 0-3 = player; 4-8 = opponent; 100 = moves are executing
    static int extraActions = 0; // How many extra actions have been used for the current combatant
    static int usingMove = 0; // Who's currently using their move
    static int applyingEffect = 0; // Which SkillEffect of the current skill is currently being applied
    static ResultType resultType; // SkillEffect resultType

    // Menu navigation
    static int index = 5;

    static Skill selectedSkill; // Currently selected skill

    // Display
    // Turn display
    static TypingLabel turn = new TypingLabel("{SPEED=0.1}{FADE}{SLIDE}" + lang.get("turn") + " 1", FontType.KORURI.getSize60());

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
    static Skill[] skills = new Skill[]{Skills.FIREBALL, Skills.HEAL, Skills.INFERNAL_PROVENANCE, Skills.DEMON_SCYTHE, Skills.ICE_AGE};
    static Skill[] skills2 = new Skill[]{Skills.ATTACK_UP, Skills.ICE_BEAM, Skills.BARRIER, Skills.PROTECT, Skills.ICE_AGE};
    static Skill[] skills3 = new Skill[]{Skills.THUNDERBOLT, Skills.ATTACK_UP, Skills.BARRIER, Skills.JET_STREAM, Skills.ICE_AGE};

    // Battle log
    // todo press L2(?) to bring up full log, better positioning
    static TextraLabel battleLog = new TextraLabel("", FontType.KORURI.getSize20());
    static String logText = "[CYAN]" + lang.get("turn") + " " + 1 + "[WHITE]\n" + lang.get("log.all_units_gain") + " " + 10 + " " + lang.get("sp") + "\n" + lang.get("log.both_teams_gain") + " " + 10 + " " + lang.get("bloom") + "\n";
    static int logScroll = 0;
    static int logSize;

    // battle mechanics todo: Targeting, Passives, Follow-Ups, Accessories
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

        Team player = new Team(new Combatant[]{new Combatant(johny, 19, johnyStats.getRealStats(19), skills, 0),
            new Combatant(james, 19, jerryStats.getRealStats(19), skills2, 1),
            new Combatant(julia, 19, johnyStats.getRealStats(19), skills3, 2),
            new Combatant(josephine, 19, jerryStats.getRealStats(19), skills, 3)});

        Team opponent = new Team(new Combatant[]{new Combatant(jerry, 19, jerryStats.getRealStats(19), skills2, 4),
            new Combatant(jacob, 19, johnyStats.getRealStats(19), skills2, 5),
            new Combatant(jude, 19, jerryStats.getRealStats(19), skills2, 6),
            new Combatant(julian, 19, johnyStats.getRealStats(19), skills2, 7)});

        battle = new Battle(player, opponent);

        // Turn display
        turn.setPosition(World.WIDTH_2, World.HEIGHT - 60, Align.center);
        stage.addActor(turn);

        for(int i = 0; i < 2; i++) {
            // Bloom displays for both teams
            TypingLabel bloom = new TypingLabel(lang.get("bloom") + ": 100/1,000", FontType.KORURI.getSize40());
            bloomL.add(bloom);
            bloom.setY(World.HEIGHT - 90);
            stage.addActor(bloom);
        }

        // Queue (move order) display
        stage.addActor(queue);

        battleLog.setPosition(World.WIDTH_2 - 200, World.HEIGHT - 270, Align.left);
        stage.addActor(battleLog);

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
        // todo support arbitrary size
        for(int i = 0; i < 5; i++) {
            skillsL.add(new TypingLabel("", FontType.KORURI.getSize30()));
            stage.addActor(skillsL.get(i));
        }
    }

    public static MenuType input(MenuType menuType) {
        // If logSize exceeds 6000, remove ~1000 lines (so this doesn't have to get called again soon)
        // logSize will be less than the exact amount of lines (by ~20% or less if I had to guess), but it'll be close enough
        // todo test if this actually works + figure out a good size limit
        if(logSize > 6000) {
            logText = logText.lines().skip(logSize - 5000).collect(Collectors.joining("\n"));
            logSize -= 1000;
        }

        // Debug
        if(Debug.enableDebugHotkeys && Gdx.input.isKeyJustPressed(Input.Keys.Q)) Gdx.app.log("", logText);

        logScroll = MenuLib.checkLogScroll(logScroll, (int) logText.lines().count());

        if (wait > 0f) {
            wait -= Gdx.graphics.getDeltaTime();
        } else if (menuType == MenuType.BATTLE) { // Selecting moves

            if (selectingMove <= 3) { // Player's turn
                // Todo instead of text, use a little picture taken from an atlas and abstract the check
                setTextIfChanged(movesL.get(selectingMove), ((InputHandler.isLastUsedController()) ? Keybind.CONFIRM.getButton().getName() : Input.Keys.toString(Keybind.CONFIRM.getKey())) + ": " + lang.get("skill.attack") +
                    "\n" + ((InputHandler.isLastUsedController()) ? Keybind.BACK.getButton().getName() : Input.Keys.toString(Keybind.BACK.getKey())) + ": " + lang.get("skill.defend") +
                    "\n" + ((InputHandler.isLastUsedController()) ? Keybind.MENU.getButton().getName() : Input.Keys.toString(Keybind.MENU.getKey())) + ": " + lang.get("skills"));
                if (selectingMove < battle.getPlayerTeam().getCmbs().length) { // if there are more allies yet to act
                    if (InputLib.checkInput(Keybind.CONFIRM)) {
                        selectedSkill = Skills.ATTACK;
                        setTextIfChanged(movesL.get(selectingMove), selectedSkill.getName());

                        // Prepare menus
                        index = 4;

                        return MenuType.TARGETING;
                    } else if (InputLib.checkInput(Keybind.BACK)) {
                        selectedSkill = Skills.DEFEND;
                        setTextIfChanged(movesL.get(selectingMove), selectedSkill.getName());

                        // Prepare menus
                        index = 4;

                        return MenuType.TARGETING;
                    } else if (InputLib.checkInput(Keybind.MENU)) {
                        setTextIfChanged(movesL.get(selectingMove), lang.get("skill"));

                        // Skill selection display
                        for(int i = 0; i < 5; i++) {
                            skillsL.get(i).setPosition(600, (World.HEIGHT - 400 - 200 * selectingMove) - ((i - 2) * 35));
                            setTextIfChanged(skillsL.get(i), battle.getPlayerTeam().getCmbs()[selectingMove].getSkills()[i].getName()); // todo support ExtraActions
                        }

                        // Prepare menus
                        index = 0;

                        return MenuType.SKILLS;
                    }
                } else selectingMove = 4; // Jump to opponent move selection
            } else if (selectingMove <= 8) { // opponent's turn
                if ((selectingMove - 4) < battle.getOpponentTeam().getCmbs().length) { // if there are more opponents yet to act
                    //Skill selectedSkill = skills2[MathUtils.random(skills.length - 1)];
                    Skill selectedSkill = Skills.NOTHING;
                    Combatant target = battle.getPlayerTeam().getCmbs()[MathUtils.random(battle.getPlayerTeam().getCmbs().length - 1)];
                    setTextIfChanged(movesL.get(selectingMove), selectedSkill.getName() + " -> " + target.getCmbType().getName()); // todo support ExtraActions
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

                    StringBuilder builder = new StringBuilder();
                    for (Combatant cmb : battle.getAllCombatants()) {
                        // Increase SP
                        cmb.setSp(Math.min(cmb.getSp() + 100, 1000));

                        StringBuilder turnEnd1 = null;
                        StringBuilder turnEnd2 = new StringBuilder();

                        // Apply buff turn end effects
                        for(BuffInstance buffInstance : cmb.getBuffInstances()) {
                            turnEnd1 = new StringBuilder();
                            turnEnd1.append(cmb.getCmbType().getName()).append("'s ").append(buffInstance.getBuff().getName()).append(": ");
                            for(BuffEffect buffEffect : buffInstance.getBuff().getBuffEffects()) {
                                // todo fix this doesnt print to the log until the start of the next turn (unless the buff wears off) for some reason even though the effect happens immediately
                                // eg, burn will decrease HP as it should, and the HP display updates, but the log message about the HP decreasing doesn't appear until you select your moves for the next turn even though the log should be updated every frame. why?
                                turnEnd2.append(buffEffect.onTurnEnd(cmb));
                                logSize++;
                            }
                        }

                        // Only have turn end message if both have messages
                        if(turnEnd1 != null && !Objects.equals(turnEnd2.toString(), "")) builder.append(turnEnd1).append(turnEnd2);

                        // Decrement stage/barrier/buff turns and remove expired stages/barriers/buffs
                        // Don't increase logSize because most of the time this will be blank
                        builder.append(cmb.decrementTurns());
                    }

                    // Log
                    logScroll = 0;
                    builder.append("[CYAN]").append(lang.get("turn")).append(" ").append(battle.getTurn() + 1).append("[WHITE]\n").append(lang.get("log.all_units_gain")).append(" ").append(10).append(" ").append(lang.get("sp")).append("\n").append(lang.get("log.both_teams_gain")).append(" ").append(10).append(" ").append(lang.get("bloom")).append("\n");
                    logSize += 3;

                    logText += builder;

                    // Increase bloom
                    battle.getPlayerTeam().setBloom(Math.min(battle.getPlayerTeam().getBloom() + 100, 1000));
                    battle.getOpponentTeam().setBloom(Math.min(battle.getOpponentTeam().getBloom() + 100, 1000));

                    // todo check if battle is over

                    return MenuType.BATTLE;
                }

                // Sort moves by Prio and then by Agi
                moves.sort(Comparator
                    .comparingInt((SkillTargeting entry) -> entry.getSkill().getPrio())
                    .thenComparingInt(entry -> entry.getSelf().getAgiWithStage()).reversed());

                // The next move plays out
                SkillTargeting move = moves.get(0);

                if (isMoveValid(move)) {
                    // Invalid newSp will cancel move
                    int newSp;

                    // Execute move
                    if(applyingEffect == 0) {
                        logScroll = 0;
                        logSize++;

                        // Set newSp
                        Element element = move.getSkill().getElement();
                        boolean isPlayerTeam = move.getSelf().isPlayerTeam();
                        Team team = (isPlayerTeam) ? battle.getPlayerTeam() : battle.getOpponentTeam();
                        newSp = ((move.getSkill().isBloom()) ? team.getBloom() : move.getSelf().getSp()) - (int) Math.ceil((move.getSkill().getCost() * ((element == Element.VIS) ? 1 : affSp[move.getSelf().getCmbType().getAffs()[element.ordinal() - 1] + 5])));

                        if(newSp >= 0) {
                            logText += move.getSelf().getCmbType().getName() + " " + lang.get("log.uses") + " " + move.getSkill().getName() + " " + lang.get("log.on") + " " + move.getTarget().getCmbType().getName();

                            if (move.getSkill().isBloom()) {
                                logText += " (" + ((isPlayerTeam) ? lang.get("log.team_player") : lang.get("log.team_opponent")) + " " + lang.get("bloom") + " " + String.format("%,d", team.getBloom()) + " -> " + String.format("%,d", newSp) + ")";
                                team.setBloom(newSp);
                            } else if (newSp != move.getSelf().getSp()) { // Use SP
                                logText += " (" + lang.get("sp") + " " + String.format("%,d", move.getSelf().getSp()) + " -> " + String.format("%,d", newSp) + ")";
                                move.getSelf().setSp(newSp);
                            }

                            logText += "\n";

                            // Apply on-skill use BuffEffects
                            for (BuffInstance buffInstance : move.getSelf().getBuffInstances()) {
                                for (BuffEffect buffEffect : buffInstance.getBuff().getBuffEffects()) {
                                    logText += buffEffect.onUseSkill(move.getSelf(), move.getTarget());
                                }
                            }

                            // Color move for currently acting combatant (temp)
                            for (int i = 0; i < 8; i++) {
                                //statsL.get(i).setX(((move.getSelf().getPos()) == i) ? (i >= 4) ? World.WIDTH - 300 : 200 : (i >= 4) ? World.WIDTH - 250 : 150);
                                if (move.getSelf().getPos() == i) {
                                    movesL.get(i).setColor(Color.PINK);
                                } else movesL.get(i).setColor(Color.WHITE);
                            }

                            resultType = ResultType.SUCCESS;
                        } else {
                            logText += move.getSelf().getCmbType().getName() + " " + lang.get("log.tries_to_use") + " " + move.getSkill().getName() + ", " + lang.get("log.but_doesnt_have_enough") + " " + (move.getSkill().isBloom() ? lang.get("bloom") : lang.get("sp")) + "\n";
                        }
                    } else newSp = 0; // SP shouldn't change

                    // Make sure newSp is valid
                    if(newSp >= 0) {
                        // Apply all SkillEffects over a period of time
                        SkillEffect[] skillEffects = move.getSkill().getSkillEffects();

                        if (applyingEffect < skillEffects.length && resultType != ResultType.FAIL) {
                            Result result = skillEffects[applyingEffect].apply(move.getSelf(), move.getTarget(), resultType);
                            resultType = result.getResultType();
                            String[] msgs = result.getMessages();
                            for(String msg : msgs) if(msg != null && !Objects.equals(msg, "")) {
                                logScroll = 0; // Reset scroll
                                logText += msg; // Add to log
                                logSize++;
                            }
                            applyingEffect++;
                            wait += 0.25f * Settings.battleSpeed; // todo add ability for SkillEffects to not take time
                        } else {
                            endMove();
                        }
                    } else endMove();

                    // todo delete killed combatants
                } else endMove();
            }
        } else if (menuType == MenuType.TARGETING) { // Picking a target

            if (InputLib.checkInput(Keybind.BACK)) {
                // Reset for next time
                for(TypingLabel stat : statsL) stat.setColor(Color.WHITE);

                return MenuType.BATTLE;
            }

            // Handle menu navigation
            index = MenuLib.checkMovementTargeting(index);

            // Handle option colors
            MenuLib.handleOptColor(statsL, index);

            // Add selection to move queue
            if (InputLib.checkInput(Keybind.CONFIRM)) {
                Combatant self = battle.getPlayerTeam().getCmbs()[selectingMove];
                Combatant target = (index < 4) ? battle.getPlayerTeam().getCmbs()[index] : battle.getOpponentTeam().getCmbs()[index - 4];
                moves.add(new SkillTargeting(selectedSkill, self, target));
                setTextIfChanged(movesL.get(selectingMove), movesL.get(selectingMove).getOriginalText() + " -> " + target.getCmbType().getName()); // todo support ExtraActions

                // Reset for next time
                for(TypingLabel stat : statsL) stat.setColor(Color.WHITE);

                // Move on to next combatant to select a move for unless this one has extra actions
                BuffInstance extraAction = self.findBuff(Buffs.EXTRA_ACTION);
                if(extraAction != null && extraActions < extraAction.getStacks()) {
                    extraActions++;
                } else {
                    extraActions = 0;
                    selectingMove++;
                }

                return MenuType.BATTLE;
            } else return MenuType.TARGETING;
        } else if (menuType == MenuType.SKILLS) { // Picking a skill

            if (InputLib.checkInput(Keybind.BACK)) {
                // Reset for next time
                for(TypingLabel skill : skillsL) {
                    skill.setText("");
                    skill.setColor(Color.WHITE);
                }

                return MenuType.BATTLE;
            }

            // Handle menu navigation
            index = MenuLib.checkMovement1D(index, skillsL.size());

            // Handle option colors
            MenuLib.handleOptColor(skillsL, index);

            // Move selected
            if (InputLib.checkInput(Keybind.CONFIRM)) {
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
            setTextIfChanged(bloomL.get(i), "{SPEED=0.1}{FADE}{SLIDE}" + lang.get("bloom") + ": " + String.format("%,d", battle.getTeam(i).getBloom()) + "/" + String.format("%,d", 1000));
            if (i == 0) bloomL.get(i).setX(bloomL.get(i).getWidth() / 3, Align.left);
            else bloomL.get(i).setX(World.WIDTH - bloomL.get(i).getWidth() / 3, Align.right);
        }

        // Queue
        // todo fix it taking a fraction of a second to add the highlight during the start of move execution
        // Sort combatants by Agi
        List<Combatant> cmbsAll = battle.getAllCombatants();
        cmbsAll.sort((a, b) -> Integer.compare(
            b.getAgiWithStage(),
            a.getAgiWithStage()
        ));

        // Copy and sort moves
        // Only copy if it hasn't started emptying yet
        if(selectingMove == 8) moves2 = new ArrayList<>(moves);
        else if(selectingMove == 100) {
            moves2.sort(Comparator
                .comparingInt((SkillTargeting entry) -> entry.getSkill().getPrio())
                .thenComparingInt(entry -> entry.getSelf().getAgiWithStage()).reversed());
        } else moves2 = new ArrayList<>();

        StringBuilder queueText = new StringBuilder().append(lang.get("queue")).append(": ");

        for(int i = 0; i < cmbsAll.size(); i++) {
            boolean active = cmbsAll.get(i).getPos() == selectingMove;
            if(!active && usingMove > 0 && moves2.size() >= cmbsAll.size()) active = (moves2.get(usingMove - 1).getSelf() == cmbsAll.get(i));
            if (active) queueText.append("[PINK][[");
            queueText.append(cmbsAll.get(i).getCmbType().getName());
            if (active) queueText.append("][WHITE]");
            if (i != cmbsAll.size() - 1) queueText.append(", ");
        }

        queue.setText(queueText.toString());
        queue.setPosition(World.WIDTH_2, World.HEIGHT - 120, Align.center); // Must be set here so it aligns properly
        queue.skipToTheEnd();

        // Log
        // todo clean up
        List<String> lines = logText.lines().toList(); // todo will this lag
        int size = Math.toIntExact(lines.size());
        if(size >= 8) {
            if (size - logScroll >= 8)
                battleLog.setText(lines.get(size - logScroll - 8) + "\n" + lines.get(size - logScroll - 7) + "\n" + lines.get(size - logScroll - 6) + "\n" + lines.get(size - logScroll - 5) + "\n" + lines.get(size - logScroll - 4) + "\n" + lines.get(size - logScroll - 3) + "\n" + lines.get(size - logScroll - 2) + "\n" + lines.get(size - logScroll - 1));
            else
                battleLog.setText(lines.get(0) + "\n" + lines.get(1) + "\n" + lines.get(2) + "\n" + lines.get(3) + "\n" + lines.get(4) + "\n" + lines.get(5) + "\n" + lines.get(6) + "\n" + lines.get(7));
        }
        else battleLog.setText(logText);

        // Update combatant stat display
        // todo disambiguation + system to display all status updates
        for (int i = 0; i < 8; i++) {
            Combatant cmb = (i >= 4) ? battle.getOpponentTeam().getCmbs()[i - 4] : battle.getPlayerTeam().getCmbs()[i]; // todo can use battle.getAllCmbs
            if (cmb != null) {
                int barrier = cmb.getBarrier() + cmb.getDefend();
                String barrierStr = (barrier > 0) ? "[CYAN](" + String.format("%,d", barrier) + ")[WHITE]" : "";
                StringBuilder text = new StringBuilder(cmb.getCmbType().getName() + "\n" + lang.get("hp") + ": " + String.format("%,d", cmb.getStatsCur().getHp()) + barrierStr + "/" + String.format("%,d", cmb.getStatsDefault().getHp()) + "\n" + lang.get("sp") + ": " + String.format("%,d", cmb.getSp()) + "/" + String.format("%,d", 1000) +
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
                if (barrier > 0) text.append(lang.get("barrier")).append("x").append(String.format("%,d", barrier)).append("(").append(cmb.getBarrierTurns()).append(") ");

                // List buffs
                List<BuffInstance> buffInstances = cmb.getBuffInstances();
                if (!buffInstances.isEmpty()) {
                    for (BuffInstance buffInstance : buffInstances) {
                        if (buffInstance.getBuff() == Buffs.DEFEND) {
                            text.append(buffInstance.getBuff().getName()).append("x").append(String.format("%,d", cmb.getDefend())).append("(").append(buffInstance.getTurns()).append(") ");
                        } else {
                            text.append(buffInstance.getBuff().getName());
                            if(buffInstance.getBuff().getMaxStacks() > 1) text.append("x").append(buffInstance.getStacks());
                            if(buffInstance.getTurns() < 1000) text.append("(").append(buffInstance.getTurns()).append(") "); // 1000+ turns = infinite; todo place infinity symbol in place of turn count
                        }
                    }
                }
                setTextIfChanged(statsL.get(i), text.toString());
            } else statsL.get(i).setText("");
        }
    }

    // todo
    public static boolean isMoveValid(SkillTargeting move) {
        return true;
    }

    public static void endMove() {
        usingMove++;
        applyingEffect = 0;
        moves.remove(0);
        wait += 1 * Settings.battleSpeed;
    }
}
