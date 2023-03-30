package foop.assignment1.messages;

import foop.assignment1.entities.Player;

public class RegistrationFailed implements Message{
    private Player player;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
