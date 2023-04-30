package foop.a1.server.entities;


public class Player {
    private final String playerId;
    private Position position;

    public Player(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerId() {
        return this.playerId;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean isAboveGround() {
        return true;
    }
}
