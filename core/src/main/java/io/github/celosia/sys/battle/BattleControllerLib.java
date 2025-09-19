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
import io.github.celosia.sys.menu.CoolRects;
import io.github.celosia.sys.menu.Fonts;
import io.github.celosia.sys.menu.InputLib;
import io.github.celosia.sys.menu.MenuLib;
import io.github.celosia.sys.menu.Path;
import io.github.celosia.sys.menu.Paths;
import io.github.celosia.sys.settings.Keybind;
import io.github.celosia.sys.settings.Settings;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static io.github.celosia.Main.coolRects;
import static io.github.celosia.Main.createPopup;
import static io.github.celosia.Main.menuList;
import static io.github.celosia.Main.paths;
import static io.github.celosia.Main.stage2;
import static io.github.celosia.Main.stage3;
import static io.github.celosia.sys.battle.AffLib.affSpCost;
import static io.github.celosia.sys.battle.BattleController.wait;
import static io.github.celosia.sys.battle.PosLib.getSide;
import static io.github.celosia.sys.battle.PosLib.getStartingIndex;
import static io.github.celosia.sys.menu.MenuLib.setTextIfChanged;
import static io.github.celosia.sys.menu.TextLib.c_ally;
import static io.github.celosia.sys.menu.TextLib.c_ally_l;
import static io.github.celosia.sys.menu.TextLib.c_bloom;
import static io.github.celosia.sys.menu.TextLib.c_buff;
import static io.github.celosia.sys.menu.TextLib.c_cd;
import static io.github.celosia.sys.menu.TextLib.c_opp;
import static io.github.celosia.sys.menu.TextLib.c_opp_l;
import static io.github.celosia.sys.menu.TextLib.c_passive;
import static io.github.celosia.sys.menu.TextLib.c_skill;
import static io.github.celosia.sys.menu.TextLib.c_sp;
import static io.github.celosia.sys.menu.TextLib.c_stat;
import static io.github.celosia.sys.menu.TextLib.c_turn;
import static io.github.celosia.sys.menu.TextLib.formatName;
import static io.github.celosia.sys.menu.TextLib.formatNum;
import static io.github.celosia.sys.menu.TextLib.getColor;
import static io.github.celosia.sys.menu.TextLib.getSign;
import static io.github.celosia.sys.menu.TextLib.getTriesToUseString;
import static io.github.celosia.sys.settings.Lang.lang;
import static io.github.celosia.sys.util.MiscLib.booleanToInt;

public class BattleControllerLib {
	// Battle
	public static Battle battle;

	// Display
	// Turn display
	static TypingLabel turn = new TypingLabel("{SPEED=0.1}{FADE}{SLIDE}" + c_turn + lang.get("turn") + " 1",
			Fonts.FontType.KORURI.getSize60());

	// Bloom displays for both teams
	static List<TypingLabel> bloomL = new ArrayList<>();

	// Queue (move order) display
	static TypingLabel queue = new TypingLabel("", Fonts.FontType.KORURI.getSize30());

	// Stat displays for all units
	static List<TypingLabel> statsL = new ArrayList<>();

	// Move selection displays
	static List<TypingLabel> movesL = new ArrayList<>();

	// Skill menu display
	static List<TypingLabel> skillsL = new ArrayList<>();

	// temp
	static Skill[] skills = new Skill[]{Skills.STAR_RULER, Skills.ATTACK_UP, Skills.SHIELD, Skills.THUNDERBOLT,
			Skills.ICE_AGE, Skills.DEFEND};
	static Skill[] skills2 = new Skill[]{Skills.STAR_RULER, Skills.FIREBALL, Skills.ATTACK_UP_GROUP, Skills.SHIELD,
			Skills.ICE_AGE, Skills.DEFEND};
	static Skill[] skills3 = new Skill[]{Skills.DEFENSE_DOWN, Skills.FIREBALL, Skills.HEAT_WAVE, Skills.PROTECT,
			Skills.ICE_AGE, Skills.DEFEND};

	// Battle log
	// todo fix positioning
	static TextraLabel battleLog = new TextraLabel("", Fonts.FontType.KORURI.getSize20());

