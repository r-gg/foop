package foop.a1.client.service;

import foop.a1.client.main.Game;
import foop.a1.client.messages.request.CreateGame;
import foop.a1.client.messages.request.RegisterForGame;
import foop.a1.client.messages.request.StartGame;
import foop.a1.client.messages.request.UpdatePosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
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
        var scheduler = new ThreadPoolTaskScheduler();
        scheduler.initialize();
        stompClient.setTaskScheduler(scheduler);
        connect(URL);
        System.setProperty("java.awt.headless", "false");
        Game.instance();
        Game.instance().setWebsocketService(this);
    }

    public void connect(String url){
        CompletableFuture<StompSession> futureSession = stompClient.connectAsync(url, sessionHandler);
        StompSession session = null;
        try {
            session = futureSession.get(2, TimeUnit.SECONDS);
            this.session = session;

            this.session.setAutoReceipt(true);
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

    public void send(String destination, Object payload){
        LOGGER.info("Sending {} to {}", payload, destination);
        session.send(destination, payload);
    }

    public StompSession.Subscription subscribe(String destination){
        LOGGER.info("Subscribing to {}", destination);
        StompHeaders headers = new StompHeaders();
        headers.setDestination(destination);
        return session.subscribe(headers, sessionHandler);
    }

    public void sendGameCreate(CreateGame createGame) {
        this.send("/app/games/create", createGame);
    }

    public void sendGameStart(StartGame startGame) {
        this.send("/app/games/start", startGame);
    }

    public void sendRegisterForGame(String gameId, RegisterForGame registerForGame) {
        this.send("/app/" + gameId + "/register", registerForGame);
    }

    public void sendUpdatePosition(UpdatePosition updatePosition) {
        this.send("/app/games/update-position", updatePosition);
    }
}
