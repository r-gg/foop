package foop.a1.server.messages.response;

import foop.a1.server.messages.Message;

public class GameOver implements Message {
    private Team winner;

    public GameOver(Team winner) {
        this.winner = winner;
    }

    public enum Team {
        PLAYERS,
        ENEMIES
    }

    public Team getWinner() {
        return winner;
    }

    public void setWinner(Team winner) {
        this.winner = winner;
    }
}
