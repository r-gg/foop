package foop.assignment1.entities;

import java.util.ArrayList;
import java.util.List;

public class Subway {

    private Position[] entrances;

    private int delay; // number of game loop iterations a mouse needs to go from one entrance to any other

    private List<Mouse> mice;

    public Subway(List<Position> entrances) {
        Position[] arr = new Position[entrances.size()];
        this.entrances = entrances.toArray(arr);
        this.mice = new ArrayList<>();
    }

    public Position[] getEntrances() {
        return entrances;
    }

    public List<Mouse> getMice() {
        return mice;
    }

    public void addMouse(Mouse mouse){
        if(mouse != null) mice.add(mouse);
    }

    public void informMice(Game game){

        for (Mouse mouse: mice) {
            mouse.inform(game);
        }
    }
}
