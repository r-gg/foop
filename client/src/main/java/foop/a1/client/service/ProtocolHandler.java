package foop.a1.client.service;


import foop.a1.client.dto.GameDTO;
import foop.a1.client.main.Game;
import foop.a1.client.messages.Message;
import foop.a1.client.messages.response.*;
import foop.a1.client.states.State;
import foop.a1.client.states.menu.Menu;
import foop.a1.client.states.playing.Playing;
import foop.a1.client.states.playing.entities.Player;
import foop.a1.client.states.waiting.Waiting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.net.ProtocolException;

@Component
public class ProtocolHandler {

    private Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    // Precondition response is an instance of Message
    public void handleResponse(StompHeaders headers, Object response) throws ProtocolException {
        if (response instanceof SingleGame) {
            handleResponse((SingleGame) response);
        } else if (response instanceof StatusUpdate) {
            handleResponse((StatusUpdate) response);
        } else if (response instanceof RegistrationResult) {
            handleResponse((RegistrationResult) response);
        } else if (response instanceof AllGames) {
            handleResponse((AllGames) response);
        } else if (response instanceof StartGame) {
            handleResponse((StartGame) response);
        } else {
            throw new ProtocolException("Response from server does not have any of the provided types");
        }

    }

    private void handleResponse(SingleGame singleGame){
        Game clientGame = Game.instance();
        GameDTO gameDTO = singleGame.getGame();
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
            LOGGER.info("Registration successfull");
        }else {
            LOGGER.error("Registration failed");
        }

    }

    private void handleResponse(AllGames allGames){
        // Not supported currently because we only implement one game.
    }

    private void handleResponse(StartGame startGame){
        Playing playing = new Playing();
        var players = startGame.getGameBoardDTO().getPlayers().stream()
                        .map(playerDTO -> new Player(playerDTO.getPlayerId(), playerDTO.getPosition().getX(),playerDTO.getPosition().getY()))
                                .toList();

        // TODO: convert mice, subways etc and set game state

        playing.setPlayers(players);


        Game.instance().nextState(new Playing());

    }
}
