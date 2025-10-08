package io.github.celosia.sys.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.github.tommyettinger.textra.TextraLabel;
import io.github.celosia.sys.Debug;
import io.github.celosia.sys.World;
import io.github.celosia.sys.input.InputLib;
import io.github.celosia.sys.input.InputPrompt;
import io.github.celosia.sys.input.InputPrompts;
import io.github.celosia.sys.input.Keybind;
import io.github.celosia.sys.menu.MenuLib;
import io.github.celosia.sys.render.CoolRects;
import io.github.celosia.sys.render.Fonts;
import io.github.celosia.sys.render.Path;
import io.github.celosia.sys.save.Settings;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.celosia.Main.NAV_PATH;
import static io.github.celosia.Main.STAGE_TYPES;
import static io.github.celosia.Main.coolRects;
import static io.github.celosia.Main.paths;
import static io.github.celosia.Main.stage2;
import static io.github.celosia.Main.stage3;
import static io.github.celosia.Main.stage4;
import static io.github.celosia.sys.battle.BattleController.delayS;
import static io.github.celosia.sys.battle.PosLib.getSide;
import static io.github.celosia.sys.battle.PosLib.getStartingIndex;
import static io.github.celosia.sys.menu.MenuLib.checkMovement1D;
import static io.github.celosia.sys.render.TriLib.createPopup;
import static io.github.celosia.sys.save.Lang.lang;
import static io.github.celosia.sys.util.BoolLib.booleanToInt;
import static io.github.celosia.sys.util.BoolLib.booleanToSign;
import static io.github.celosia.sys.util.TextLib.C_ALLY;
import static io.github.celosia.sys.util.TextLib.C_ALLY_L;
import static io.github.celosia.sys.util.TextLib.C_BLOOM;
import static io.github.celosia.sys.util.TextLib.C_BUFF;
import static io.github.celosia.sys.util.TextLib.C_CD;
import static io.github.celosia.sys.util.TextLib.C_NUM;
import static io.github.celosia.sys.util.TextLib.C_OPP;
import static io.github.celosia.sys.util.TextLib.C_OPP_L;
import static io.github.celosia.sys.util.TextLib.C_PASSIVE;
import static io.github.celosia.sys.util.TextLib.C_SKILL;
import static io.github.celosia.sys.util.TextLib.C_STAT;
import static io.github.celosia.sys.util.TextLib.C_TURN;
import static io.github.celosia.sys.util.TextLib.formatName;
import static io.github.celosia.sys.util.TextLib.formatNum;
import static io.github.celosia.sys.util.TextLib.getNamesAsMultiline;
import static io.github.celosia.sys.util.TextLib.getPercentageColor;
import static io.github.celosia.sys.util.TextLib.getStatColor;
import static io.github.celosia.sys.util.TextLib.getTriesToUseString;

public class BattleControllerLib {

    public static Battle battle;

    /// Display
    static TextraLabel turn = new TextraLabel("{SPEED=0.1}{FADE}{SLIDE}" + C_TURN + lang.get("turn") + " 1",
            Fonts.FontType.KORURI.get(80));
    static TextraLabel[] bloomL = new TextraLabel[2];
    static TextraLabel queue = new TextraLabel("", Fonts.FontType.KORURI.get(30));
    static TextraLabel[] statsL = new TextraLabel[8];
    static TextraLabel[] buffsL = new TextraLabel[8];
    static TextraLabel[] movesL = new TextraLabel[8];
    static List<TextraLabel> skillsL = new ArrayList<>();

    // Inspect menu
    static TextraLabel unitList = new TextraLabel("", Fonts.FontType.KORURI.get(30));
    static TextraLabel pageList = new TextraLabel(
            lang.get("skills") + "   " + lang.get("passives") + "   " + lang.get("buffs") + "   " + lang.get("stats"),
            Fonts.FontType.KORURI.get(30));
    static Array<Vector2> pointsPageDivL = new Array<>(false, 2);
    static Array<Vector2> pointsPageDivR = new Array<>(false, 2);
    static TextraLabel pageItemList = new TextraLabel("", Fonts.FontType.KORURI.get(20));
    static TextraLabel pageItemRightList = new TextraLabel("", Fonts.FontType.KORURI.get(20));
    static TextraLabel descHeader = new TextraLabel("", Fonts.FontType.KORURI.get(30));
    static TextraLabel desc = new TextraLabel("", Fonts.FontType.KORURI.get(20));

    // Stat, Equip, Affinity, Mult, Mod, Other
    static int[] infoX = new int[] { 960 + 330, 700, 300, 300, 750, 1125 };
    static int[] infoY = new int[] { World.HEIGHT - (110 + 60), World.HEIGHT - 245, World.HEIGHT - 245,
            World.HEIGHT - 385,
            World.HEIGHT - 385, World.HEIGHT - 385 };
    static InputPrompt[] infoPrompts = new InputPrompt[] { InputPrompts.I_STAT, InputPrompts.I_AFFINITY,
            InputPrompts.I_EQUIP, InputPrompts.I_MULT, InputPrompts.I_MOD, InputPrompts.I_OTHER };
    static TextraLabel[] infoL = new TextraLabel[6];

    static TextraLabel equip = new TextraLabel("", Fonts.FontType.KORURI.get(20));
    static TextraLabel affinities = new TextraLabel("", Fonts.FontType.KORURI.get(20));

    static String[] statNames = new String[] { lang.get("stat.str"), lang.get("stat.mag"), lang.get("stat.fth"),
            lang.get("stat.amr"), lang.get("stat.res"), lang.get("stat.agi") };
    static TextraLabel[] statsBasicL = new TextraLabel[6];
    static TextraLabel[] statsBasicNumL = new TextraLabel[6];

    static TextraLabel hp = new TextraLabel(lang.get("hp"), Fonts.FontType.KORURI.get(20));
    static TextraLabel hpAmt = new TextraLabel("", Fonts.FontType.KORURI.get(20));
    static TextraLabel sp = new TextraLabel(lang.get("sp"), Fonts.FontType.KORURI.get(20));
    static TextraLabel spAmt = new TextraLabel("", Fonts.FontType.KORURI.get(20));

    static String[] statCategoryHeaderNames = new String[] { lang.get("info.mult"), lang.get("info.mod"),
            lang.get("info.other") };
    static TextraLabel[] statCategoryHeaderL = new TextraLabel[3];

