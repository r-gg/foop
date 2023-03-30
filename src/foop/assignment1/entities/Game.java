package foop.assignment1.entities;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Game {

    String id;

    GameBoard board;

    List<Player> players;

    boolean isActive = false;

    public Game() {
        id  = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GameBoard getBoard() {
        return board;
    }

    public void setBoard(GameBoard board) {
        this.board = board;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void addPlayer(Player player){
        if(player == null){
            return;
        }
        if(players == null){
            players = new ArrayList<>();
        }
        players.add(player);
    }

    public void start(){
        isActive = true;
    }


}
