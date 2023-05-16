package foop.a1.server.entities;

import foop.a1.server.util.Constants;
import foop.a1.server.util.GameStatus;
import org.apache.tomcat.util.bcel.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;

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
        Random random = new Random();

        for (int i = 0; i < Constants.N_MICE; i++) {
            var mouse = new Mouse();
            mouse.setId(UUID.randomUUID().toString());
            int x = random.nextInt(Constants.GAMEBOARD_WIDTH);
            int y = random.nextInt(Constants.GAMEBOARD_HEIGHT);
            mouse.setPosition(new Position(x, y));
            this.mice.add(mouse);
        }
        // test mice
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
            if (mice.isEmpty()) {
                if (this.onGameOver != null) {
                    this.onGameOver.apply(Team.PLAYERS);
                    this.status = GameStatus.ENDED;
                }
                break;
            }
            // move mice
            // just test movement to test communication
            //mice.get(0).setPosition(new Position(mice.get(0).getPosition().x() + 1, mice.get(0).getPosition().y() + 10));

//            // test game over
//            if (mice.get(0).getPosition().x() > 140) {
//                if (this.onGameOver != null) {
//                    this.onGameOver.apply(Team.ENEMIES);
//                    this.status = GameStatus.ENDED;
//                }
//            }
            // if all alive mice are in goal subway, game over
            if (board.getGoalSubway().getMice().size() == mice.size() && mice.size() > 0) {
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
        List<Mouse> toRemove = new ArrayList<>();

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
                Subway closestSubway = board.getSubways().stream()
                        .map(s -> Pair.of(s, s.getEntrances()))
                        .map( pair -> Pair.of(pair.getFirst(), Arrays.stream(pair.getSecond())
                                .min(Comparator.comparingDouble(e -> e.euclideanDistance(mousePos)))
                                .orElseThrow(() -> new RuntimeException("No subway entrance found"))))
                        // here each pair (subway , position of its closest entrance)
                        .min(Comparator.comparingDouble(pair -> pair.getSecond().euclideanDistance(mousePos)))
                        .orElseThrow(() -> new RuntimeException("No subway found"))
                        .getFirst();
                Position closestSubwayEntrance = Arrays.stream(closestSubway.getEntrances())
                        .min(Comparator.comparingDouble(e -> e.euclideanDistance(mousePos)))
                        .orElseThrow(() -> new RuntimeException("No subway entrance found"));
                if(closestPlayerPos != null){
                    if(closestPlayerPos.euclideanDistance(mousePos) <= Constants.HITBOX_RADIUS ){
                        toRemove.add(mouse);
                        logger.info("Mouse {} was caught by a player, {} mice remaining", mouse.getId(), mice.size());
                        continue;
                    }
                    if(closestPlayerPos.euclideanDistance(mousePos) <= Constants.MOUSE_ALERT_RANGE){
                        // move away from the player
                        Position newPos = mousePos.moveAwayFrom(closestPlayerPos, Constants.MOUSE_SPEED);
                        mouse.setPosition(newPos);
                        checkIfAboveSubwayEntranceAndEnterIfSo(mouse);
                        continue;

                    }
                    // if closest player is between this mouse and the goal subway,
                    // move away from the player or towards the closest subway exit in the opposite direction
                    // of the closest player
                    if (mousePos.isBetweenWithinPerimeter(closestPlayerPos, closestGoalSubwayEntrancePos, Constants.BETWEEN_PERIMETER)){
                        // move away from player
                        Position newPos = mousePos.moveAwayFrom(closestPlayerPos, Constants.MOUSE_SPEED);
                        mouse.setPosition(newPos);
                        checkIfAboveSubwayEntranceAndEnterIfSo(mouse);
                        continue;
                    }
                    // if closest subway entrance is between this mouse and the goal subway,
                    // move towards the closest subway entrance
//                    if (mousePos.isBetweenWithinPerimeter(closestSubwayEntrance, closestGoalSubwayEntrancePos, Constants.BETWEEN_PERIMETER)){
//                        // move towards subway entrance
//                        Position newPos = mousePos.moveTowards(closestSubwayEntrance, Constants.MOUSE_SPEED);
//                        mouse.setPosition(newPos);
//                        checkIfAboveSubwayEntranceAndEnterIfSo(mouse);
//                        continue;
//                    }

                    // if everything is clear between the mouse and goal subway, move towards the goal subway
                    Position newPos = mousePos.moveTowards(closestGoalSubwayEntrancePos, Constants.MOUSE_SPEED);
                    mouse.setPosition(newPos);
                    checkIfAboveSubwayEntranceAndEnterIfSo(mouse);

                }else {
                    throw new RuntimeException("No player found");
                }


            } else {
                if(mouse.getCurrentSubway().equals(board.getGoalSubway())){
                    // if mouse is in the goal subway, don't do anything
                    continue;
                }
                // check the remaining delay
                if(mouse.getRemainingDelay() == 0){
                    // build pairs of subways and their entrances with the distance to each goal subway entrance
                    Position selectedExit = Arrays.stream(mouse.getCurrentSubway().getEntrances())
                            .flatMap(exit -> Arrays.stream(board.getGoalSubway().getEntrances()).map(goalEntrance -> Pair.of(exit, goalEntrance)))
                            .min(Comparator.comparingDouble(pair -> pair.getFirst().euclideanDistance(pair.getSecond())))
                            .orElseThrow(() -> new RuntimeException("No subway exit found")).getFirst();
                    mouse.exitSubway();

                    // generate a random angle and move the mouse towards a position on a circle around the selected exit
                    // defined by the random angle
                    // The mouse moves exactly three steps from the exit "boundary"
                    Random random = new Random();
                    double randAngle = random.nextDouble()*2*Math.PI;
                    double randomX = Math.cos(randAngle) * ( Constants.SUBWAY_ENTRANCE_RADIUS + 3*Constants.MOUSE_SPEED ) ;
                    double randomY = Math.sin(randAngle)* (Constants.SUBWAY_ENTRANCE_RADIUS + 3*Constants.MOUSE_SPEED);
                    Position randomPos = new Position((int) randomX, (int) randomY);


                    Position newPos = selectedExit.add(randomPos); // add offset to the selected exit
                    mouse.setPosition(newPos);
                }else if (mouse.getRemainingDelay() > 0){
                    mouse.decreaseRemainingDelay();
                }else {
                    throw new RuntimeException("Mouse remaining delay is negative even when under ground");
                }
            }
        }
        mice.removeAll(toRemove);
    }

    /**
     * Checks if there is a subway below the mouse's new position and enters it if so.
     * @param mouse
     */
    private void checkIfAboveSubwayEntranceAndEnterIfSo(Mouse mouse){
        // finding first subway that has one of the entrances below the mouse's new position
        Position mousePos = mouse.getPosition();
        Optional<Subway> subwayBelow = board.getSubways().stream()
                .filter(s -> Arrays.stream(s.getEntrances())
                        .anyMatch(e -> e.euclideanDistance(mousePos) <= Constants.SUBWAY_ENTRANCE_RADIUS))
                .findFirst();
        // if such subway exists, then enter it:
        if(subwayBelow.isPresent()){
            Subway subway = subwayBelow.get();
            mouse.enterSubway(subway);
            List<Position> catLocations = players.stream()
                    .map(Player::getPosition)
                    .collect(Collectors.toList());
            subway.addMouse(mouse, catLocations);
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
        if(mice.size() < 4){
            logger.info("Mice size < 4");
        }
        for (var mouse : mice) {
            // return only above ground to avoid cheating on client side
            if (mouse.isAboveGround()) {
                result.put(mouse.getId(), mouse.getPosition());
            }
        }
        return result;
    }
}