	// Actions being made this turn
	static List<Move> moves = new ArrayList<>();
	// Copy for queue
	static List<Move> moves2 = new ArrayList<>();

	// How many extra actions have been used for the current combatant
	static int extraActions = 0;

	// Pos of the Unit that's currently using their move
	static int usingMove = 0;

	// Index of the SkillEffect of the current skill that's currently being applied
	static int applyingEffect = 0;

	// Previous SkillEffect resultTypes for each pos
	// todo is it better if this just an 8-len array
	static Int2ObjectMap<ResultType> prevResults;

	// Amount of non-fail results for this skill
	static int nonFails = 0;

	// Menu navigation
	static int indexSkill = 0;
	static int indexTarget = 0;

	// Currently selected skill
	static SkillInstance selectedSkillInstance;

	// Who's currently selecting their move. 0-3 = player; 4-8 = opponent; 100 =
	// moves are executing
	static int selectingMove = 0;

	// temp
	static SkillInstance nothingInstanceTemp = new SkillInstance(Skills.NOTHING);

	static List<String> logText = new ArrayList<>();

	// Amount of lines scrolled upwards in the log
	static int logScroll = 0;

	static void handleSetup() {
		// Setup teams (temp)
		Stats johnyStats = new Stats(100, 100, 100, 100, 100, 100, 100);
		UnitType johny = new UnitType("Johny", johnyStats, new Affinities(4, -4, 0, 0, 0, 0, 0),
				Passives.DEBUFF_DURATION_UP, Passives.RESTORATION, Passives.PERCENTAGE_DMG_TAKEN_DOWN_50);
		Stats jerryStats = new Stats(100, 100, 100, 100, 100, 100, 115);
		UnitType jerry = new UnitType("Jerry", jerryStats, new Affinities(5, -4, 0, 5, 0, 0, 0),
				Passives.DEBUFF_DURATION_UP);
		UnitType james = new UnitType("James", jerryStats, new Affinities(-4, 5, 0, 0, 0, 0, 0),
				Passives.DEBUFF_DURATION_UP, Passives.ETERNAL_WELLSPRING);
		UnitType jacob = new UnitType("Jacob", johnyStats, new Affinities(0, 0, 5, -4, 0, 0, 0),
				Passives.DEBUFF_DURATION_UP);
		UnitType julia = new UnitType("Julia", johnyStats, new Affinities(0, 0, -4, 5, 0, 0, 0),
				Passives.DEBUFF_DURATION_UP);
		UnitType jude = new UnitType("Jude", jerryStats, new Affinities(0, 0, -3, -3, 5, 0, 0),
				Passives.DEBUFF_DURATION_UP, Passives.PERCENTAGE_DMG_TAKEN_DOWN_999);
		UnitType josephine = new UnitType("Josephine", jerryStats, new Affinities(0, 0, 0, 0, 0, 5, -4),
				Passives.DEBUFF_DURATION_UP);
		UnitType julian = new UnitType("Julian", johnyStats, new Affinities(0, 0, 0, 0, 0, -4, 5),
				Passives.DEBUFF_DURATION_UP);

		Team player = new Team(new Unit[]{new Unit(johny, 19, skills, 0), new Unit(james, 19, skills2, 1),
				new Unit(julia, 19, skills3, 2), new Unit(josephine, 19, skills, 3)});

		Team opponent = new Team(new Unit[]{new Unit(jerry, 19, skills2, 4), new Unit(jacob, 19, skills2, 5),
				new Unit(jude, (long) 1E15, skills2, 6), new Unit(julian, 19, skills2, 7)});

		battle = new Battle(player, opponent);

		// Turn display
		turn.setPosition(World.WIDTH_2, World.HEIGHT - 60, Align.center);
		stage2.addActor(turn);

		for (int i = 0; i < 2; i++) {
			// Bloom displays for both teams
			TypingLabel bloom = new TypingLabel(
					c_stat + lang.get("bloom") + "[WHITE]: " + c_bloom + "100[WHITE]/" + c_bloom + "1,000",
					Fonts.FontType.KORURI.getSize40());
			bloomL.add(bloom);
			bloom.setY(World.HEIGHT - 90);
			stage2.addActor(bloom);
		}

		// Queue (move order) display
		stage2.addActor(queue);

		// Log
		stage3.addActor(battleLog);

		for (int i = 0; i < 8; i++) {
			int y = (i >= 4) ? World.HEIGHT - 300 - 300 * (i - 4) : World.HEIGHT - 300 - 300 * i;

			// Stat displays for all units
			TypingLabel stats = new TypingLabel("", Fonts.FontType.KORURI.getSize30());
			statsL.add(stats);
			stats.setPosition((i >= 4) ? World.WIDTH - 350 : 50, y);
			stage2.addActor(stats);

			// Move selection displays
			TypingLabel moves = new TypingLabel("", Fonts.FontType.KORURI.getSize30());
			movesL.add(moves);
			moves.setPosition((i >= 4) ? World.WIDTH - 550 : 400, y);
			stage2.addActor(moves);
		}

		// Skill menu display
		// todo support arbitrary size
		for (int i = 0; i < 6; i++) {
			skillsL.add(new TypingLabel("", Fonts.FontType.KORURI.getSize30()));
			stage2.addActor(skillsL.get(i));
		}

		// Notify Passives onGive
		for (Unit unit : battle.getAllUnits()) {
			for (Passive passive : unit.getPassives()) {
				for (BuffEffect buffEffect : passive.buffEffects())
					buffEffect.onGive(unit, 1);
			}
		}

		// Log
		appendToLog(c_turn + lang.get("turn") + " " + 1 + "[WHITE]");
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
		if (selectingMove <= 3) { // Player's turn
			selectPlayerMove();
		} else if (selectingMove <= 8) { // opponent's turn
			selectOpponentMove();
		} else if (selectingMove == 100) { // Moves play out
			executeMove();
		}
	}

