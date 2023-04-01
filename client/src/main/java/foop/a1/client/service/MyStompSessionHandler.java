package foop.a1.client.service;

import foop.a1.client.main.Game;
import foop.a1.client.messages.Message;
import foop.a1.client.messages.ServerMessage;
import foop.a1.client.messages.response.AllGames;
import foop.a1.client.messages.response.RegistrationResult;
import foop.a1.client.messages.response.SingleGame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Type;
import java.util.HashMap;


// Like a proxy for the Subject in the observer pattern

@Component
public class MyStompSessionHandler extends StompSessionHandlerAdapter {

    @Autowired
    private ProtocolHandler protocolHandler;

    private Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final HashMap<String, Type> destination2responseType = new HashMap<>(){{
        put("/topic/games/create", SingleGame.class);
        put("/topic/games", AllGames.class);
        put("/topic/register", RegistrationResult.class);
    }};
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        LOGGER.info("Client connected");
        super.afterConnected(session, connectedHeaders);
    }


    @Override
    public Type getPayloadType(StompHeaders headers) {
        String destination = headers.getDestination();
        LOGGER.info("Determining payload type for '{}'", destination);
        return destination2responseType.get(destination);
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        if(payload instanceof ServerMessage){
            LOGGER.info("Handling frame: \n\tPayload = {} of type {}, \n\tDestination = {}", payload, payload.getClass() , headers.getDestination());
            protocolHandler.handleResponse(headers, (ServerMessage) payload);
        }
    }
}
