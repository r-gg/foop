package foop.a1.server.entities;



import java.util.ArrayList;
import java.util.List;

public class Subway {

    private Position[] entrances;

    private int delay; // number of game loop iterations a mouse needs to go from one entrance to any other

    // TODO: Transform into Mouse
    private List<ServerMouse> mice;

    public Subway(List<Position> entrances) {
        Position[] arr = new Position[entrances.size()];
        this.entrances = entrances.toArray(arr);
        this.mice = new ArrayList<>();
    }

    public Position[] getEntrances() {
        return entrances;
    }

    public List<ServerMouse> getMice() {
        return mice;
    }

    public void addMouse(ServerMouse mouse){
        if(mouse != null) mice.add(mouse);
    }

    // TODO: Check if runs on server, then cast?
    public void informMice(Game game){
        for (ServerMouse mouse: mice) {
            mouse.inform(game);
        }
    }
}
