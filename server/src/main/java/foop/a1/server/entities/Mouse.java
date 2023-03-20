package foop.a1.server.entities;

public class Mouse implements Locatable {
    private Position position;

    @Override
    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public Position getPosition() {
        return this.position;
    }

    @Override
    public boolean isAboveGround() {
        return false;
    }
}
