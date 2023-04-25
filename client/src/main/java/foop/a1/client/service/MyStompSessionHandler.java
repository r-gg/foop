package foop.a1.client.service;

import foop.a1.client.messages.Message;
import foop.a1.client.messages.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Type;
import java.net.ProtocolException;
import java.util.HashMap;


// Like a proxy for the Subject in the observer pattern

@Component
public class MyStompSessionHandler extends StompSessionHandlerAdapter {

    @Autowired
    private ProtocolHandler protocolHandler;

    private Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final HashMap<String, Type> destination2responseType = new HashMap<>(){{
        put("/topic/games/create", GameCreated.class);
        put("/topic/games", AllGames.class);
        put("/user/queue/register", RegistrationResult.class);
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
        if(destination.endsWith("/start")){
            return GameStarted.class;
        } else if (destination.endsWith("/position-updated")) {
            return PositionUpdated.class;
        } else if (destination.endsWith("/enemies-positions-updated")) {
            return EnemiesPositionsUpdated.class;
        }
        return destination2responseType.get(destination);
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        if(payload instanceof Message){
            LOGGER.info("Handling frame: \n\tPayload = {} of type {}, \n\tDestination = {}", payload, payload.getClass() , headers.getDestination());
            try {
                protocolHandler.handleResponse(headers, payload);
            } catch (ProtocolException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        LOGGER.error(exception.toString());
        super.handleException(session, command, headers, payload, exception);
    }
}
