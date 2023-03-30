package foop.a1.server.entities;

import java.util.UUID;

public class Player {
    private final String playerId;
    private Position position;

    public Player() {
        this.playerId = UUID.randomUUID().toString();
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