	static void selectPlayerMove() {
		if (selectingMove < battle.getPlayerTeam().getUnits().length) { // if there are more allies yet to act
			// Skill selection display
			// todo support arbitrary size
			for (int i = 0; i < 6; i++) {
				skillsL.get(i).setPosition(600, (World.HEIGHT - 400 - 250 * selectingMove) - ((i - 2) * 35));
				// todo support ExA
				setTextIfChanged(skillsL.get(i),
						battle.getPlayerTeam().getUnits()[selectingMove].getSkillInstances()[i].getSkill()
								// temp
								// todo real skill description display
								.getName() + "(" + c_cd
								+ battle.getPlayerTeam().getUnits()[selectingMove].getSkillInstances()[i].getCooldown()
								+ "[WHITE])");
				skillsL.get(i).skipToTheEnd();
			}

			selectMove();
		}
	}

	static void selectOpponentMove() {
		if (!Debug.selectOpponentMoves) {
			// If there are more opponents yet to act
			if ((selectingMove - 4) < battle.getOpponentTeam().getUnits().length) {
				// Skill selectedSkill = skills2[MathUtils.random(skills.length - 1)];
				SkillInstance selectedSkillInstance = nothingInstanceTemp;
				Unit target = battle.getPlayerTeam().getUnits()[MathUtils
						.random(battle.getPlayerTeam().getUnits().length - 1)];
				// todo support ExA
				setTextIfChanged(movesL.get(selectingMove),
						selectedSkillInstance.getSkill().getName() + " → " + target.getUnitType().name());
				moves.add(new Move(selectedSkillInstance, battle.getOpponentTeam().getUnits()[selectingMove - 4],
						target.getPos())); // todo AI
				selectingMove++;
			} else
				selectingMove = 100; // Jump to move execution
		} else {
			selectMove();
		}
	}

