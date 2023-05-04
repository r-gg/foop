package foop.a1.server.entities;

import java.util.List;

public class Mouse {
    private String id;
    private Position position;

    private boolean isAboveGround = true;

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
        return isAboveGround;
    }

    public void setAboveGround(boolean aboveGround) {
        isAboveGround = aboveGround;
    }

    public void inform(Game game) {
        List<Position> catLocations = game.getCatLocations();
    }

}