    static String multNames = getNamesAsMultiline(Mult.values(), Mult::getName);
    static String modNames = getNamesAsMultiline(Mod.values(), Mod::getName);
    // todo dont show immunities
    static String otherNames = getNamesAsMultiline(BooleanStat.values(), BooleanStat::getName) + "\n" +
            lang.get("extra_actions");
    static String[] statCategoryNames = new String[] { multNames, modNames, otherNames };
    static Array<Vector2> pointsMult = new Array<>(false, 2);
    static Array<Vector2> pointsMod = new Array<>(false, 2);
    static Array<Vector2> pointsOther = new Array<>(false, 2);
    static TextraLabel[] statsPageL = new TextraLabel[3];
    static TextraLabel[] statsPageNumL = new TextraLabel[3];

    // temp
    static Skill[] skills = new Skill[] { Skills.FIREBALL, Skills.SHIELD, Skills.RASETU_FEAST,
            Skills.AGILITY_UP_GROUP, Skills.ICE_AGE, Skills.DEFEND };
    static Skill[] skills2 = new Skill[] { Skills.ICE_BEAM, Skills.DEMON_SCYTHE, Skills.ATTACK_UP_GROUP,
            Skills.RASETU_FEAST, Skills.ICE_AGE, Skills.DEFEND };
    static Skill[] skills3 = new Skill[] { Skills.THUNDERBOLT, Skills.DEFENSE_DOWN, Skills.HEAT_WAVE, Skills.PROTECT,
            Skills.ICE_AGE, Skills.DEFEND };
    static SkillInstance nothingInstanceTemp = new SkillInstance(Skills.NOTHING);

    // todo fix positioning
    static TextraLabel battleLog = new TextraLabel("", Fonts.FontType.KORURI.get(20));
    static List<String> logText = new ArrayList<>();

    // Amount of lines scrolled upwards
    static int logScroll = 0;

    static List<Move> moves = new ArrayList<>();
    // Copy for queue reasons ...? todo
    static List<Move> moves2 = new ArrayList<>();

    // How many extra actions have been used for the currently acting Unit
    static int extraActions = 0;

    // Pos of the Unit that's currently selecting their move. 100 = moves are executing
    static int selectingMove = 0;

    // Pos of the Unit that's currently using their Move
    static int usingMove = 0;

    // Index of the currently-applying SkillEffect of the current Move
    static int applyingEffect = 0;

    // Previous SkillEffect resultTypes for each pos
    static ResultType[] prevResults = new ResultType[8];

    // Amount of non-fail results for the current Move so far
    static int nonFails = 0;

    // Menu navigation
    static int indexSkill = 0;
    static int indexTarget = 0;
    static int indexPage = 0;
    static int indexPageList = 0;

    // Inspect pages
    static final int SKILLS = 0;
    static final int PASSIVES = 1;
    static final int BUFFS = 2;
    static final int STATS = 3;

    // Currently selected skill
    static SkillInstance selectedSkillInstance;

