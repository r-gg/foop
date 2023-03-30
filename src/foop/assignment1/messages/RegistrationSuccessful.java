package foop.assignment1.messages;

import foop.assignment1.entities.Player;

public class RegistrationSuccessful implements Message{
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