	static void executeMove() {
		// All moves have happened; end turn
		if (moves.isEmpty()) {
			endTurn();
			return;
		}

		// Sort moves by Prio and then by Agi
		moves.sort(Comparator.comparingInt((Move entry) -> entry.skillInstance().getSkill().getPrio())
				.thenComparingLong(entry -> entry.self().getAgiWithStage()).reversed());

		// The next move plays out
		Move move = moves.getFirst();

		if (move.isInRange()) {
			int cd = move.skillInstance().getCooldown();
			// Only check cooldown at the start
			if (cd == 0 || applyingEffect > 0) {
				// Invalid newSp will cancel move
				int newSp = 0;

				// Execute move
				// Setup at start of execution
				if (applyingEffect == 0) {
					// Set cooldown
					move.skillInstance().setCooldown(cd);

					// Set newSp
					Unit self = move.self();
					Skill skill = move.skillInstance().getSkill();

					Element element = skill.getElement();
					boolean isPlayerTeam = self.getPos() < 4;
					Team team = (isPlayerTeam) ? battle.getPlayerTeam() : battle.getOpponentTeam();
					int cost = (self.isInfiniteSp() && !skill.isBloom()) ? 0 : skill.getCost();
					// Make sure cost doesn't go below 1 unless the skill has a base 0 SP cost
					int costMod = (cost > 0)
							? (int) Math.max(cost * (affSpCost.get(self.getAffsCur().getAff(element)) / 1000d), 1)
							: 0;
					int change = skill.isBloom() ? costMod : (int) (costMod * self.getMultWithExpSpUse());
					newSp = skill.isBloom() ? team.getBloom() - change : self.getSp() - change;

					if (newSp >= 0) {
						Unit target = battle.getUnitAtPos(move.targetPos());

                        change *= -1;

						if (skill.isBloom() && newSp != team.getBloom()) {
							appendToLog(lang.format("log.skill_use",
									formatName(self.getUnitType().name(), self.getPos(), false),
									c_skill + skill.getName(),
									formatName(target.getUnitType().name(), move.targetPos(), false),
									booleanToInt(skill.isRangeSelf()), 1, c_bloom + formatNum(team.getBloom()), c_bloom + formatNum(newSp), getColor(change) + getSign(change) + formatNum(change)));
							team.setBloom(newSp);
						} else if (!skill.isBloom() && newSp != self.getSp()) {
							appendToLog(lang.format("log.skill_use",
									formatName(self.getUnitType().name(), self.getPos(), false),
									c_skill + skill.getName(),
									formatName(target.getUnitType().name(), move.targetPos(), false),
									booleanToInt(skill.isRangeSelf()), 0, c_sp + formatNum(self.getSp()), c_sp + formatNum(newSp), getColor(change) + getSign(change) + change));
							self.setSp(newSp);
						}

						// Apply on-skill use BuffEffects
						self.onUseSkill(target, skill);

						// Color move for currently acting combatant (temp)
						for (int i = 0; i < 8; i++) {
							// statsL.get(i).setX(((self.getPos()) == i) ? (i >= 4) ? World.WIDTH - 300 :
							// 200 : (i >= 4) ? World.WIDTH - 250 : 150);
							if (self.getPos() == i) {
								movesL.get(i).setColor(Color.PINK);
							} else
								movesL.get(i).setColor(Color.WHITE);
						}

						prevResults = new Int2ObjectOpenHashMap<>();
					} else {
						String msg = lang.format("log.skill_fail.no_sp", getTriesToUseString(move),
								lang.format("log.but_doesnt_have_enough", booleanToInt(skill.isBloom())));
						appendToLog(msg);
					}
				}

				Skill skill = move.skillInstance().getSkill();
				List<SkillEffect> skillEffects = skill.getSkillEffects();

				Unit targetMain = battle.getUnitAtPos(move.targetPos());

				// Make sure newSp is valid
				// Apply SkillEffects one at a time
				// Looking for at least 1 non-fail to continue the skill after the first effect
				if (newSp >= 0 && applyingEffect < skillEffects.size() && (nonFails > 0 || applyingEffect == 0)) {
					// Apply to all targets
					for (int targetPos : skill.getRange().getTargetPositions(move.self().getPos(), move.targetPos())) {
						if (targetPos != -1) { // Target's position is valid
							Unit targetCur = battle.getUnitAtPos(targetPos);
							if (applyingEffect == 0) { // First effect
								// Initialize prevResults
								prevResults.put(targetPos, ResultType.SUCCESS);

								// Apply onTargetedBySkill BuffEffects
								targetCur.onTargetedBySkill(move.self(), skill);
							}

							// Hasn't failed on this target yet
							if (prevResults.get(targetPos) != ResultType.FAIL) {
								// Not a fail
								nonFails++;

								// Apply effect and track result
								ResultType resultType = skillEffects.get(applyingEffect).apply(move.self(), targetCur,
										targetCur == targetMain, prevResults.get(targetPos));
								prevResults.put(targetPos, resultType);
							}
						}
					}
					// Wait a bit before next SkillEffect
					if (!skillEffects.get(applyingEffect).isInstant()) {
						wait += 0.25f * Settings.battleSpeed;
					}

					applyingEffect++;

					// Just finished applying the last effect
					if (skillEffects.size() == applyingEffect) {
						endMove();
						// Set cooldown
						move.skillInstance().setCooldown(move.skillInstance().getSkill().getCooldown());
					}
				} else {
					endMove();
				}
				// todo delete killed units
			} else {
				appendToLog(lang.format("log.skill_fail.cooldown", getTriesToUseString(move),
						lang.format("log.but_its_on_cooldown", c_cd + cd)));
				endMove();
			}
		} else {
			appendToLog(lang.format("log.skill_fail.range", getTriesToUseString(move), lang.get("log.but_cant_reach")));
			endMove();
		}
	}