    static void handleSetup() {
        // Setup teams (temp)
        Map<Element, Integer> affinitiesTemp = new HashMap<>();
        Stats johnyStats = new Stats(100, 100, 100, 100, 100, 100, 100);
        UnitType johny = new UnitType("Johny", "", johnyStats, new HashMap<>(affinitiesTemp),
                Passives.DEBUFF_DURATION_UP, Passives.RESTORATION, Passives.PERCENTAGE_DMG_TAKEN_DOWN_50);
        Stats jerryStats = new Stats(100, 100, 100, 100, 100, 100, 115);
        UnitType jerry = new UnitType("Jerry", "", jerryStats, new HashMap<>(affinitiesTemp),
                Passives.DEBUFF_DURATION_UP);
        UnitType james = new UnitType("James", "", jerryStats, new HashMap<>(affinitiesTemp),
                Passives.DEBUFF_DURATION_UP, Passives.ETERNAL_WELLSPRING);
        UnitType jacob = new UnitType("Jacob", "", johnyStats, new HashMap<>(affinitiesTemp),
                Passives.DEBUFF_DURATION_UP);
        UnitType julia = new UnitType("Julia", "", johnyStats, new HashMap<>(affinitiesTemp),
                Passives.DEBUFF_DURATION_UP);
        UnitType jude = new UnitType("Jude", "", jerryStats, new HashMap<>(affinitiesTemp),
                Passives.DEBUFF_DURATION_UP, Passives.PERCENTAGE_DMG_TAKEN_DOWN_999);
        UnitType josephine = new UnitType("Josephine", "", jerryStats, new HashMap<>(affinitiesTemp),
                Passives.DEBUFF_DURATION_UP);
        UnitType julian = new UnitType("Julian", "", johnyStats, new HashMap<>(affinitiesTemp),
                Passives.DEBUFF_DURATION_UP);

        Team player = new Team(new Unit[] { new Unit(johny, 19, skills, Accessories.FIREBORN_RING, 0),
                new Unit(james, 19, skills2, Accessories.FIREBORN_RING, 1),
                new Unit(julia, 19, skills3, Accessories.FIREBORN_RING, 2),
                new Unit(josephine, 19, skills, Accessories.FIREBORN_RING, 3) });

        Team opponent = new Team(new Unit[] { new Unit(jerry, 19, skills2, Accessories.FIREBORN_RING, 4),
                new Unit(jacob, 19, skills2, Accessories.FIREBORN_RING, 5),
                new Unit(jude, (long) 1E15, skills2, Accessories.FIREBORN_RING, 6),
                new Unit(julian, 19, skills2, Accessories.FIREBORN_RING, 7) });

        battle = new Battle(player, opponent);

        // Do NOT use the overload of setPosition() that also takes in Align. I don't know why, but it doesn't work
        turn.setAlignment(Align.center);
        turn.setPosition(World.WIDTH_2, World.HEIGHT - 60);
        stage2.addActor(turn);

        for (int i = 0; i < 2; i++) {
            TextraLabel bloom = new TextraLabel(
                    C_STAT + lang.get("bloom") + "[WHITE]: " + C_BLOOM + "100[WHITE]/" + C_BLOOM + "1,000",
                    Fonts.FontType.KORURI.get(30));
            bloomL[i] = bloom;
            bloom.setY(World.HEIGHT - 90);

            if (i == 1) {
                bloom.setAlignment(Align.right);
                bloom.setX(World.WIDTH - 70);
            } else {
                bloom.setX(70);
            }

            stage2.addActor(bloom);
        }

        queue.setAlignment(Align.center);
        queue.setPosition(World.WIDTH_2, World.HEIGHT - 120);
        stage2.addActor(queue);

        battleLog.setPosition(World.WIDTH_2 - 200, World.HEIGHT - 270);
        stage3.addActor(battleLog);

        // Iterate for each Unit
        for (int i = 0; i < 8; i++) {
            int y = (i >= 4) ? World.HEIGHT - 300 - 300 * (i - 4) : World.HEIGHT - 300 - 300 * i;

            TextraLabel stats = new TextraLabel("", Fonts.FontType.KORURI.get(30));
            statsL[i] = stats;
            stats.setPosition((i >= 4) ? World.WIDTH - 350 : 50, y);
            stage2.addActor(stats);

            TextraLabel buffs = new TextraLabel("", Fonts.FontType.KORURI.get(20));
            buffsL[i] = buffs;
            buffs.setAlignment(Align.topLeft);
            buffs.setPosition((i >= 4) ? World.WIDTH - 350 : 50, y - 70);
            stage2.addActor(buffs);

            TextraLabel moves = new TextraLabel("", Fonts.FontType.KORURI.get(30));
            movesL[i] = moves;
            moves.setPosition((i >= 4) ? World.WIDTH - 550 : 400, y);
            stage2.addActor(moves);
        }

        equip.setPosition(300, World.HEIGHT - 215);
        affinities.setPosition(700, World.HEIGHT - 215);

        hp.setPosition(300, World.HEIGHT - 110);
        hpAmt.setAlignment(Align.right);
        hpAmt.setPosition(600, World.HEIGHT - 110);

        sp.setPosition(300, World.HEIGHT - 140);
        spAmt.setAlignment(Align.right);
        spAmt.setPosition(600, World.HEIGHT - (110 + 30));

        // Inspect info and basic stats
        for (int i = 0; i < 6; i++) {
            TextraLabel info = new TextraLabel("", Fonts.FontType.KORURI.get(20));
            info.setPosition(infoX[i], infoY[i]);
            infoL[i] = info;

            TextraLabel stat = new TextraLabel(statNames[i], Fonts.FontType.KORURI.get(20));
            stat.setPosition((i > 2) ? 960 : 630, World.HEIGHT - (110 + (30 * (i % 3))));
            stat.setAlignment(Align.left);
            statsBasicL[i] = stat;

            TextraLabel statNum = new TextraLabel("", Fonts.FontType.KORURI.get(20));
            statNum.setPosition((i > 2) ? 1260 : 930, World.HEIGHT - (110 + (30 * (i % 3))));
            statNum.setAlignment(Align.right);
            statsBasicNumL[i] = statNum;
        }

        // Inspect stats page
        for (int i = 0; i < 3; i++) {
            TextraLabel statHeader = new TextraLabel(statCategoryHeaderNames[i], Fonts.FontType.KORURI.get(30));
            statHeader.setPosition(50 + (450 * i), World.HEIGHT - 380);
            statCategoryHeaderL[i] = statHeader;

            TextraLabel stat = new TextraLabel(statCategoryNames[i], Fonts.FontType.KORURI.get(20));
            stat.setAlignment(Align.topLeft);
            stat.setPosition(50 + (i * 450), World.HEIGHT - 415);
            statsPageL[i] = stat;

            TextraLabel statNum = new TextraLabel("", Fonts.FontType.KORURI.get(20));
            statNum.setAlignment(Align.topRight);
            statNum.setPosition(250 + (i * 450), World.HEIGHT - 415);
            statsPageNumL[i] = statNum;
        }

        int y = World.HEIGHT - 400;
        pointsMult.addAll(new Vector2(40, y), new Vector2(440, y));
        pointsMod.addAll(new Vector2(40 + 450, y), new Vector2(440 + 450, y));
        pointsOther.addAll(new Vector2(40 + 900, y), new Vector2(440 + 900, y));

        unitList.setPosition(320, World.HEIGHT - 50);

        pageList.setAlignment(Align.center);
        pageList.setPosition(640, World.HEIGHT - 320);

        y = World.HEIGHT - 315;
        pointsPageDivL.addAll(new Vector2(30, y), new Vector2(400, y));
        pointsPageDivR.addAll(new Vector2(870, y), new Vector2(1450, y));

        pageItemList.setAlignment(Align.topLeft);
        pageItemList.setPosition(50, World.HEIGHT - 370);

        pageItemRightList.setAlignment(Align.topRight);
        pageItemRightList.setPosition(475, World.HEIGHT - 370);

        descHeader.setPosition(550, World.HEIGHT - 390);

        desc.setAlignment(Align.topLeft);
        desc.setPosition(550, World.HEIGHT - 420);

        // todo support arbitrary size
        for (int i = 0; i < 6; i++) {
            skillsL.add(new TextraLabel("", Fonts.FontType.KORURI.get(30)));
            stage2.addActor(skillsL.get(i));
        }

        for (Unit unit : battle.getAllUnits()) {
            for (Passive passive : unit.getPassives()) {
                for (BuffEffect buffEffect : passive.getBuffEffects()) {
                    buffEffect.onGive(unit, 1);
                }
            }
        }

        appendToLog(C_TURN + lang.get("turn") + " " + 1 + "[WHITE]");
        appendToLog(lang.get("log.gain_sp_bloom"));
    }

    static void handleDebug() {
        // Print log
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            Gdx.app.log("Battle Log", String.join("\n", logText).replaceAll("\\[.*?]", ""));
        }

