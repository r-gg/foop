package foop.a1.server.entities;

import foop.a1.server.util.Constants;
import foop.a1.server.util.GameStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Game implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final String gameId;
    private final List<Player> players = new ArrayList<>();
    private final List<Mouse> mice = new ArrayList<>();
    private GameBoard board;
    private volatile GameStatus status;
    private Function<Map<String, Position>, Void> onMicePositionsUpdate = null;
    private Function<Team, Void> onGameOver = null;

    public enum Team {
        PLAYERS,
        ENEMIES
    }

    public Game() {
        gameId = UUID.randomUUID().toString();
        status = GameStatus.WAITING;

        // test mice
        var mouse1 = new Mouse();
        mouse1.setId(UUID.randomUUID().toString());
        mouse1.setPosition(new Position(50, 300));
        this.mice.add(mouse1);
        var mouse2 = new Mouse();
        mouse2.setId(UUID.randomUUID().toString());
        mouse2.setPosition(new Position(10, 100));
        this.mice.add(mouse2);
        var mouse3 = new Mouse();
        mouse3.setId(UUID.randomUUID().toString());
        mouse3.setPosition(new Position(20, 250));
        this.mice.add(mouse3);
        var mouse4 = new Mouse();
        mouse4.setId(UUID.randomUUID().toString());
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
            // move mice
            // just test movement to test communication
            mice.get(0).setPosition(new Position(mice.get(0).getPosition().x() + 10, mice.get(0).getPosition().y() + 10));

            // test game over
            if (mice.get(0).getPosition().x() > 140) {
                if (this.onGameOver != null) {
                    this.onGameOver.apply(Team.ENEMIES);
                    this.status = GameStatus.ENDED;
                }
            }
            if (mice.isEmpty()) {
                if (this.onGameOver != null) {
                    this.onGameOver.apply(Team.PLAYERS);
                    this.status = GameStatus.ENDED;
                }
            }
            if (board.getGoalSubway().getMice().size() == mice.size()) {
                if (this.onGameOver != null) {
                    this.onGameOver.apply(Team.ENEMIES);
                    this.status = GameStatus.ENDED;
                }
            }

            // send updated locations back
            if (this.onMicePositionsUpdate != null) {
                this.moveMice();
                this.onMicePositionsUpdate.apply(getMicePositions());
            }

            if (this.status == GameStatus.ENDED) {
                break;
            }

            try {
                Thread.sleep(Duration.ofMillis(Constants.GAME_LOOP_DELAY_MILLIS));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
//            if(System.currentTimeMillis() - startMillis >= Constants.GAME_TIMEOUT_MILLIS){
//                break;
//            }
        }

//        this.status = GameStatus.ENDED;
        logger.info("Finished game {}", this.gameId);
    }


    /**
     * Implements a naive movement strategy for the mice. Based on the current position of the mice, the players
     * and the subways.
     */
    private void moveMice(){
        for(var mouse : mice){
            if(mouse.isAboveGround()){
                // if mouse is above ground, move towards the goal subway
                // if there is a player in the way, move towards the nearest subway exit
                // if there is no player in the way, but there is another subway between the mouse and the goal subway,
                // such that one of the remaining exits of that subway is closer to the goal subway,
                // move into that subway
                Position mousePos = mouse.getPosition();
                Position closestGoalSubwayEntrancePos = Arrays.stream(board.getGoalSubway().getEntrances())
                        .min(Comparator.comparingDouble(e -> e.euclideanDistance(mousePos)))
                        .orElseThrow(() -> new RuntimeException("No goal subway entrance found"));
                Position closestPlayerPos = players.stream()
                        .map(Player::getPosition)
                        .min(Comparator.comparingDouble(p -> p.euclideanDistance(mousePos)))
                        .orElse(null);
                if(closestPlayerPos != null){
                    if(closestPlayerPos.euclideanDistance(mousePos) <= Constants.HITBOX_RADIUS ){
                        mice.remove(mouse);
                        logger.info("Mouse {} was caught by a player, {} mice remaining", mouse.getId(), mice.size());
                        continue;
                    }
                }


            } else {
                // if mouse is in subway, move towards the subway exit closest to the goal subway

            }
        }
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

    public void setOnMicePositionsUpdate(Function<Map<String, Position>, Void> onMicePositionsUpdate) {
        this.onMicePositionsUpdate = onMicePositionsUpdate;
    }

    public void setOnGameOver(Function<Team, Void> onGameOver) {
        this.onGameOver = onGameOver;
    }

    public Map<String, Position> getMicePositions() {
        var result = new HashMap<String, Position>();
        for (var mouse : mice) {
            // return only above ground to avoid cheating on client side
            if (mouse.isAboveGround()) {
                result.put(mouse.getId(), mouse.getPosition());
            }
        }
        return result;
    }
}
