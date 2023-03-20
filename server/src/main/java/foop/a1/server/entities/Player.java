package foop.a1.server.entities;

public class Player implements Locatable {

    private Position position;
    private String playerId;

    public Player() {
    }

    public String getPlayerId() {
        return this.playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean isAboveGround() {
        return true;
    }
}
