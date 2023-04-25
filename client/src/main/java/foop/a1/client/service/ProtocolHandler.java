package foop.a1.client.service;


import foop.a1.client.dto.GameDTO;
import foop.a1.client.main.Game;
import foop.a1.client.messages.response.*;
import foop.a1.client.states.State;
import foop.a1.client.states.menu.Menu;
import foop.a1.client.states.playing.Playing;
import foop.a1.client.states.playing.entities.Enemy;
import foop.a1.client.states.playing.entities.Player;
import foop.a1.client.states.playing.entities.SubwayEntrance;
import foop.a1.client.states.waiting.Waiting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;

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
            var player = new Player(playerDto.getPlayerId(), playerDto.getPosition().getX(), playerDto.getPosition().getY());
            Game.instance().setCurrentPlayer(player);
        }else {
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
                        .map(playerDTO -> new Player(playerDTO.getPlayerId(), playerDTO.getPosition().getX(),playerDTO.getPosition().getY()))
                                .toList();
        playing.setPlayers(players);

        // TODO: behavior type
        var enemies = gameStarted.getGameBoardDTO().getMice().stream().map(mouseDTO -> new Enemy(mouseDTO.getPosition().getX(), mouseDTO.getPosition().getY(), 0)).toList();
        playing.setEnemies(enemies);

        List<SubwayEntrance> entrances = new ArrayList<>();
        for (var subway : gameStarted.getGameBoardDTO().getSubways()) {
            for (var en : subway.getEntrances()) {
                entrances.add(new SubwayEntrance(en.getX(), en.getY()));
            }
        }
        playing.setSubwayEntrances(entrances);

        Game.instance().nextState(playing);
    }

    private void handleResponse(PositionUpdated positionUpdated){
        // TODO
    }
}
