package foop.a1.server.entities;


import java.util.ArrayList;
import java.util.List;

public class Subway {
    private final Position[] entrances;
    private final List<Mouse> mice;
    private int delay;

    private boolean isGoalSubway = false;

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

    public void addMouse(Mouse mouse) {
        mice.add(mouse);
    }

    public void informMice(Game game) {
        for (Mouse mouse : mice) {
            mouse.inform(game);
        }
    }

    public void removeMouse(Mouse mouse) {
        mice.remove(mouse);
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getDelay() {
        return delay;
    }

    public void setGoalSubway(boolean isGoalSubway) {
        this.isGoalSubway = isGoalSubway;
    }

    public boolean isGoalSubway() {
        return isGoalSubway;
    }
}
