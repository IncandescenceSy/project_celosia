package io.github.celosia.sys.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.github.tommyettinger.textra.TextraLabel;
import com.github.tommyettinger.textra.TypingLabel;
import io.github.celosia.sys.Debug;
import io.github.celosia.sys.World;
import io.github.celosia.sys.menu.*;
import io.github.celosia.sys.menu.Fonts.FontType;
import io.github.celosia.sys.menu.MenuLib.MenuType;
import io.github.celosia.sys.settings.Keybind;
import io.github.celosia.sys.settings.Settings;

import java.util.*;

import static io.github.celosia.Main.*;
import static io.github.celosia.sys.battle.AffLib.getAffMultSpCost;
import static io.github.celosia.sys.battle.BattleLib.getStartingIndex;
import static io.github.celosia.sys.battle.BuffEffectLib.notifyOnUseSkill;
import static io.github.celosia.sys.battle.PosLib.getSide;
import static io.github.celosia.sys.menu.MenuLib.setTextIfChanged;
import static io.github.celosia.sys.menu.TextLib.*;
import static io.github.celosia.sys.settings.Lang.lang;

public class BattleController {

    // Battle
    public static Battle battle;

    // Time to wait in seconds
    static float wait = 0f;

    // Actions being made this turn
    static List<Move> moves = new ArrayList<>();
    // Copy for queue
    static List<Move> moves2 = new ArrayList<>();

    static int selectingMove = 0; // Who's currently selecting their move. 0-3 = player; 4-8 = opponent; 100 = moves are executing
    static int extraActions = 0; // How many extra actions have been used for the current combatant
    static int usingMove = 0; // Who's currently using their move
    static int applyingEffect = 0; // Which SkillEffect of the current skill is currently being applied
    static Map<Integer, ResultType> prevResults; // Previous SkillEffect resultType
    static int nonFails = 0; // If this skill's effects have succeeded at all

    // Menu navigation
    static int indexSkill = 0;
    static int indexTarget = 0;

    static Skill selectedSkill; // Currently selected skill

    // Display
    // Turn display
    static TypingLabel turn = new TypingLabel("{SPEED=0.1}{FADE}{SLIDE}" + lang.get("turn") + " 1", FontType.KORURI.getSize60());

    // Bloom displays for both teams
    static List<TypingLabel> bloomL = new ArrayList<>();

    // Queue (move order) display
    static TypingLabel queue = new TypingLabel("", FontType.KORURI.getSize30());

    // Stat displays for all units
    static List<TypingLabel> statsL = new ArrayList<>();

    // Move selection displays
    static List<TypingLabel> movesL = new ArrayList<>();

    // Skill menu display
    static List<TypingLabel> skillsL = new ArrayList<>();

    // temp
    static Skill[] skills = new Skill[]{Skills.ATTACK_DOWN_GROUP, Skills.FIREBALL, Skills.GET_EXA, Skills.THUNDERBOLT, Skills.ICE_AGE, Skills.DEFEND};
    static Skill[] skills2 = new Skill[]{Skills.ATTACK_UP_GROUP, Skills.GET_EXA, Skills.FIREBALL, Skills.SHIELD, Skills.ICE_AGE, Skills.DEFEND};
    static Skill[] skills3 = new Skill[]{Skills.GET_EXA, Skills.SHIELD, Skills.HEAT_WAVE, Skills.PROTECT, Skills.ICE_AGE, Skills.DEFEND};

    // Battle log
    // todo press L2(?) to bring up full log, better positioning
    static TextraLabel battleLog = new TextraLabel("", FontType.KORURI.getSize20());
    static List<String> logText = new ArrayList<>();
    static int logScroll = 0;