        // Test popup
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            createPopup("among", "ussss");
        }
    }

    static void handleBattle() {
        // Player's turn
        if (selectingMove <= 3) {
            selectPlayerMove();
        }
        // Opponent's turn
        else if (selectingMove <= 8) {
            selectOpponentMove();
        }
        // Moves play out
        else if (selectingMove == 100) {
            executeMove();
        }
    }

    static void selectPlayerMove() {
        if (selectingMove < battle.getPlayerTeam().getUnits().length) {
            // todo support arbitrary size
            for (int i = 0; i < 6; i++) {
                skillsL.get(i).setPosition(600, (World.HEIGHT - 400 - 250 * selectingMove) - ((i - 2) * 35));
                // todo support ExA
                skillsL.get(i).setText(
                        battle.getPlayerTeam().getUnits()[selectingMove].getSkillInstances().get(i).getSkill()
                                // temp
                                // todo real skill description display
                                .getNameWithIcon() + "(" + C_CD +
                                battle.getPlayerTeam().getUnits()[selectingMove].getSkillInstances().get(i)
                                        .getCooldown() +
                                "[WHITE])");
                skillsL.get(i).skipToTheEnd();
            }

            selectMove();
        }
    }

    static void selectOpponentMove() {
        if (!Debug.selectOpponentMoves) { // todo make this actually work
            if ((selectingMove - 4) < battle.getOpponentTeam().getUnits().length) {
                // Skill selectedSkill = skills2[MathUtils.random(skills.length - 1)];
                SkillInstance selectedSkillInstance = nothingInstanceTemp;
                Unit target = battle.getPlayerTeam().getUnits()[MathUtils
                        .random(battle.getPlayerTeam().getUnits().length - 1)];
                // todo support ExA
                movesL[selectingMove].setText(
                        selectedSkillInstance.getSkill().getName() + " → " + target.getUnitType().getName());
                moves.add(new Move(selectedSkillInstance, battle.getOpponentTeam().getUnits()[selectingMove - 4],
                        target.getPos())); // todo AI
                selectingMove++;
            } else {
                // Jump to move execution
                selectingMove = 100;
            }
        } else {
            selectMove();
        }
    }

    static void executeMove() {
        if (moves.isEmpty()) {
            endTurn();
            return;
        }

        // Sort moves by Prio and then by Agi
        moves.sort(Comparator.comparingInt((Move entry) -> entry.skillInstance().getSkill().getPrio())
                .thenComparingLong(entry -> entry.self().getAgiWithStage()).reversed());

        Move move = moves.getFirst();
        Unit self = move.self();

        if (self.isBooleanStat(BooleanStat.UNABLE_TO_ACT)) {
            appendToLog(lang.format("log.skill_fail.unable_to_act", getTriesToUseString(move),
                    lang.format("log.but_is_unable_to_act", self.getBooleanStat(BooleanStat.UNABLE_TO_ACT))));
            endMove();
            return;
        }

        int cd = move.skillInstance().getCooldown();
        if (cd > 0 && applyingEffect == 0) {
            appendToLog(lang.format("log.skill_fail.cooldown", getTriesToUseString(move),
                    lang.format("log.but_its_on_cooldown", cd)));
            endMove();
            return;
        }

        if (!move.isInRange()) {
            appendToLog(lang.format("log.skill_fail.range", getTriesToUseString(move), lang.get("log.but_cant_reach")));
            endMove();
            return;
        }

        // SP after move executes. Invalid spNew will cancel move
        int spNew = 0;

        if (applyingEffect == 0) {
            move.skillInstance().setCooldown(cd);
            Skill skill = move.skillInstance().getSkill();

            Element element = skill.getElement();
            boolean isPlayerTeam = self.getPos() < 4;
            Team team = (isPlayerTeam) ? battle.getPlayerTeam() : battle.getOpponentTeam();
            int cost = (self.isBooleanStat(BooleanStat.INFINITE_SP) && !skill.isBloom()) ? 0 : skill.getCost();

            // Make sure cost doesn't go below 1 unless the skill has a base 0 SP cost
            int costMod = (cost > 0) ?
                    (int) Math.max(cost * (AffLib.SP_COST.get(self.getAffinity(element)) / 1000d), 1) : 0;

            int change = skill.isBloom() ? costMod : (int) (costMod * self.getMultWithExp(Mult.SP_USE));
            spNew = skill.isBloom() ? team.getBloom() - change : self.getSp() - change;

            if (spNew < 0) {
                String msg = lang.format("log.skill_fail.sp", getTriesToUseString(move),
                        lang.format("log.but_doesnt_have_enough", booleanToInt(skill.isBloom())));
                appendToLog(msg);
            } else {
                Unit target = battle.getUnitAtPos(move.targetPos());

                boolean isBloom = skill.isBloom();
                int spOld = (isBloom) ? team.getBloom() : self.getSp();
                change *= -1;
                String changeSp = "";

                if (spOld != spNew) {
                    lang.format("log.skill_use.change_sp_bloom", booleanToInt(isBloom), spOld, spNew, change);
                }

                if (isBloom) {
                    team.setBloom(spNew);
                } else {
                    self.setSp(spNew);
                }

                appendToLog(lang.format("log.skill_use", formatName(self.getUnitType().getName(), self.getPos(), false),
                        skill.getNameWithIcon(C_SKILL),
                        formatName(target.getUnitType().getName(), move.targetPos(), false),
                        booleanToInt(skill.isRangeSelf()), changeSp));

                self.onUseSkill(target, skill);

                // Color move for currently acting combatant (temp)
                for (int i = 0; i < 8; i++) {
                    if (self.getPos() == i) {
                        movesL[i].setColor(Color.PINK);
                    } else {
                        movesL[i].setColor(Color.WHITE);
                    }
                }

                prevResults = new ResultType[8];
            }
        }

        Skill skill = move.skillInstance().getSkill();
        SkillEffect[] skillEffects = skill.getSkillEffects();

        Unit targetMain = battle.getUnitAtPos(move.targetPos());

        // The check for reaching skillEffects.length will only apply here if the length is 0, because otherwise it'll
        // be applied at the end
        if (spNew < 0 || applyingEffect == skillEffects.length || (nonFails == 0 && applyingEffect > 0)) {
            endMove();
            return;
        }

        for (int targetPos : skill.getRange().getTargetPositions(move.self().getPos(), move.targetPos())) {
            if (targetPos != -1) {
                Unit targetCur = battle.getUnitAtPos(targetPos);
                if (applyingEffect == 0) {
                    prevResults[targetPos] = ResultType.SUCCESS;

                    targetCur.onTargetedBySkill(move.self(), skill);
                }

                if (prevResults[targetPos] != ResultType.FAIL) {
                    nonFails++;

                    ResultType resultType = skillEffects[applyingEffect].apply(move.self(), targetCur,
                            targetCur == targetMain, prevResults[targetPos]);
                    prevResults[targetPos] = resultType;
                }
            }
        }

        if (!skillEffects[applyingEffect].isInstant()) {
            delayS += 0.25f * Settings.battleSpeed;
        }

        applyingEffect++;

        if (skillEffects.length == applyingEffect) {
            endMove();
            move.skillInstance().setCooldown(move.skillInstance().getSkill().getCooldown());
        }
        // todo delete killed units
    }

    static void handleTargeting() {
        if (InputLib.checkInput(Keybind.BACK)) {
            for (TextraLabel stat : statsL) {
                stat.setColor(Color.WHITE);
            }
            movesL[selectingMove].setText("");

            NAV_PATH.removeLast();
            return;
        }

        indexTarget = MenuLib.checkMovementTargeting(indexTarget, selectingMove,
                selectedSkillInstance.getSkill().getRange());

        MenuLib.handleOptColor(statsL, indexTarget);

        if (InputLib.checkInput(Keybind.CONFIRM)) {
            Unit self = battle.getPlayerTeam().getUnits()[selectingMove];
            Unit target = (indexTarget < 4) ? battle.getPlayerTeam().getUnits()[indexTarget] :
                    battle.getOpponentTeam().getUnits()[indexTarget - 4];
            moves.add(new Move(selectedSkillInstance, self, target.getPos()));
            // todo support ExA
            movesL[selectingMove].setText(
                    movesL[selectingMove].storedText + " → " + target.getUnitType().getName());

            for (TextraLabel stat : statsL) {
                stat.setColor(Color.WHITE);
            }

            // Move on to next Unit unless this one has extra actions
            if (extraActions < self.getExtraActions()) {
                extraActions++;
            } else {
                extraActions = 0;
                selectingMove++;
            }

            indexSkill = 0;

            NAV_PATH.removeLast();
        }
    }

    static void endTurn() {
        selectingMove = 0;
        usingMove = 0;
        battle.setTurn(battle.getTurn() + 1);

        turn.setText(C_TURN + lang.get("turn") + " " + (battle.getTurn() + 1));

        for (int i = 0; i < 8; i++) {
            movesL[i].setText("");
            movesL[i].setColor(Color.WHITE);
        }

        for (Unit unit : battle.getAllUnits()) {
            if (!unit.isBooleanStat(BooleanStat.INFINITE_SP)) {
                unit.setSp(Math.min((int) (unit.getSp() + (100 * unit.getMultWithExp(Mult.SP_GAIN))), 1000));
            }

            for (Passive passive : unit.getPassives()) {
                StringBuilder turnEnd1 = new StringBuilder();
                turnEnd1.append(
                        lang.format("log.turn_end_effect", formatName(unit.getUnitType().getName(), unit.getPos()),
                                C_PASSIVE + passive.getName()))
                        .append(" ");

                for (BuffEffect buffEffect : passive.getBuffEffects()) {
                    StringBuilder turnEnd2 = new StringBuilder();
                    String[] effectMsgs = buffEffect.onTurnEnd(unit, 1);

                    for (String effectMsg : effectMsgs) {
                        if (!effectMsg.isEmpty()) {
                            turnEnd2.append(effectMsg);
                        }
                    }

                    if (!turnEnd2.isEmpty()) {
                        appendToLog(turnEnd1 + turnEnd2.toString());
                    }
                }
            }

            for (BuffInstance buffInstance : unit.getBuffInstances()) {
                StringBuilder turnEnd1 = new StringBuilder();
                turnEnd1.append(
                        lang.format("log.turn_end_effect", formatName(unit.getUnitType().getName(), unit.getPos()),
                                buffInstance.getBuff().getIcon() + C_BUFF + buffInstance.getBuff().getName()))
                        .append(" ");

                for (BuffEffect buffEffect : buffInstance.getBuff().getBuffEffects()) {
                    StringBuilder turnEnd2 = new StringBuilder();
                    String[] effectMsgs = buffEffect.onTurnEnd(unit, buffInstance.getStacks());
                    for (String effectMsg : effectMsgs) {
                        if (!effectMsg.isEmpty()) {
                            turnEnd2.append(effectMsg);
                        }
                    }

                    if (!turnEnd2.isEmpty()) {
                        appendToLog(turnEnd1 + turnEnd2.toString());
                    }
                }
            }

            // Decrement stage/shield/buff turns and remove expired stages/shields/buffs
            unit.decrementTurns();
        }

        // todo is trailing white needed
        appendToLog(C_TURN + lang.get("turn") + " " + (battle.getTurn() + 1) + "[WHITE]");
        appendToLog(lang.get("log.gain_sp_bloom"));

        // Increase bloom
        battle.getPlayerTeam().setBloom(Math.min(battle.getPlayerTeam().getBloom() + 100, 1000));
        battle.getOpponentTeam().setBloom(Math.min(battle.getOpponentTeam().getBloom() + 100, 1000));
    }

    public static void updateStatDisplay() {
        for (int i = 0; i < 2; i++) {
            // todo colors
            // it seems [] and {} tags cant be mixed
            // setTextIfChanged(bloomL.get(i), "{SPEED=0.1}{FADE}{SLIDE}" + c_stat +
            // lang.get("bloom") + "[WHITE]: " + c_bloom + String.format("%,d",
            // battle.getTeam(i).getBloom()) + "[WHITE]/" + c_bloom + String.format("%,d",
            // 1000));
            // bloomL.get(i).skipToTheEnd();
            bloomL[i].setText("{SPEED=0.1}{FADE}{SLIDE}" + lang.get("bloom") + ": " +
                    formatNum(battle.getTeam(i).getBloom()) + "/" + formatNum(1000));
        }

        // Queue
        List<Unit> unitsAll = battle.getAllUnits();
        unitsAll.sort((a, b) -> Long.compare(b.getAgiWithStage(), a.getAgiWithStage()));

        // Copy and sort moves
        // todo do i really need 2 of these though
        // actually wtf is the purpose of this code?? i cant even tell what this is supposed to do??? i wrote this???
        // Only copy if it hasn't started emptying yet
        if (selectingMove == 8) {
            moves2 = new ArrayList<>(moves);
        } else if (selectingMove == 100) {
            moves2.sort(Comparator.comparingInt((Move entry) -> entry.skillInstance().getSkill().getPrio())
                    .thenComparingLong(entry -> entry.self().getAgiWithStage()).reversed());
            // todo is this supposed to change queue order? cuz it doesnt. probably remove
            // this or make it work
        } else {
            moves2 = new ArrayList<>();
        }

        StringBuilder queueText = new StringBuilder().append(lang.get("queue")).append(": ");

        for (int i = 0; i < unitsAll.size(); i++) {
            int pos = unitsAll.get(i).getPos();
            boolean active = pos == selectingMove;

            if (!active && usingMove > 0 && moves2.size() >= unitsAll.size()) {
                active = (moves2.get(usingMove - 1).self() == unitsAll.get(i));
            }

            queueText.append((getSide(pos) == Side.ALLY) ? C_ALLY : C_OPP);

            if (active) {
                queueText.append((getSide(pos) == Side.ALLY) ? C_ALLY_L : C_OPP_L).append("[[");
            }

            queueText.append(unitsAll.get(i).getUnitType().getName());

            if (active) {
                queueText.append("][WHITE]");
            }

            if (i != unitsAll.size() - 1) {
                queueText.append(", ");
            }
        }
        queue.setText(queueText.toString());

        // Update combatant stat display
        // todo disambiguation + system to display all status updates
        for (int i = 0; i < 8; i++) {
            // todo can use battle.getAlUnits
            Unit unit = (i >= 4) ? battle.getOpponentTeam().getUnits()[i - 4] : battle.getPlayerTeam().getUnits()[i];
            // todo unit cant be null for now, i think it might be possible later, impl better null safety then
            // if (unit != null) {
            long shield = unit.getDisplayShield() + unit.getDisplayDefend();
            // Account for overflow
            if (shield < 0) {
                shield = Long.MAX_VALUE;
            }
            // todo colors
            // String shieldStr = (shield > 0) ? c_shield + "+" + String.format("%,d",
            // shield) : "";
            // String spStr = (!unit.isInfiniteSp()) ? formatNum(unit.getSp()) +
            // "[WHITE]/" + c_sp + formatNum(1000) : "∞";
            // StringBuilder text = new StringBuilder(unit.getUnitType().getName() + "\n" +
            // c_stat + lang.get("hp") + "[WHITE]: " + c_hp + String.format("%,d",
            // unit.getStatsCur().getHp()) +
            // shieldStr + "[WHITE]/" + c_hp + String.format("%,d",
            // unit.getMaxHp()) + "\n" + c_stat + lang.get("sp") + "[WHITE]:
            // " + c_sp + spStr +
            // //"\nStr: " + unit.getStrWithStage() + "/" + unit.getStatsDefault().getStr()
            // + "\nMag:" + unit.getMagWithStage() + "/" + unit.getStatsDefault().getMag() +
            // //"\nAmr: " + unit.getAmrWithStage() + "/" + unit.getStatsDefault().getAmr()
            // + "\nRes: " + unit.getResWithStage() + "/" + unit.getStatsDefault().getRes()
            // +
            // "\n");
            /// Stat display
            String shieldStr = (shield > 0) ? "[CYAN]+" + formatNum(shield) + "[WHITE]" : "";
            String spStr = (!unit.isBooleanStat(BooleanStat.INFINITE_SP)) ?
                    formatNum(unit.getSp()) + "/" + formatNum(1000) : "∞";
            StringBuilder text = new StringBuilder(unit.getUnitType().getName() + "\n" + lang.get("hp") + ": " +
                    formatNum(unit.getDisplayHp()) + shieldStr + "/" + formatNum(unit.getDisplayMaxHp()) + "\n" +
                    lang.get("sp") + ": " + spStr); // + "\nStr: " + formatNum(unit.getStrWithStage()) + "/"
            // + formatNum(unit.getStatsDefault().getStr()) +
            // "\nMag:" + unit.getMagWithStage() + "/" + unit.getStatsDefault().getMag() +
            // "\nAmr: " + unit.getAmrWithStage() + "/" + unit.getStatsDefault().getAmr() +
            // "\nRes: " + unit.getResWithStage() + "/" + unit.getStatsDefault().getRes() +
            // "\n");

            statsL[i].setText(text.toString());

            /// Buff display

            int buffCount = 0;
            text = new StringBuilder();

            // List stage changes
            for (StageType stageType : STAGE_TYPES) {
                int stage = unit.getStage(stageType);
                if (stage != 0) {
                    if (buffCount > 0 && buffCount % 4 == 0) text.append("\n");

                    buffCount++;

                    text.append(stageType.getIcon()).append("[WHITE]").append((stage >= 1) ? "+" : "").append(stage)
                            .append("(").append(unit.getStageTurns(stageType)).append(") ");
                }
            }

            // List buffs
            List<BuffInstance> buffInstances = unit.getBuffInstances();
            if (!buffInstances.isEmpty()) {
                for (BuffInstance buffInstance : buffInstances) {
                    if (buffCount > 0 && buffCount % 4 == 0) text.append("\n");
                    buffCount++;

                    if (buffInstance.getBuff() == Buffs.DEFEND) {
                        text.append(buffInstance.getBuff().getIcon()).append("[WHITE]").append("x")
                                .append(formatNum(unit.getDisplayDefend())).append("(")
                                .append(buffInstance.getTurns()).append(") ");
                    } else if (buffInstance.getBuff() == Buffs.SHIELD) {
                        text.append(buffInstance.getBuff().getIcon()).append("[WHITE]").append("x")
                                .append(formatNum(unit.getDisplayShield())).append("(")
                                .append(buffInstance.getTurns()).append(") ");
                    } else {
                        text.append(buffInstance.getBuff().getIcon()).append("[WHITE]");
                        if (buffInstance.getBuff().getMaxStacks() > 1) {
                            text.append("x").append(buffInstance.getStacks());
                        }
                        // 1000+ turns = infinite
                        text.append("(");
                        if (buffInstance.getTurns() < 1000) {
                            text.append(buffInstance.getTurns());
                        } else text.append("∞");
                        text.append(") ");
                    }
                }
            }

            buffsL[i].setText(text.toString());
            /*
             * } else {
             * statsL[i].setText("");
             * buffsL[i].setText("");
             * }
             */
        }
    }

    public static void appendToLog(String... entries) {
        // todo figure out a good size limit
        if (logText.size() > 2500) {
            logText.subList(0, 500).clear();
        }

        for (String entry : entries) {
            if (!entry.isEmpty()) {
                logText.add(entry);
                logScroll = 0;
                updateLog();
            }
        }
    }

    public static void appendToLog(List<String> entry) {
        if (logText.size() > 2500) {
            logText.subList(0, 500).clear();
        }

        logText.addAll(entry);
        logScroll = 0;
        updateLog();
    }

    static void updateLog() {
        battleLog.setText(formatLog());

        if (NAV_PATH.getLast() == MenuLib.MenuType.LOG) {
            Path scrollbar = paths[0];

            // Portion of the log currently displayed
            float ratio = Math.min(48f / logText.size(), 1);

            // Maximum range for the bar to move
            int range = World.HEIGHT - 10;

            float barLength = range * ratio;

            range -= barLength;

            // Amount of current scroll. 0 = bottom; 1 = top
            float pos = (float) logScroll / Math.max(logText.size() - 48, 0);

            // Start (bottom)
            float sx = 1300 + (range * pos) / 6;
            float sy = 5 + (range * pos);

            // End (top)
            float ex = sx + (barLength / 6);
            float ey = sy + barLength;

            Array<Vector2> points = new Array<>(false, 2);
            points.addAll(new Vector2(sx, sy), new Vector2(ex, ey));
            scrollbar.setPoints(points);
        }
    }

    static String formatLog() {
        int lines = (NAV_PATH.getLast() == MenuLib.MenuType.LOG) ? 48 : 8;
        int scroll = (NAV_PATH.getLast() == MenuLib.MenuType.LOG) ? logScroll : 0;

        int start = Math.max(0, logText.size() - lines - scroll);
        int end = Math.min(start + lines, logText.size());
        List<String> subList = logText.subList(start, end);

        return String.join("\n", subList);
    }

    static void createFullLog() {
        battleLog.setAlignment(Align.topLeft);
        battleLog.setPosition(20, World.HEIGHT);
        NAV_PATH.add(MenuLib.MenuType.LOG);
        coolRects[CoolRects.COVER_LEFT.ordinal()].setDir(1).setPrio(3);
        paths[0].setDir(1).setThickness(10).setPrio(3);
        updateLog();
    }

    static void handleLog() {
        if (InputLib.checkInput(Keybind.BACK, Keybind.MENU)) {
            battleLog.setAlignment(Align.left);
            battleLog.setPosition(World.WIDTH_2 - 200, World.HEIGHT - 270);
            NAV_PATH.removeLast();
            updateLog();
            coolRects[CoolRects.COVER_LEFT.ordinal()].setDir(-1);
            paths[0].setDir(-1);
        }

        int logScrollNew = MenuLib.checkLogScroll(logScroll, logText.size(),
                (NAV_PATH.getLast() == MenuLib.MenuType.LOG) ? 48 : 8);
        if (logScroll != logScrollNew) {
            logScroll = logScrollNew;
            updateLog();
        }
    }

    static void createInspectTargeting() {
        for (TextraLabel skill : skillsL) {
            skill.setText("");
            skill.setColor(Color.WHITE);
        }

        NAV_PATH.add(MenuLib.MenuType.INSPECT_TARGETING);
    }

    static void handleInspectTargeting() {
        if (InputLib.checkInput(Keybind.BACK)) {
            for (TextraLabel label : statsL) {
                label.setColor(Color.WHITE);
            }
            movesL[selectingMove].setText("");

            NAV_PATH.removeLast();
            return;
        }

        indexTarget = MenuLib.checkMovementTargeting(indexTarget, selectingMove, Ranges.OTHER_3R_OR_SELF);

        MenuLib.handleOptColor(statsL, indexTarget);

        if (InputLib.checkInput(Keybind.CONFIRM, Keybind.MAP)) {
            createInspect();
        }
    }

    static void createInspect() {
        NAV_PATH.removeLast();
        NAV_PATH.add(MenuLib.MenuType.INSPECT);

        coolRects[CoolRects.COVER_LEFT.ordinal()].setDir(1).setPrio(4);
        coolRects[CoolRects.CURSOR_1.ordinal()].setDir(1).setPrio(4).setLR(45, 475);
        coolRects[CoolRects.CURSOR_2.ordinal()].setPrio(4).setLR(45, 475);

        paths[0].setDir(1).setPoints(pointsPageDivL).setThickness(5).setPrio(4);
        paths[1].setDir(1).setPoints(pointsPageDivR).setThickness(5).setPrio(4);
        paths[2].setDir(1).setPoints(pointsMult).setThickness(5).setPrio(4);
        paths[3].setDir(1).setPoints(pointsMod).setThickness(5).setPrio(4);
        paths[4].setDir(1).setPoints(pointsOther).setThickness(5).setPrio(4);

        List<Unit> unitsAll = battle.getAllUnits();
        StringBuilder unitNames = new StringBuilder();
        for (Unit unit : unitsAll) unitNames.append(unit.getUnitType().getName()).append(" ");
        unitList.setText(unitNames.toString());
        stage4.addActor(unitList);

        stage4.addActor(pageList);
        stage4.addActor(pageItemList);
        stage4.addActor(pageItemRightList);
        stage4.addActor(descHeader);
        stage4.addActor(desc);
        for (TextraLabel label : infoL) stage4.addActor(label);
        stage4.addActor(equip);
        stage4.addActor(affinities);
        for (TextraLabel label : statsBasicL) stage4.addActor(label);
        for (TextraLabel label : statsBasicNumL) stage4.addActor(label);
        stage4.addActor(hp);
        stage4.addActor(hpAmt);
        stage4.addActor(sp);
        stage4.addActor(spAmt);
        for (TextraLabel label : statCategoryHeaderL) stage4.addActor(label);
        for (TextraLabel label : statsPageL) stage4.addActor(label);
        for (TextraLabel label : statsPageNumL) stage4.addActor(label);
    }

    static void handleInspect() {
        if (InputLib.checkInput(Keybind.BACK)) {
            deleteInspect();
            return;
        }

        indexTarget = checkMovement1D(indexTarget, 8, Keybind.PAGE_L2, Keybind.PAGE_R2);
        Unit unit = battle.getUnitAtPos(indexTarget);

        equip.setText(unit.getEquipped().getNameWithIcon());

        affinities.setText(unit.getAffinitiesString());

        // todo bars
        hpAmt.setText(formatNum(unit.getDisplayHp()) + "/" + formatNum(unit.getDisplayMaxHp()));
        spAmt.setText(formatNum(unit.getSp()) + "/" + formatNum(1000));

        if (Settings.showInputGuide) {
            for (int i = 0; i < infoL.length; i++) {
                infoL[i].setText(infoPrompts[i].getText());
            }
        }

        Stat[] stats = Stat.values();
        for (int i = 0; i < statsBasicNumL.length; i++) {
            long statCur = unit.getStatWithStage(stats[i]);
            long statDefault = unit.getStatsDefault().getStat(stats[i]);
            double statPerc = ((double) statCur / statDefault) * 100;
            statsBasicNumL[i].setText(getStatColor(statCur, statDefault) + formatNum(statCur) + "[WHITE]/" + C_NUM +
                    formatNum(statDefault) + " [WHITE](" + getPercentageColor(statPerc) + formatNum(statPerc) +
                    "%[WHITE])");
        }

        indexPage = checkMovement1D(indexPage, 4, Keybind.LEFT, Keybind.RIGHT);

        if (InputLib.checkInput(Keybind.CONFIRM)) {
            createPopup(unit.getEquipped().getNameWithIcon(), unit.getEquipped().getDesc());
        } else if (InputLib.checkInput(Keybind.MENU)) {
            createPopup(lang.get("info.stat"), lang.get("info.stat.desc"));
        } else if (InputLib.checkInput(Keybind.MAP)) {
            createPopup(lang.get("info.affinity"), lang.get("info.affinity.desc"));
        }

        switch (indexPage) {
            case SKILLS:
                int skillCount = unit.getSkillCount();
                handleInspectPage(false, skillCount);

                List<SkillInstance> skillInstances = unit.getSkillInstances();

                pageItemList.setText(getNamesAsMultiline(skillInstances,
                        skillInstance -> skillInstance.getSkill().getNameWithIcon()));

                pageItemRightList.setText(getNamesAsMultiline(skillInstances,
                        skillInstance -> skillInstance.getSkill().getCostFormatted()));

                if (skillCount > 0) {
                    indexPageList = checkMovement1D(indexPageList, skillCount);

                    if (indexPageList < skillCount) {
                        Skill skill = unit.getSkill(indexPageList);
                        descHeader.setText(skill.getNameWithIcon());
                        desc.setText(skill.getDesc());
                    }
                }

                break;

            case PASSIVES:
                int passiveCount = unit.getPassiveCount();
                handleInspectPage(false, passiveCount);

                pageItemList.setText(getNamesAsMultiline(unit.getPassives(), Passive::getNameWithIcon));
                pageItemRightList.setText("");

                if (passiveCount > 0) {
                    indexPageList = checkMovement1D(indexPageList, passiveCount);

                    if (indexPageList < passiveCount) {
                        Passive passive = unit.getPassive(indexPageList);
                        descHeader.setText(passive.getNameWithIcon());
                        desc.setText(passive.getDesc());
                    }
                }

                break;

            case BUFFS:
                List<StageType> alteredStages = unit.getAlteredStages();
                int alteredStageCount = alteredStages.size();
                int buffCount = unit.getBuffCount();

                handleInspectPage(false, buffCount + alteredStageCount);

                pageItemList.setText(
                        getNamesAsMultiline(alteredStages,
                                stageType -> stageType.getNameWithIconAndSign(unit.getStage(stageType)), true) +
                                getNamesAsMultiline(unit.getBuffInstances(),
                                        buffInstance -> buffInstance.getBuff().getNameWithIcon()));

                pageItemRightList.setText(
                        getNamesAsMultiline(alteredStages, stageType -> stageType.getTurnsStacksFormatted(unit), true) +
                                getNamesAsMultiline(unit.getBuffInstances(), BuffInstance::getTurnsStacksFormatted));

                if (buffCount + alteredStageCount > 0) {
                    indexPageList = checkMovement1D(indexPageList, buffCount + alteredStageCount);

                    if (indexPageList < alteredStageCount) {
                        StageType stageType = alteredStages.get(indexPageList);
                        descHeader.setText(stageType.getNameWithIconAndSign(unit.getStage(stageType)));
                        desc.setText(stageType.getDesc());
                    } else if (indexPageList - alteredStageCount < buffCount) {
                        Buff buff = unit.getBuff(indexPageList - alteredStageCount);
                        descHeader.setText(buff.getNameWithIcon());
                        desc.setText(buff.getDesc());
                    }
                }

                break;

            case STATS:
                handleInspectPage(true, 0);

                if (InputLib.checkInput(Keybind.PAGE_L1)) {
                    createPopup(lang.get("info.mult"), lang.get("info.mult.desc"));
                } else if (InputLib.checkInput(Keybind.PAGE_R1)) {
                    createPopup(lang.get("info.mod"), lang.get("info.mod.desc"));
                } else if (InputLib.checkInput(Keybind.UP, Keybind.DOWN)) {
                    createPopup(lang.get("info.other"), lang.get("info.other.desc"));
                }

                break;
        }
    }

    static void handleInspectPage(boolean isStatsPage, int pageItemCount) {
        setStatVisibility(isStatsPage);
        setPageItemVisibility(pageItemCount > 0);

        if (isStatsPage || pageItemCount == 0) {
            coolRects[CoolRects.CURSOR_1.ordinal()].setDir(-1).setSpeed(4f);
            return;
        }

        MenuLib.handleCursor(coolRects[CoolRects.CURSOR_1.ordinal()], coolRects[CoolRects.CURSOR_2.ordinal()],
                indexPageList, 45, 475, World.HEIGHT - 367, 30, false);
        coolRects[CoolRects.CURSOR_1.ordinal()].setDir(1).setSpeed(2f);
    }

    static void setStatVisibility(boolean isStatsPage) {
        for (TextraLabel label : statCategoryHeaderL) label.setVisible(isStatsPage);
        for (TextraLabel label : statsPageL) label.setVisible(isStatsPage);
        for (TextraLabel label : statsPageNumL) label.setVisible(isStatsPage);

        infoL[3].setVisible(isStatsPage);
        infoL[4].setVisible(isStatsPage);
        infoL[5].setVisible(isStatsPage);

        paths[2].setDir(booleanToSign(isStatsPage));
        paths[3].setDir(booleanToSign(isStatsPage));
        paths[4].setDir(booleanToSign(isStatsPage));
    }

    static void setPageItemVisibility(boolean visible) {
        pageItemList.setVisible(visible);
        pageItemRightList.setVisible(visible);
        descHeader.setVisible(visible);
        desc.setVisible(visible);
    }

    static void deleteInspect() {
        NAV_PATH.removeLast();

        coolRects[CoolRects.COVER_LEFT.ordinal()].setDir(-1);
        coolRects[CoolRects.CURSOR_1.ordinal()].setDir(-1).setPrio(2);
        coolRects[CoolRects.CURSOR_2.ordinal()].setPrio(2);

        paths[0].setDir(-1);
        paths[1].setDir(-1);
        paths[2].setDir(-1);
        paths[3].setDir(-1);
        paths[4].setDir(-1);

        Group root = stage4.getRoot();

        root.removeActor(unitList);
        root.removeActor(pageList);
        root.removeActor(pageItemList);
        root.removeActor(pageItemRightList);
        root.removeActor(descHeader);
        root.removeActor(desc);
        for (TextraLabel label : infoL) root.removeActor(label);
        root.removeActor(equip);
        root.removeActor(affinities);
        for (TextraLabel label : statsBasicL) root.removeActor(label);
        for (TextraLabel label : statsBasicNumL) root.removeActor(label);
        root.removeActor(hp);
        root.removeActor(hpAmt);
        root.removeActor(sp);
        root.removeActor(spAmt);
        for (TextraLabel label : statCategoryHeaderL) root.removeActor(label);
        for (TextraLabel label : statsPageL) root.removeActor(label);
        for (TextraLabel label : statsPageNumL) root.removeActor(label);
    }

    static void selectMove() {
        if (InputLib.checkInput(Keybind.BACK) && selectingMove != 0) {
            for (TextraLabel skill : skillsL) {
                skill.setText("");
                skill.setColor(Color.WHITE);
            }

            selectingMove--;
            indexSkill = 0;
            movesL[selectingMove].setText("");

            for (int i = 0; i <= battle.getPlayerTeam().getUnits()[selectingMove].getExtraActions(); i++) {
                moves.removeLast();
            }

            return;
        }

        indexSkill = MenuLib.checkMovement1D(indexSkill, skillsL.size());

        MenuLib.handleOptColor(skillsL, indexSkill);

        if (InputLib.checkInput(Keybind.CONFIRM)) {
            selectedSkillInstance = battle.getPlayerTeam().getUnits()[selectingMove].getSkillInstances()
                    .get(indexSkill);
            movesL[selectingMove].setText(selectedSkillInstance.getSkill().getName());

            for (TextraLabel skill : skillsL) {
                skill.setText("");
                skill.setColor(Color.WHITE);
            }

            indexTarget = getStartingIndex(selectedSkillInstance.getSkill());
            NAV_PATH.add(MenuLib.MenuType.TARGETING);
        }
    }

    static void endMove() {
        usingMove++;
        applyingEffect = 0;
        nonFails = 0;
        moves.removeFirst();
        delayS += 1 * Settings.battleSpeed;
    }
}
