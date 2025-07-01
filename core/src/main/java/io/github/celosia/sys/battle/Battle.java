package io.github.celosia.sys.battle;

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

    public int getTurn() {
        return turn;
    }

    public Team getPlayerTeam() {
        return playerTeam;
    }

    public Team getOpponentTeam() {
        return opponentTeam;
    }
}
