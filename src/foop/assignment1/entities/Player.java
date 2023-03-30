package foop.assignment1.entities;

public class Player implements Locatable{

    private Position position;
    private PlayerId playerId;

    public Player() {
    }

    public PlayerId getPlayerId() {
        return this.playerId;
    }

    public void setPlayerId(PlayerId playerId) {
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
}