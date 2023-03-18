package foop.assignment1.entities;

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
}
