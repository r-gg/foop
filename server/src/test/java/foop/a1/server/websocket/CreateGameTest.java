package foop.a1.server.websocket;

import foop.a1.server.messages.request.CreateGame;
import foop.a1.server.messages.response.SingleGame;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CreateGameTest {
    @LocalServerPort
    private final int port = 8081;
    private String URL;

    @BeforeEach
    public void setup() {
        URL = "ws://localhost:" + port + "/game";
    }

    @Test
    public void testCreateGame() throws ExecutionException, InterruptedException, TimeoutException {
        var client = new StandardWebSocketClient();
        var stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        final String[] gameId = new String[1];
        var futureSession = stompClient.connectAsync(URL, new StompSessionHandlerAdapter() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return SingleGame.class;
            }

            @Override
            public void afterConnected(StompSession session, StompHeaders headers) {
                session.subscribe("/topic/games/create", this);
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                var game = (SingleGame) payload;
                gameId[0] = game.getGame().getGameId();
            }
        });

        var createGame = new CreateGame();

        var session = futureSession.get(1, TimeUnit.SECONDS);
        session.send("/app/games/create", createGame);

        Thread.sleep(100);
        assertNotNull(gameId[0]);
    }
}