	static void handleLog() {
		// Go back
		if (InputLib.checkInput(Keybind.BACK, Keybind.MENU)) {
			menuList.removeLast();
			updateLog();
			coolRects.get(CoolRects.COVER_LEFT.ordinal()).setDir(-1);
			paths.get(Paths.SCROLLBAR.ordinal()).setDir(-1);
		}

		// Scroll
		int logScrollNew = MenuLib.checkLogScroll(logScroll, logText.size(),
				(menuList.getLast() == MenuLib.MenuType.LOG) ? 48 : 8);
		if (logScroll != logScrollNew) {
			logScroll = logScrollNew;
			updateLog();
		}
	}

	static void handleTargeting() {
		if (InputLib.checkInput(Keybind.BACK)) {
			// Reset for next time
			for (TypingLabel stat : statsL)
				stat.setColor(Color.WHITE);
			movesL.get(selectingMove).setText("");

			menuList.removeLast();
			return;
		}

		// Handle menu navigation
		indexTarget = MenuLib.checkMovementTargeting(indexTarget, selectingMove,
				selectedSkillInstance.getSkill().getRange());

		// Handle option colors
		MenuLib.handleOptColor(statsL, indexTarget);

		// Add selection to move queue
		if (InputLib.checkInput(Keybind.CONFIRM)) {
			Unit self = battle.getPlayerTeam().getUnits()[selectingMove];
			Unit target = (indexTarget < 4)
					? battle.getPlayerTeam().getUnits()[indexTarget]
					: battle.getOpponentTeam().getUnits()[indexTarget - 4];
			moves.add(new Move(selectedSkillInstance, self, target.getPos()));
			// todo support ExA
			setTextIfChanged(movesL.get(selectingMove),
					movesL.get(selectingMove).getOriginalText() + " → " + target.getUnitType().name());

			// Reset for next time
			for (TypingLabel stat : statsL)
				stat.setColor(Color.WHITE);

			// Move on to next combatant to select a move for unless this one has extra
			// actions
			if (extraActions < self.getExtraActions()) {
				extraActions++;
			} else {
				extraActions = 0;
				selectingMove++;
			}

			indexSkill = 0;

			menuList.removeLast();
		}
	}

