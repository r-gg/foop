package foop.assignment1.entities;

import java.util.ArrayList;
import java.util.Collection;

public class GameBoard {
    private Collection<Locatable> locatable;

    public GameBoard() {
        this.locatable = new ArrayList<>();
    }

    public Collection<Locatable> getLocatable() {
        return locatable;
    }

    public void setLocatable(Collection<Locatable> locatable) {
        this.locatable = locatable;
    }
}
