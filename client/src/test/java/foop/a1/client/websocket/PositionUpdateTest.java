package foop.a1.client.websocket;

import foop.a1.client.main.Game;
import foop.a1.client.messages.request.PositionUpdate;
import foop.a1.client.messages.response.CurrentPosition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PositionUpdateTest {
    @LocalServerPort
    private int port;
    private String URL;

    @BeforeEach
    public void setup() {
        URL = "ws://localhost:" + port + "/game";
    }

    @Test
    public void testPositionUpdate() throws ExecutionException, InterruptedException, TimeoutException {
        var client = new StandardWebSocketClient();
        var stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        final CurrentPosition[] position = new CurrentPosition[1];
        var futureSession = stompClient.connectAsync(URL, new StompSessionHandlerAdapter() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return CurrentPosition.class;
            }

            @Override
            public void afterConnected(StompSession session, StompHeaders headers) {
                session.subscribe("/topic/1/update", this);
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                position[0] = (CurrentPosition) payload;
            }
        });

        var game = Game.instance();
        game.setGameId("1");

        var session = futureSession.get(1, TimeUnit.SECONDS);
        session.send("/app/1/update", new PositionUpdate());

        Thread.sleep(200);
        assertNotNull(position[0]);
    }

    @Test
    public void testPositionUpdateDifferentGame() throws ExecutionException, InterruptedException, TimeoutException {
        var client = new StandardWebSocketClient();
        var stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        final CurrentPosition[] position = new CurrentPosition[1];
        var futureSession = stompClient.connectAsync(URL, new StompSessionHandlerAdapter() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return CurrentPosition.class;
            }

            @Override
            public void afterConnected(StompSession session, StompHeaders headers) {
                session.subscribe("/topic/1/update", this);
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                position[0] = (CurrentPosition) payload;
            }
        });

        var session = futureSession.get(1, TimeUnit.SECONDS);
        session.send("/app/1/update", new PositionUpdate());

        Thread.sleep(200);
        assertNull(position[0]);
    }
}