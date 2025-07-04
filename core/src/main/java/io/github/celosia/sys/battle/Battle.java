package io.github.celosia.sys.battle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// 2 Teams facing each other and a global field
public class Battle {

    int turn;

    Team playerTeam;
    Team opponentTeam;

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

    public List<Combatant> getAllCombatants() {
        Combatant[] cmbsPlayer = playerTeam.getCmbs();
        Combatant[] cmbsOpponent = opponentTeam.getCmbs();
        List<Combatant> cmbsAll = new ArrayList<Combatant>();
        Collections.addAll(cmbsAll, cmbsPlayer);
        Collections.addAll(cmbsAll, cmbsOpponent);
        return cmbsAll;
    }
}