	static void endTurn() {
		selectingMove = 0;
		usingMove = 0;
		battle.setTurn(battle.getTurn() + 1);

		// Update turn display
		turn.setText(c_turn + lang.get("turn") + " " + (battle.getTurn() + 1));

		// Reset stat/move displays to normal
		for (int i = 0; i < 8; i++) {
			// statsL.get(i).setX((i >= 4) ? World.WIDTH - 250 : 150);
			movesL.get(i).setText("");
			movesL.get(i).setColor(Color.WHITE);
		}

		for (Unit unit : battle.getAllUnits()) {
			// Increase SP
			if (!unit.isInfiniteSp())
				unit.setSp(Math.min((int) (unit.getSp() + (100 * unit.getMultWithExpSpGain())), 1000));

			// Apply turn end BuffEffects
			for (Passive passive : unit.getPassives()) {
				StringBuilder turnEnd1 = new StringBuilder();
				turnEnd1.append(lang.format("log.turn_end_effect", formatName(unit.getUnitType().name(), unit.getPos()),
						c_passive + passive.name())).append(" ");

				for (BuffEffect buffEffect : passive.buffEffects()) {
					StringBuilder turnEnd2 = new StringBuilder();
					String[] effectMsgs = buffEffect.onTurnEnd(unit, 1);
					for (String effectMsg : effectMsgs)
						if (!effectMsg.isEmpty())
							turnEnd2.append(effectMsg);

					// Only have turn end message if both have messages
					if (!turnEnd2.isEmpty())
						appendToLog(turnEnd1 + turnEnd2.toString());
				}
			}

			for (BuffInstance buffInstance : unit.getBuffInstances()) {
				StringBuilder turnEnd1 = new StringBuilder();
				turnEnd1.append(lang.format("log.turn_end_effect", formatName(unit.getUnitType().name(), unit.getPos()),
						c_buff + buffInstance.getBuff().name())).append(" ");

				for (BuffEffect buffEffect : buffInstance.getBuff().buffEffects()) {
					StringBuilder turnEnd2 = new StringBuilder();
					String[] effectMsgs = buffEffect.onTurnEnd(unit, buffInstance.getStacks());
					for (String effectMsg : effectMsgs)
						if (!effectMsg.isEmpty())
							turnEnd2.append(effectMsg);

					if (!turnEnd2.isEmpty())
						appendToLog(turnEnd1 + turnEnd2.toString());
				}
			}

			// Decrement stage/shield/buff turns and remove expired stages/shields/buffs
			unit.decrementTurns();
		}

		// Log
		appendToLog(c_turn + lang.get("turn") + " " + (battle.getTurn() + 1) + "[WHITE]"); // todo is trailing white
																							// needed
		appendToLog(lang.get("log.gain_sp_bloom"));

		// Increase bloom
		battle.getPlayerTeam().setBloom(Math.min(battle.getPlayerTeam().getBloom() + 100, 1000));
		battle.getOpponentTeam().setBloom(Math.min(battle.getOpponentTeam().getBloom() + 100, 1000));
	}

