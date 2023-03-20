package foop.a1.server.messages;


import foop.a1.server.entities.Player;

public class RegistrationFailed implements Message {
    private Player player;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
