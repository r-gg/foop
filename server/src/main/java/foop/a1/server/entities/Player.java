package foop.a1.server.entities;


import foop.a1.server.util.Constants;

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
        this.position = new Position(
                Math.max(Math.min(position.x(), Constants.GAMEBOARD_WIDTH) , 0),
                Math.max(Math.min(position.y(), Constants.GAMEBOARD_HEIGHT) , 0)
        ) ;
    }

    public boolean isAboveGround() {
        return true;
    }
}
