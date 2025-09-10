package io.github.celosia.sys.battle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// 2 Teams facing each other and a global field
public class Battle {

	private int turn;

	private final Team playerTeam;
	private final Team opponentTeam;

	// todo field effects

	public Battle(Team player, Team opponent) {
		this.turn = 0;

		this.playerTeam = player;
		this.opponentTeam = opponent;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public int getTurn() {
		return turn;
	}

	public Team getPlayerTeam() {
		return playerTeam;
	}

	public Team getOpponentTeam() {
		return opponentTeam;
	}

	public Team getTeam(int id) {
		return (id == 0) ? playerTeam : opponentTeam;
	}

	public Unit getUnitAtPos(int pos) {
		return (pos < 4) ? playerTeam.getUnits()[pos] : opponentTeam.getUnits()[pos - 4];
	}

	// Returns the team that the Unit at pos belongs to
	public Team getTeamAtPos(int pos) {
		return (pos < 4) ? playerTeam : opponentTeam;
	}

	public List<Unit> getAllUnits() {
		Unit[] unitsPlayer = playerTeam.getUnits();
		Unit[] unitsOpponent = opponentTeam.getUnits();
		List<Unit> unitsAll = new ArrayList<>();
		Collections.addAll(unitsAll, unitsPlayer);
		Collections.addAll(unitsAll, unitsOpponent);
		return unitsAll;
	}

	public List<BuffInstance> getAllBuffInstances() {
		List<BuffInstance> buffInstances = new ArrayList<>();
		List<Unit> unitsAll = getAllUnits();
		for (Unit unit : unitsAll) {
			Collections.addAll(buffInstances, unit.getBuffInstances().toArray(new BuffInstance[0]));
		}
		return buffInstances;
	}
}