	public static void updateStatDisplay() {
		// Bloom displays
		for (int i = 0; i < 2; i++) {
			// todo colors
			// it seems [] and {} tags cant be mixed
			// setTextIfChanged(bloomL.get(i), "{SPEED=0.1}{FADE}{SLIDE}" + c_stat +
			// lang.get("bloom") + "[WHITE]: " + c_bloom + String.format("%,d",
			// battle.getTeam(i).getBloom()) + "[WHITE]/" + c_bloom + String.format("%,d",
			// 1000));
			// bloomL.get(i).skipToTheEnd();
			setTextIfChanged(bloomL.get(i), "{SPEED=0.1}{FADE}{SLIDE}" + lang.get("bloom") + ": "
					+ formatNum(battle.getTeam(i).getBloom()) + "/" + formatNum(1000));
			if (i == 0)
				bloomL.get(i).setX(bloomL.get(i).getWidth() / 3, Align.left);
			else
				bloomL.get(i).setX(World.WIDTH - bloomL.get(i).getWidth() / 3, Align.right);
		}

		// Queue
		// Sort units by Agi
		List<Unit> unitsAll = battle.getAllUnits();
		unitsAll.sort((a, b) -> Long.compare(b.getAgiWithStage(), a.getAgiWithStage()));

		// Copy and sort moves
		// Only copy if it hasn't started emptying yet
		if (selectingMove == 8)
			moves2 = new ArrayList<>(moves);
		else if (selectingMove == 100) {
			moves2.sort(Comparator.comparingInt((Move entry) -> entry.skillInstance().getSkill().getPrio())
					.thenComparingLong(entry -> entry.self().getAgiWithStage()).reversed());
			// todo will thenComparingInt(pos.reversed) work
		} else
			moves2 = new ArrayList<>();

		StringBuilder queueText = new StringBuilder().append(lang.get("queue")).append(": ");

		for (int i = 0; i < unitsAll.size(); i++) {
			int pos = unitsAll.get(i).getPos();
			boolean active = pos == selectingMove;
			if (!active && usingMove > 0 && moves2.size() >= unitsAll.size())
				active = (moves2.get(usingMove - 1).self() == unitsAll.get(i));
			queueText.append((getSide(pos) == Side.ALLY) ? c_ally : c_opp);
			if (active)
				queueText.append((getSide(pos) == Side.ALLY) ? c_ally_l : c_opp_l).append("[[");
			queueText.append(unitsAll.get(i).getUnitType().name());
			if (active)
				queueText.append("][WHITE]");
			if (i != unitsAll.size() - 1)
				queueText.append(", ");
		}
		queue.setText(queueText.toString());
		queue.setPosition(World.WIDTH_2, World.HEIGHT - 120, Align.center); // Must be set here so it aligns properly
		queue.skipToTheEnd();

		// Update combatant stat display
		// todo disambiguation + system to display all status updates
		for (int i = 0; i < 8; i++) {
			// todo can use battle.getAlUnits
			Unit unit = (i >= 4) ? battle.getOpponentTeam().getUnits()[i - 4] : battle.getPlayerTeam().getUnits()[i];
			if (unit != null) {
				long shield = unit.getDisplayShield() + unit.getDisplayDefend();
				// todo colors
				// String shieldStr = (shield > 0) ? c_shield + "+" + String.format("%,d",
				// shield) : "";
				// String spStr = (!unit.isInfiniteSp()) ? formatNum(unit.getSp()) +
				// "[WHITE]/" + c_sp + formatNum(1000) : "∞";
				// StringBuilder text = new StringBuilder(unit.getUnitType().getName() + "\n" +
				// c_stat + lang.get("hp") + "[WHITE]: " + c_hp + String.format("%,d",
				// unit.getStatsCur().getHp()) +
				// shieldStr + "[WHITE]/" + c_hp + String.format("%,d",
				// unit.getStatsDefault().getHp()) + "\n" + c_stat + lang.get("sp") + "[WHITE]:
				// " + c_sp + spStr +
				// //"\nStr: " + unit.getStrWithStage() + "/" + unit.getStatsDefault().getStr()
				// + "\nMag:" + unit.getMagWithStage() + "/" + unit.getStatsDefault().getMag() +
				// //"\nAmr: " + unit.getAmrWithStage() + "/" + unit.getStatsDefault().getAmr()
				// + "\nRes: " + unit.getResWithStage() + "/" + unit.getStatsDefault().getRes()
				// +
				// "\n");
				String shieldStr = (shield > 0) ? "[CYAN]+" + formatNum(shield) + "[WHITE]" : "";
				String spStr = (!unit.isInfiniteSp()) ? formatNum(unit.getSp()) + "/" + formatNum(1000) : "∞";
				StringBuilder text = new StringBuilder(unit.getUnitType().name() + "\n" + lang.get("hp") + ": "
						+ formatNum(unit.getStatsCur().getDisplayHp()) + shieldStr + "/"
						+ formatNum(unit.getStatsDefault().getDisplayHp()) + "\n" + lang.get("sp") + ": " + spStr
						+ "\nStr: " + formatNum(unit.getStrWithStage()) + "/"
						+ formatNum(unit.getStatsDefault().getStr()) +
						// "\nMag:" + unit.getMagWithStage() + "/" + unit.getStatsDefault().getMag() +
						// "\nAmr: " + unit.getAmrWithStage() + "/" + unit.getStatsDefault().getAmr() +
						// "\nRes: " + unit.getResWithStage() + "/" + unit.getStatsDefault().getRes() +
						"\n");

				// List stage changes
				for (StageType stageType : StageType.values()) {
					int stage = unit.getStage(stageType);
					if (stage != 0) {
						text.append(stageType.getName()).append((stage >= 1) ? "+" : "").append(stage).append("(")
								.append(unit.getStageTurns(stageType)).append(") ");
					}
				}

				// Shield
				shield = unit.getDisplayShield();
				if (shield > 0)
					text.append(lang.get("shield")).append("x").append(formatNum(shield)).append("(")
							.append(unit.getShieldTurns()).append(") ");

				// List buffs
				List<BuffInstance> buffInstances = unit.getBuffInstances();
				if (!buffInstances.isEmpty()) {
					for (BuffInstance buffInstance : buffInstances) {
						if (buffInstance.getBuff() == Buffs.DEFEND) {
							text.append(buffInstance.getBuff().name()).append("x")
									.append(formatNum(unit.getDisplayDefend())).append("(")
									.append(buffInstance.getTurns()).append(") ");
						} else {
							text.append(buffInstance.getBuff().name());
							if (buffInstance.getBuff().maxStacks() > 1)
								text.append("x").append(buffInstance.getStacks());
							// 1000+ turns = infinite
							text.append("(");
							if (buffInstance.getTurns() < 1000)
								text.append(buffInstance.getTurns());
							else
								text.append("∞");
							text.append(") ");
						}
					}
				}
				setTextIfChanged(statsL.get(i), text.toString());
			} else
				statsL.get(i).setText("");
		}
	}