    // battle mechanics todo: Passives, Follow-Ups, Accessories
    public static void create() {
        // Setup teams (temp)
        Stats johnyStats = new Stats(100, 100, 100, 100, 100, 100, 100);
        UnitType johny = new UnitType("Johny", johnyStats, 4, -4, 0, 0 ,0, 0, 0, Passives.DEBUFF_DURATION_UP, Passives.RESTORATION);
        Stats jerryStats = new Stats(100, 100, 100, 100, 100, 100, 115);
        UnitType jerry = new UnitType("Jerry", jerryStats, 5, -4, 0, 5 ,0, 0, 0, Passives.DEBUFF_DURATION_UP);
        UnitType james = new UnitType("James", jerryStats, -4, 5, 0, 0 ,0, 0, 0, Passives.DEBUFF_DURATION_UP, Passives.ETERNAL_WELLSPRING);
        UnitType jacob = new UnitType("Jacob", johnyStats, 0, 0, 5, -4 ,0, 0, 0, Passives.DEBUFF_DURATION_UP);
        UnitType julia = new UnitType("Julia", johnyStats, 0, 0, -4, 5 ,0, 0, 0, Passives.DEBUFF_DURATION_UP);
        UnitType jude = new UnitType("Jude", jerryStats, 0, 0, -3, -3 ,5, 0, 0, Passives.DEBUFF_DURATION_UP);
        UnitType josephine = new UnitType("Josephine", jerryStats, 0, 0, 0, 0 ,0, 5, -4, Passives.DEBUFF_DURATION_UP);
        UnitType julian = new UnitType("Julian", johnyStats, 0, 0, 0, 0 ,0, -4, 5, Passives.DEBUFF_DURATION_UP);

        Team player = new Team(new Unit[]{new Unit(johny, 19, skills, 0),
            new Unit(james, 19, skills2, 1),
            new Unit(julia, 19, skills3, 2),
            new Unit(josephine, 19, skills, 3)});

        Team opponent = new Team(new Unit[]{new Unit(jerry, 19, skills2, 4),
            new Unit(jacob, 19, skills2, 5),
            new Unit(jude, 19, skills2, 6),
            new Unit(julian, 19, skills2, 7)});

        battle = new Battle(player, opponent);

        // Turn display
        turn.setPosition(World.WIDTH_2, World.HEIGHT - 60, Align.center);
        stage1.addActor(turn);

        for(int i = 0; i < 2; i++) {
            // Bloom displays for both teams
            TypingLabel bloom = new TypingLabel(lang.get("bloom") + ": 100/1,000", FontType.KORURI.getSize40());
            bloomL.add(bloom);
            bloom.setY(World.HEIGHT - 90);
            stage1.addActor(bloom);
        }

        // Queue (move order) display
        stage1.addActor(queue);

        // Log
        stage2.addActor(battleLog);

        for(int i = 0; i < 8; i++) {
            int y = (i >= 4) ? World.HEIGHT - 300 - 300 * (i - 4) : World.HEIGHT - 300 - 300 * i;

            // Stat displays for all units
            TypingLabel stats = new TypingLabel("", FontType.KORURI.getSize30());
            statsL.add(stats);
            stats.setPosition((i >= 4) ? World.WIDTH - 350 : 50, y);
            stage1.addActor(stats);

            // Move selection displays
            TypingLabel moves = new TypingLabel("", FontType.KORURI.getSize30());
            movesL.add(moves);
            moves.setPosition((i >= 4) ? World.WIDTH - 550 : 400, y);
            stage1.addActor(moves);
        }

        // Skill menu display
        // todo support arbitrary size
        for(int i = 0; i < 6; i++) {
            skillsL.add(new TypingLabel("", FontType.KORURI.getSize30()));
            stage1.addActor(skillsL.get(i));
        }

        // Notify Passives onGive
        for(Unit unit : battle.getAllUnits()) {
            for (Passive passive : unit.getPassives()) {
                for (BuffEffect buffEffect : passive.getBuffEffects()) {
                    String[] effectMsgs = buffEffect.onGive(unit, 1);
                    for (String effectMsg : effectMsgs) if (!effectMsg.isEmpty()) appendToLog(effectMsg);
                }
            }
        }

        // Log
        appendToLog(c_turn + lang.get("turn") + " " + 1 + "[WHITE]");
        appendToLog(lang.get("log.gain_sp_bloom"));
    }

