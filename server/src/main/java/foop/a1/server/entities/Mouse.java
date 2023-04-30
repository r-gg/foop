package foop.a1.server.entities;

import java.util.List;

public class Mouse implements Runnable {
    private String id;
    private Position position;

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return this.position;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isAboveGround() {
        return true; // TODO
    }

    public void inform(Game game) {
        List<Position> catLocations = game.getCatLocations();
    }

    @Override
    public void run() { // is thread per mouse really needed? Is it not enough just to update on interval?
        // run, eat cheese
        // squeak
    }
}