	public static void appendToLog(String... entries) {
		// If logText exceeds 2500 lines, remove ~500 lines (so this doesn't have to get
		// called again soon)
		// todo figure out a good size limit
		if (logText.size() > 2500) {
			logText.subList(0, 500).clear();
		}

		for (String entry : entries) {
			if (!entry.isEmpty()) {
				logText.add(entry);

				// Reset scroll to bottom
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

		// Reset scroll to bottom
		logScroll = 0;

		updateLog();
	}

	static void updateLog() {
		battleLog.setText(formatLog());

		// Scrollbar
		if (menuList.getLast() == MenuLib.MenuType.LOG) {
			battleLog.setPosition(20, World.HEIGHT_2, Align.topLeft);

			Path scrollbar = paths.get(Paths.SCROLLBAR.ordinal());

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

		} else
			battleLog.setPosition(World.WIDTH_2 - 200, World.HEIGHT - 270, Align.left);
	}

	static String formatLog() {
		int lines = (menuList.getLast() == MenuLib.MenuType.LOG) ? 48 : 8;
		int scroll = (menuList.getLast() == MenuLib.MenuType.LOG) ? logScroll : 0;
		if (logText == null || logText.isEmpty())
			return "";
		int start = Math.max(0, logText.size() - lines - scroll);
		int end = Math.min(start + lines, logText.size());
		List<String> subList = logText.subList(start, end);
		return String.join("\n", subList);
	}

	static void createFullLog() {
		menuList.add(MenuLib.MenuType.LOG);
		coolRects.get(CoolRects.COVER_LEFT.ordinal()).setDir(1);
		paths.get(Paths.SCROLLBAR.ordinal()).setDir(1);
		updateLog();
	}

	static void selectMove() {
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
			for (int i = 0; i <= battle.getPlayerTeam().getUnits()[selectingMove].getExtraActions(); i++)
				moves.removeLast();

			// menuList.removeLast();
			return;
		}

		// Handle menu navigation
		indexSkill = MenuLib.checkMovement1D(indexSkill, skillsL.size());

		// Handle option colors
		MenuLib.handleOptColor(skillsL, indexSkill);

		// Move selected
		if (InputLib.checkInput(Keybind.CONFIRM)) {
			selectedSkillInstance = battle.getPlayerTeam().getUnits()[selectingMove].getSkillInstances()[indexSkill];
			setTextIfChanged(movesL.get(selectingMove),
					lang.get("skill") + ": " + selectedSkillInstance.getSkill().getName());

			// Reset for next time
			for (TypingLabel skill : skillsL) {
				skill.setText("");
				skill.setColor(Color.WHITE);
			}

			// Prepare menus
			indexTarget = getStartingIndex(selectedSkillInstance.getSkill());

			menuList.add(MenuLib.MenuType.TARGETING);
		}
	}

	static void endMove() {
		usingMove++;
		applyingEffect = 0;
		moves.removeFirst();
		wait += 1 * Settings.battleSpeed;
	}
}
