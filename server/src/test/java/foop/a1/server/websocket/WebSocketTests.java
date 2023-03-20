package foop.a1.server.websocket;
import foop.a1.server.entities.Player;
import foop.a1.server.messages.RegisterForGame;
import foop.a1.server.messages.RegistrationSuccessful;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class WebSocketTests {
    @Autowired
    private Logger logger;
    @LocalServerPort
    private int port = 8081;
    private String URL = "";
    private Player player;

    @BeforeEach
    public void setUp() {
        URL = "ws://localhost:" + port + "/game";


        player = new Player();
        player.setPlayerId(UUID.randomUUID().toString());
    }

    @Test
    public void testRegisterForGame() throws InterruptedException, ExecutionException, TimeoutException {
        var client = new StandardWebSocketClient();
        var stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        final boolean[] success = {false};
        var futureSession = stompClient.connectAsync(URL, new StompSessionHandlerAdapter() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return RegistrationSuccessful.class;
            }

            @Override
            public void afterConnected(StompSession session, StompHeaders headers) {
                session.subscribe("/topic/register", this);
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                logger.info("Client received: payload {}, headers {}", payload, headers);
                success[0] = true;
            }
        });

        var registerForGame = new RegisterForGame();
        registerForGame.setPlayer(player);

        var session = futureSession.get(1, TimeUnit.SECONDS);
        session.send("/app/register", registerForGame);

        Thread.sleep(Duration.ofSeconds(1));
        session.disconnect();
        assertTrue(success[0]);
    }
}