    public static void input() {
        // Debug
        if(Debug.enableDebugHotkeys) {
            if(Gdx.input.isKeyJustPressed(Input.Keys.Q)) Gdx.app.log("", String.join("\n", logText));
            if(Debug.enableDebugHotkeys && Gdx.input.isKeyJustPressed(Input.Keys.W)) createPopup("among", "ussss");
        }

        // Log
        int logScrollNew = MenuLib.checkLogScroll(logScroll, logText.size());
        if(logScroll != logScrollNew) {
            logScroll = logScrollNew;
            updateLog();
        }

        if (wait > 0f) {
            wait -= Gdx.graphics.getDeltaTime();
        } else if(menuList.getLast() == MenuType.LOG) { // Fullscreen log
            // Go back
            if (InputLib.checkInput(Keybind.BACK, Keybind.MENU)) {
                menuList.removeLast();
                updateLog();
                coolRects.get(CoolRects.COVER_LEFT.ordinal()).setDir(-1);
                paths.get(Paths.SCROLLBAR.ordinal()).setDir(-1);
                return;
            }

        } else if (InputLib.checkInput(Keybind.MENU)) {
            createFullLog();
        }
        else if (menuList.getLast() == MenuType.BATTLE) { // Selecting moves
            if (selectingMove <= 3) { // Player's turn
                if (selectingMove < battle.getPlayerTeam().getUnits().length) { // if there are more allies yet to act
                    // Skill selection display
                    // todo support arbitrary size
                    for (int i = 0; i < 6; i++) {
                        skillsL.get(i).setPosition(600, (World.HEIGHT - 400 - 250 * selectingMove) - ((i - 2) * 35));
                        setTextIfChanged(skillsL.get(i), battle.getPlayerTeam().getUnits()[selectingMove].getSkills()[i].getName()); // todo support ExtraActions
                    }

                    selectMove();
                }
            } else if (selectingMove <= 8) { // opponent's turn
                if(!Debug.selectOpponentMoves) {
                    if ((selectingMove - 4) < battle.getOpponentTeam().getUnits().length) { // if there are more opponents yet to act
                        //Skill selectedSkill = skills2[MathUtils.random(skills.length - 1)];
                        Skill selectedSkill = Skills.NOTHING;
                        Unit target = battle.getPlayerTeam().getUnits()[MathUtils.random(battle.getPlayerTeam().getUnits().length - 1)];
                        setTextIfChanged(movesL.get(selectingMove), selectedSkill.getName() + " -> " + target.getUnitType().getName()); // todo support ExtraActions
                        moves.add(new Move(selectedSkill, battle.getOpponentTeam().getUnits()[selectingMove - 4], target.getPos())); // todo AI
                        selectingMove++;
                    } else selectingMove = 100; // Jump to move execution
                }
                selectMove();
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

                    for (Unit unit : battle.getAllUnits()) {
                        // Increase SP
                        if(!unit.isInfiniteSp()) unit.setSp(Math.min((int) (unit.getSp() + (100 * (Math.max(unit.getMultSpGain(), 10) / 100d))), 1000));

                        // Apply turn end BuffEffects
                        for (Passive passive : unit.getPassives()) {
                            StringBuilder turnEnd1 = new StringBuilder();
                            turnEnd1.append(formatName(unit.getUnitType().getName(), unit.getPos())).append(" ").append(passive.getName()).append(": ");

                            for (BuffEffect buffEffect : passive.getBuffEffects()) {
                                StringBuilder turnEnd2 = new StringBuilder();
                                String[] effectMsgs = buffEffect.onTurnEnd(unit, 1);
                                for (String effectMsg : effectMsgs) if (!effectMsg.isEmpty()) turnEnd2.append(effectMsg);

                                // Only have turn end message if both have messages
                                if(!turnEnd2.isEmpty()) appendToLog(turnEnd1 + turnEnd2.toString());
                            }
                        }

                        for(BuffInstance buffInstance : unit.getBuffInstances()) {
                            StringBuilder turnEnd1 = new StringBuilder();
                            turnEnd1.append(formatName(unit.getUnitType().getName(), unit.getPos())).append(" ").append(buffInstance.getBuff().getName()).append(": ");

                            for(BuffEffect buffEffect : buffInstance.getBuff().getBuffEffects()) {
                                StringBuilder turnEnd2 = new StringBuilder();
                                String[] effectMsgs = buffEffect.onTurnEnd(unit, buffInstance.getStacks());
                                for(String effectMsg : effectMsgs) if(!effectMsg.isEmpty()) turnEnd2.append(effectMsg);

                                if(!turnEnd2.isEmpty()) appendToLog(turnEnd1 + turnEnd2.toString());
                            }
                        }

                        // Decrement stage1/shield/buff turns and remove expired stage1s/shields/buffs
                        List<String> decrement = unit.decrementTurns();
                        if(!decrement.isEmpty()) appendAllToLog(decrement);
                    }

                    // Log
                    appendToLog(c_turn + lang.get("turn") + " " + (battle.getTurn() + 1) + "[WHITE]");
                    appendToLog(lang.get("log.gain_sp_bloom"));

                    // Increase bloom
                    battle.getPlayerTeam().setBloom(Math.min(battle.getPlayerTeam().getBloom() + 100, 1000));
                    battle.getOpponentTeam().setBloom(Math.min(battle.getOpponentTeam().getBloom() + 100, 1000));

                    // todo check if battle is over

                    return;
                }

                // Sort moves by Prio and then by Agi
                moves.sort(Comparator
                    .comparingInt((Move entry) -> entry.getSkill().getPrio())
                    .thenComparingInt(entry -> entry.getSelf().getAgiWithStage()).reversed());

                // The next move plays out
                Move move = moves.getFirst();

                // Is in range
                if (move.isValid()) {
                    // Invalid newSp will cancel move
                    int newSp;

                    // Execute move
                    if(applyingEffect == 0) {

                        // Set newSp
                        Unit self = move.getSelf();
                        Skill skill = move.getSkill();

                        Element element = skill.getElement();
                        boolean isPlayerTeam = self.getPos() < 4;
                        Team team = (isPlayerTeam) ? battle.getPlayerTeam() : battle.getOpponentTeam();
                        int cost = (self.isInfiniteSp() && !skill.isBloom()) ? 0 : skill.getCost();
                        // Make sure cost doesn't go below 1 unless the skill has a base 0 SP cost
                        int costMod = (cost > 0) ? (int) Math.max(Math.ceil((cost * getAffMultSpCost(self.getAff(element)))), 1) : 0;
                        newSp = ((skill.isBloom()) ? team.getBloom() - costMod : (int) (self.getSp() - (costMod * (Math.max(self.getMultSpUse(), 10) / 100d))));

                        StringBuilder builder = new StringBuilder();
                        if(newSp >= 0) {
                            Unit target = battle.getUnitAtPos(move.getTargetPos());
                            builder.append(formatName(self.getUnitType().getName(), self.getPos(), false)).append(" ").append(lang.get("log.uses")).append(" ").append(skill.getName());
                            if(!skill.isRangeSelf()) builder.append(" ").append(lang.get("log.on")).append(" ").append(formatName(target.getUnitType().getName(), move.getTargetPos(), false));

                            if (skill.isBloom()) {
                                builder.append(" (").append((isPlayerTeam) ? lang.get("log.team_player") : lang.get("log.team_opponent")).append(" ").append(lang.get("bloom")).append(" ")
                                    .append(String.format("%,d", team.getBloom())).append(" -> ").append(String.format("%,d", newSp)).append(")");
                                team.setBloom(newSp);
                            } else if (newSp != self.getSp()) { // Use SP
                                builder.append(" (").append(lang.get("sp")).append(" ").append(String.format("%,d", self.getSp())).append(" -> ").append(String.format("%,d", newSp)).append(")");
                                self.setSp(newSp);
                            }

                            appendToLog(builder.toString());

                            // Apply on-skill use BuffEffects
                            notifyOnUseSkill(self, target, skill);

                            // Color move for currently acting combatant (temp)
                            for (int i = 0; i < 8; i++) {
                                //statsL.get(i).setX(((self.getPos()) == i) ? (i >= 4) ? World.WIDTH - 300 : 200 : (i >= 4) ? World.WIDTH - 250 : 150);
                                if (self.getPos() == i) {
                                    movesL.get(i).setColor(Color.PINK);
                                } else movesL.get(i).setColor(Color.WHITE);
                            }

                            prevResults = new HashMap<>();
                        } else {
                            String msg = formatName(self.getUnitType().getName(), self.getPos(), false) + " " + lang.get("log.tries_to_use") + " " + skill.getName();
                            if(!skill.isRangeSelf()) msg += " " + lang.get("log.on") + " " + formatName(battle.getUnitAtPos(move.getTargetPos()).getUnitType().getName(), move.getTargetPos(), false);
                            msg += ", " + lang.get("log.but_doesnt_have_enough") + " " + (skill.isBloom() ? lang.get("bloom") : lang.get("sp"));
                            appendToLog(msg);
                        }
                    } else newSp = 0; // SP shouldn't change

                    // Make sure newSp is valid
                    if(newSp >= 0) {
                        // Apply all SkillEffects over a period of time
                        List<SkillEffect> skillEffects = move.getSkill().getSkillEffects();

                        Unit targetMain = battle.getUnitAtPos(move.getTargetPos());

                        // Apply SkillEffects one at a time
                        if (applyingEffect < skillEffects.size()) {
                            // Looking for at least 1 non-fail to continue the skill after the first effect
                            if(nonFails > 0 || applyingEffect == 0) {
                                // Apply to all targets
                                for (int targetPos : move.getSkill().getRange().getTargetPositions(move.getSelf().getPos(), move.getTargetPos())) {
                                    if (targetPos != -1) { // Target's position is valid
                                        Unit targetCur = battle.getUnitAtPos(targetPos);
                                        if (applyingEffect == 0) { // First effect
                                            // Initialize prevResults
                                            prevResults.put(targetPos, ResultType.SUCCESS);

                                            // Apply onTargetedBySkill BuffEffects
                                            for (BuffInstance buffInstance : targetCur.getBuffInstances()) {
                                                for (BuffEffect buffEffect : buffInstance.getBuff().getBuffEffects()) {
                                                    String[] effectMsgs = buffEffect.onTargetedBySkill(targetCur, buffInstance.getStacks());
                                                    for (String effectMsg : effectMsgs)
                                                        if (!effectMsg.isEmpty())
                                                            appendToLog(effectMsg);
                                                }
                                            }
                                        }

                                        // Hasn't failed on this target yet
                                        if (prevResults.get(targetPos) != ResultType.FAIL) {
                                            // Not a fail
                                            nonFails++;

                                            // Apply effect and track result
                                            Result result = skillEffects.get(applyingEffect).apply(move.getSelf(), targetCur, targetCur == targetMain, prevResults.get(targetPos));
                                            prevResults.put(targetPos, result.getResultType());

                                            // Log message
                                            List<String> msgs = result.getMessages();
                                            for (String msg : msgs)
                                                if (msg != null && !msg.isEmpty()) {
                                                    appendToLog(msg); // Add to log
                                                }
                                        }
                                    }
                                }
                                // Wait a bit before next SkillEffect
                                if (!skillEffects.get(applyingEffect).isInstant()) wait += 0.25f * Settings.battleSpeed;
                                applyingEffect++;
                            } else endMove();
                        } else endMove();
                    } else endMove();

                    // todo delete killed units
                } else {
                    appendToLog(formatName(move.getSelf().getUnitType().getName(), move.getSelf().getPos(), false) + " " +
                        lang.get("log.tries_to_use") + " " + move.getSkill().getName() + " " + lang.get("log.on") + " " +
                        formatName(battle.getUnitAtPos(move.getTargetPos()).getUnitType().getName(), move.getTargetPos(), false) + ", " + lang.get("log.but_cant_reach"));
                    endMove();
                }
            }
        } else if (menuList.getLast() == MenuType.TARGETING) { // Picking a target

            if (InputLib.checkInput(Keybind.BACK)) {
                // Reset for next time
                for(TypingLabel stat : statsL) stat.setColor(Color.WHITE);
                movesL.get(selectingMove).setText("");

                menuList.removeLast();
                return;
            }

            // Handle menu navigation
            indexTarget = MenuLib.checkMovementTargeting(indexTarget, selectingMove, selectedSkill.getRange());

            // Handle option colors
            MenuLib.handleOptColor(statsL, indexTarget);

            // Add selection to move queue
            if (InputLib.checkInput(Keybind.CONFIRM)) {
                Unit self = battle.getPlayerTeam().getUnits()[selectingMove];
                Unit target = (indexTarget < 4) ? battle.getPlayerTeam().getUnits()[indexTarget] : battle.getOpponentTeam().getUnits()[indexTarget - 4];
                moves.add(new Move(selectedSkill, self, target.getPos()));
                setTextIfChanged(movesL.get(selectingMove), movesL.get(selectingMove).getOriginalText() + " -> " + target.getUnitType().getName()); // todo support ExtraActions

                // Reset for next time
                for(TypingLabel stat : statsL) stat.setColor(Color.WHITE);

                // Move on to next combatant to select a move for unless this one has extra actions
                if(extraActions < self.getExtraActions()) {
                    extraActions++;
                } else {
                    extraActions = 0;
                    selectingMove++;
                }

                indexSkill = 0;

                menuList.removeLast();
            }
        }
    }

    public static void updateStatDisplay() {
        // Bloom displays
        for(int i = 0; i < 2; i++) {
            setTextIfChanged(bloomL.get(i), "{SPEED=0.1}{FADE}{SLIDE}" + lang.get("bloom") + ": " + String.format("%,d", battle.getTeam(i).getBloom()) + "/" + String.format("%,d", 1000));
            if (i == 0) bloomL.get(i).setX(bloomL.get(i).getWidth() / 3, Align.left);
            else bloomL.get(i).setX(World.WIDTH - bloomL.get(i).getWidth() / 3, Align.right);
        }

        // Queue
        // Sort units by Agi
        List<Unit> unitsAll = battle.getAllUnits();
        unitsAll.sort((a, b) -> Integer.compare(
            b.getAgiWithStage(),
            a.getAgiWithStage()
        ));

        // Copy and sort moves
        // Only copy if it hasn't started emptying yet
        if(selectingMove == 8) moves2 = new ArrayList<>(moves);
        else if(selectingMove == 100) {
            moves2.sort(Comparator
                .comparingInt((Move entry) -> entry.getSkill().getPrio())
                .thenComparingInt(entry -> entry.getSelf().getAgiWithStage()).reversed());
                //todo will thenComparingInt(pos.reversed) work
        } else moves2 = new ArrayList<>();

        StringBuilder queueText = new StringBuilder().append(lang.get("queue")).append(": ");

        for(int i = 0; i < unitsAll.size(); i++) {
            int pos = unitsAll.get(i).getPos();
            boolean active = pos == selectingMove;
            if(!active && usingMove > 0 && moves2.size() >= unitsAll.size()) active = (moves2.get(usingMove - 1).getSelf() == unitsAll.get(i));
            queueText.append((getSide(pos) == Side.ALLY) ? c_ally : c_opp);
            if (active) queueText.append((getSide(pos) == Side.ALLY) ? c_ally_l : c_opp_l).append("[[");
            queueText.append(unitsAll.get(i).getUnitType().getName());
            if (active) queueText.append("][WHITE]");
            if (i != unitsAll.size() - 1) queueText.append(", ");
        }
        queue.setText(queueText.toString());
        queue.setPosition(World.WIDTH_2, World.HEIGHT - 120, Align.center); // Must be set here so it aligns properly
        queue.skipToTheEnd();

        // Update combatant stat display
        // todo disambiguation + system to display all status updates
        for (int i = 0; i < 8; i++) {
            Unit unit = (i >= 4) ? battle.getOpponentTeam().getUnits()[i - 4] : battle.getPlayerTeam().getUnits()[i]; // todo can use battle.getAllUnits
            if (unit != null) {
                int shield = unit.getShield() + unit.getDefend();
                String shieldStr = (shield > 0) ? "[CYAN]+" + String.format("%,d", shield) + "[WHITE]" : "";
                String spStr = (!unit.isInfiniteSp()) ? String.format("%,d", unit.getSp()) + "/" + String.format("%,d", 1000) : "∞";
                StringBuilder text = new StringBuilder(unit.getUnitType().getName() + "\n" + lang.get("hp") + ": " + String.format("%,d", unit.getStatsCur().getHp()) +
                    shieldStr + "/" + String.format("%,d", unit.getStatsDefault().getHp()) + "\n" + lang.get("sp") + ": " + spStr +
                    //"\nStr: " + unit.getStrWithStage() + "/" + unit.getStatsDefault().getStr() + "\nMag:" + unit.getMagWithStage() + "/" + unit.getStatsDefault().getMag() +
                    //"\nAmr: " + unit.getAmrWithStage() + "/" + unit.getStatsDefault().getAmr() + "\nRes: " + unit.getResWithStage() + "/" + unit.getStatsDefault().getRes() +
                    "\n");

                // List stage1 changes
                for(StageType stage1Type : StageType.values()) {
                    int stage1 = unit.getStage(stage1Type);
                    if (stage1 != 0) {
                        text.append(stage1Type.getName()).append((stage1 >= 1) ? "+" : "").append(stage1).append("(").append(unit.getStageTurns(stage1Type)).append(") ");
                    }
                }

                // Shield
                shield = unit.getShield();
                if (shield > 0) text.append(lang.get("shield")).append("x").append(String.format("%,d", shield)).append("(").append(unit.getShieldTurns()).append(") ");

                // List buffs
                List<BuffInstance> buffInstances = unit.getBuffInstances();
                if (!buffInstances.isEmpty()) {
                    for (BuffInstance buffInstance : buffInstances) {
                        if (buffInstance.getBuff() == Buffs.DEFEND) {
                            text.append(buffInstance.getBuff().getName()).append("x").append(String.format("%,d", unit.getDefend())).append("(").append(buffInstance.getTurns()).append(") ");
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

    public static void appendToLog(String entry) {
        // If logSize exceeds 2500, remove ~500 lines (so this doesn't have to get called again soon)
        // logSize will be less than the exact amount of lines (by ~20% or less if I had to guess), but it'll be close enough
        // todo test if this actually works + figure out a good size limit
        if(logText.size() > 2500) logText.subList(0, 500).clear();

        if(!entry.isEmpty()) {
            logText.add(entry);

            // Reset scroll to bottom
            logScroll = 0;

            updateLog();
        }
    }

    public static void appendAllToLog(List<String> entry) {
        if(logText.size() > 2500) logText.subList(0, 500).clear();

        logText.addAll(entry);

        // Reset scroll to bottom
        logScroll = 0;

        updateLog();
    }

    public static void updateLog() {
        battleLog.setText(formatLog());

        if(menuList.getLast() == MenuType.LOG) {
            battleLog.setPosition(20, World.HEIGHT_2, Align.topLeft);

            Path scrollbar = paths.get(Paths.SCROLLBAR.ordinal());

            // Portion of the log currently displayed
            float ratio = Math.min(48f / logText.size(), 1);

            int range = World.HEIGHT - 50;
            int height = (int) (range * ratio);

            // 1 = bottom; 0 = top
            float pos = 1 - (float) Math.min(logText.size() - 48, logScroll) / logText.size();
            //float pos = 1 - (float) Math.max(Math.min(logText.size() - 48, logScroll) / logText.size(), 0);

            // (World.WIDTH_4 + 115) - (height / 6), World.HEIGHT_2 + (height / 2), World.WIDTH_4 + 125  - (height / 6), World.HEIGHT_2 - (height / 2)
            //0, World.HEIGHT + 100, World.WIDTH_4 + 150, -100
            // todo: fix problems:
            //      bar only goes about halfway up
            //      bar's x position isnt static
            // Start (bottom)
            int sx = (int) (World.WIDTH_4 + 265 - ((range * pos) / 6f));
            int sy = (int) (World.HEIGHT - (height / 2f) - (range * pos));

            // End (top)
            int ex = (int) (World.WIDTH_4 + 265 + (height / 6f) - ((range * pos) / 6f));
            int ey = (int) (World.HEIGHT + (height / 2f) - (range * pos));

            Array<Vector2> points = new Array<>(false, 2);
            points.addAll(new Vector2(sx, sy), new Vector2(ex, ey));
            scrollbar.setPoints(points);

        } else battleLog.setPosition(World.WIDTH_2 - 200, World.HEIGHT - 270, Align.left);
    }

    public static String formatLog() {
        int lines = (menuList.getLast() == MenuType.LOG) ? 48 : 8;
        if (logText == null || logText.isEmpty() || logScroll < 0 || logScroll > logText.size()) return "";
        int start = Math.max(0, logText.size() - lines - logScroll);
        int end = Math.min(start + lines, logText.size());
        List<String> subList = logText.subList(start, end);
        return String.join("\n", subList);
    }

    public static void createFullLog() {
        menuList.add(MenuType.LOG);
        coolRects.get(CoolRects.COVER_LEFT.ordinal()).setDir(1);
        paths.get(Paths.SCROLLBAR.ordinal()).setDir(1);

        updateLog();

    }

    public static void selectMove() {
        // Go back
        if (InputLib.checkInput(Keybind.BACK) && selectingMove != 0) {
            // Reset for next time
            for (TypingLabel skill : skillsL) {
                skill.setText("");
                skill.setColor(Color.WHITE);
            }

            selectingMove--;
            indexSkill = 0;
            movesL.get(selectingMove).setText("");

            // Remove all selected moves
            for(int i = 0; i <= battle.getPlayerTeam().getUnits()[selectingMove].getExtraActions(); i++) moves.removeLast();

            //menuList.removeLast();
            return;
        }

        // Handle menu navigation
        indexSkill = MenuLib.checkMovement1D(indexSkill, skillsL.size());

        // Handle option colors
        MenuLib.handleOptColor(skillsL, indexSkill);

        // Move selected
        if (InputLib.checkInput(Keybind.CONFIRM)) {
            selectedSkill = battle.getPlayerTeam().getUnits()[selectingMove].getSkills()[indexSkill];
            setTextIfChanged(movesL.get(selectingMove), lang.get("skill") + ": " + selectedSkill.getName());

            // Reset for next time
            for (TypingLabel skill : skillsL) {
                skill.setText("");
                skill.setColor(Color.WHITE);
            }

            // Prepare menus
            indexTarget = getStartingIndex(selectedSkill);

            menuList.add(MenuType.TARGETING);
        }
    }

    public static void endMove() {
        usingMove++;
        applyingEffect = 0;
        moves.removeFirst();
        wait += 1 * Settings.battleSpeed;
    }
}
