package foop.a1.server.entities;

import foop.a1.server.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Game implements Runnable {

    private Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    String id;

    GameBoard board;

    List<Player> players;

    List<Mouse> mice;

    boolean isActive = false;

    public Game() {
        id = UUID.randomUUID().toString();
    }

    public List<Mouse> getMice() {
        return mice;
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

    public void addPlayer(Player player) {
        if (player == null) {
            logger.debug("Player was null when adding");
            return;
        }
        if (players == null) {
            players = new ArrayList<>();
        }
        players.add(player);
        logger.info("Added player: {}", player.getPlayerId());
    }

    public void start() {
        isActive = true;
        Thread t = new Thread(this);
        t.start();
    }

    public List<Position> getCatLocations() {
        return players.stream().map(Player::getPosition).collect(Collectors.toList());
    }


    @Override
    public void run() {
        long startMillis = System.currentTimeMillis();
        while (true) {
            // send request

            // wait for responses (with timeout)

            // update locations

            // move mice

            // send updated locations back -- redraw


            try {
                Thread.sleep(Duration.ofMillis(Constants.GAME_LOOP_DELAY_MILLIS));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(System.currentTimeMillis() - startMillis >= Constants.GAME_TIMEOUT_MILLIS){
                break;
            }
        }
        logger.info("Finished game {}", this.id);

    }
}
