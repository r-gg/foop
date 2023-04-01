package foop.a1.client.service;

import foop.a1.client.main.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class WebsocketService {

    private Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    private StandardWebSocketClient client;
    private WebSocketStompClient stompClient;

    private MyStompSessionHandler sessionHandler;


    @Value("${websocket.server.port}")
    private String websocketServerPort; // injected after the constructor
    private String URL;

    private StompSession session;


    public WebsocketService(MyStompSessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
        URL = "ws://localhost:" + 8081 + "/game";
        client = new StandardWebSocketClient();
        stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        connect();
        subscribe("/topic/games/create");
        subscribe("/topic/games");
        Game.setWebsocketService(this);
    }



    public void connect(int port){
        if(port != -1){ // TODO: remove after moving to Client
            URL = "ws://localhost:" + port + "/game";
        }
        CompletableFuture<StompSession> futureSession = stompClient.connectAsync(URL, sessionHandler);
        StompSession session = null;
        try {
            session = futureSession.get(2, TimeUnit.SECONDS);
            this.session = session;
        } catch (InterruptedException e) {
            LOGGER.error("Interrupted the client connect");
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            LOGGER.error("Execution exception in client connect");
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            LOGGER.error("Timeout happened in client connect. " +
                    "The server is either unreachable or a wrong port is selected.");
            throw new RuntimeException(e);
        }

    }

    public void connect(){
        connect(-1);
    }

    public void send(String destination, Object payload){
        LOGGER.info("Sending {} to {}", payload, destination);
        session.send(destination, payload);
    }

    public StompSession.Subscription subscribe(String destination){
        LOGGER.info("Subscribing to {}",destination);
        return session.subscribe(destination, sessionHandler);
    }


}
