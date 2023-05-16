package foop.a1.client.service;


import foop.a1.client.dto.GameDTO;
import foop.a1.client.dto.PositionDTO;
import foop.a1.client.main.Game;
import foop.a1.client.messages.response.*;
import foop.a1.client.states.State;
import foop.a1.client.states.menu.Menu;
import foop.a1.client.states.playing.Playing;
import foop.a1.client.states.playing.entities.Enemy;
import foop.a1.client.states.playing.entities.Player;
import foop.a1.client.states.playing.entities.Position;
import foop.a1.client.states.playing.entities.SubwayEntrance;
import foop.a1.client.states.waiting.Waiting;
import foop.a1.client.states.gameover.GameOver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ProtocolHandler {

    private Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    // Precondition response is an instance of Message
    public void handleResponse(StompHeaders headers, Object response) throws ProtocolException {
        if (response instanceof GameCreated) {
            handleResponse((GameCreated) response);
        } else if (response instanceof StatusUpdate) {
            handleResponse((StatusUpdate) response);
        } else if (response instanceof RegistrationResult) {
            handleResponse((RegistrationResult) response);
//        } else if (response instanceof AllGames) {
//            handleResponse((AllGames) response);
        } else if (response instanceof GameStarted) {
            handleResponse((GameStarted) response);
        } else if (response instanceof PositionUpdated) {
            handleResponse((PositionUpdated) response);
        } else if (response instanceof EnemiesPositionsUpdated) {
            handleResponse((EnemiesPositionsUpdated) response);
        } else if (response instanceof foop.a1.client.messages.response.GameOver) {
            handleResponse((foop.a1.client.messages.response.GameOver)response);
        } else {
            throw new ProtocolException("Response from server does not have any of the provided types");
        }

    }

    private void handleResponse(GameCreated gameCreated){
        Game clientGame = Game.instance();
        GameDTO gameDTO = gameCreated.getGame();
        if(clientGame.getState().getClass().equals(Menu.class)){
            clientGame.setGameId(gameDTO.getGameId());
            clientGame.nextState(new Waiting());
        }
    }

    private void handleResponse(StatusUpdate statusUpdate){
        Class<? extends State> stateClass = Game.instance().getState().getClass();

        if (stateClass.equals(Playing.class)) {
            Game.instance().getState().update();
        }
    }

    private void handleResponse(RegistrationResult registrationResult){
        if(registrationResult.isSuccessful()){
            LOGGER.info("Registration successful");
            var playerDto = registrationResult.getPlayer();
            var player = new Player(playerDto.getPlayerId(), new Position(playerDto.getPosition().getX(), playerDto.getPosition().getY()));
            Game.instance().setCurrentPlayer(player);
        } else {
            LOGGER.error("Registration failed");
        }
    }

//    private void handleResponse(AllGames allGames){
//        // Not supported currently because we only implement one game.
//    }

    private void handleResponse(GameStarted gameStarted){
        Playing playing = new Playing();
        playing.setPlayer(Game.instance().getCurrentPlayer());
        var players = gameStarted.getGameBoardDTO().getPlayers().stream().filter(p -> !p.getPlayerId().equals(Game.instance().getCurrentPlayer().getId()))
                        .map(playerDTO -> new Player(playerDTO.getPlayerId(), new Position(playerDTO.getPosition().getX(),playerDTO.getPosition().getY())))
                                .toList();
        playing.setPlayers(players);

        var enemies = gameStarted.getGameBoardDTO().getMice().stream().map(mouseDTO -> new Enemy(mouseDTO.getId(), new Position(mouseDTO.getPosition().getX(), mouseDTO.getPosition().getY()))).toList();
        playing.setEnemies(enemies);

        List<SubwayEntrance> entrances = new ArrayList<>();
        for (var subway : gameStarted.getGameBoardDTO().getSubways()) {
            for (var en : subway.getEntrances()) {
                SubwayEntrance entrance = new SubwayEntrance(new Position(en.getX(), en.getY()));
                if (subway.isGoalSubway()) entrance.setBelongsToGoalSubway(true);
                entrances.add(entrance);

            }
        }
        playing.setSubwayEntrances(entrances);

        Game.instance().nextState(playing);
    }

    private void handleResponse(PositionUpdated positionUpdated) {
        State currentState = Game.instance().getState();
        if (!(currentState instanceof Playing)) {
            this.LOGGER.error("Player position can be updated only in playing state");
            return;
        }
        var player = ((Playing) currentState).getPlayers().stream().filter(p -> positionUpdated.getPlayerId().equals(p.getId()))
                .findAny()
                .orElse(null);
        LOGGER.info("current position: " +((Playing) currentState).getPlayer().getPosition().getX() + " " + ((Playing) currentState).getPlayer().getPosition().getY() );
        if (player != null) {
            player.setPosition(new Position(positionUpdated.getNewPosition().getX(), positionUpdated.getNewPosition().getY()));
        } else if (!positionUpdated.getPlayerId().equals(((Playing) currentState).getPlayer().getId())) {
            this.LOGGER.error("Position updated: player " + positionUpdated.getPlayerId() + " not found");
        }
    }

    private void handleResponse(EnemiesPositionsUpdated positionsUpdated) {
        State currentState = Game.instance().getState();
        if (!(currentState instanceof Playing)) {
            this.LOGGER.error("Enemies positions can be updated only in playing state");
            return;
        }

        Map<String, PositionDTO> newPositionsById = positionsUpdated.getNewPositionsById();
        Map<String, Integer> newScores = positionsUpdated.getScores();
        List<Enemy> newEnemies = new ArrayList<>();
        int nEnemiesBefore = ((Playing) currentState).getEnemies().size();
        for (var newPosId : newPositionsById.keySet()) {
            newEnemies.add(new Enemy(newPosId, new Position(newPositionsById.get(newPosId).getX(), newPositionsById.get(newPosId).getY())));
        }
        ((Playing) currentState).setEnemies(newEnemies);
        ((Playing) currentState).setScores(newScores);
        if (newEnemies.size() < nEnemiesBefore) {
            this.LOGGER.info("Enemy caught, now there are {} enemies", newEnemies.size());
        }
    }

    private void handleResponse(foop.a1.client.messages.response.GameOver gameOver) {
        State currentState = Game.instance().getState();
        if (!(currentState instanceof Playing)) {
            this.LOGGER.error("Enemies positions can be updated only in playing state");
            return;
        }
        LOGGER.info("Game over, winner: " + gameOver.getWinner());

        String winnerName = gameOver.getWinner() == foop.a1.client.messages.response.GameOver.Team.PLAYERS ? "Cats" : "Mice";
        foop.a1.client.states.gameover.GameOver newState = new foop.a1.client.states.gameover.GameOver(winnerName);
        Game.instance().nextState(newState);
    }
}
