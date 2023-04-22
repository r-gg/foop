package foop.a1.server.entities;

import foop.a1.server.util.Constants;
import foop.a1.server.util.GameStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Game implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final String gameId;
    private final List<Player> players = new ArrayList<>();
    private final List<Mouse> mice = new ArrayList<>();
    private GameBoard board;
    private GameStatus status;

    public Game() {
        gameId = UUID.randomUUID().toString();
        status = GameStatus.WAITING;

        // test mice
        var mouse1 = new Mouse();
        mouse1.setPosition(new Position(50, 300));
        this.mice.add(mouse1);
        var mouse2 = new Mouse();
        mouse2.setPosition(new Position(10, 100));
        this.mice.add(mouse2);
        var mouse3 = new Mouse();
        mouse3.setPosition(new Position(20, 250));
        this.mice.add(mouse3);
        var mouse4 = new Mouse();
        mouse4.setPosition(new Position(170, 220));
        this.mice.add(mouse4);
    }

    public void addPlayer(Player player) {
        if(!status.equals(GameStatus.WAITING)){
            logger.info("Game is already running");
            return;
        }

        players.add(player);
        logger.info("Added player: {}", player.getPlayerId());

        //TODO if players size >= 4, status to ALL_READY and start
    }

    public void removePlayer(Player player){
        players.remove(player);
    }

    public void start() {
        this.status = GameStatus.STARTED;

        Thread t = new Thread(this);
        t.start();
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

        this.status = GameStatus.ENDED;
        logger.info("Finished game {}", this.gameId);
    }

    public String getGameId() {
        return gameId;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Mouse> getMice() {
        return mice;
    }

    public GameBoard getBoard() {
        return board;
    }

    public void setBoard(GameBoard board) {
        this.board = board;
    }

    public GameStatus getStatus(){
        return this.status;
    }

    public List<Position> getCatLocations() {
        return players.stream().map(Player::getPosition).collect(Collectors.toList());
    }
}
