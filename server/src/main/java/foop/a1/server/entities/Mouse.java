package foop.a1.server.entities;

import java.util.List;

public class Mouse implements Runnable {
    private Position position;

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return this.position;
    }

    public boolean isAboveGround() {
        return false;
    }

    public void inform(Game game) {
        List<Position> catLocations = game.getCatLocations();
    }

    @Override
    public void run() {
        // run, eat cheese
        // squeak
    }
}
