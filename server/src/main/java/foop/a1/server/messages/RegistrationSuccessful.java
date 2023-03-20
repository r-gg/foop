package foop.a1.server.messages;

import foop.a1.server.entities.Player;

public class RegistrationSuccessful implements Message {
    private Player player;

    private String gameId;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
