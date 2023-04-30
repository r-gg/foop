package foop.a1.server.websocket;

import foop.a1.server.dto.GameDTO;
import foop.a1.server.messages.request.GetGames;
import foop.a1.server.messages.response.AllGames;
import foop.a1.server.messages.request.CreateGame;
import foop.a1.server.messages.response.GameCreated;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class GetGamesTest {
    @LocalServerPort
    private final int port = 8082;
    private String URL;

    @BeforeEach
    public void setup(){
        URL = "ws://localhost:" + port + "/game";
    }

    @Test
    public void testGetGames() throws ExecutionException, InterruptedException, TimeoutException {
        createGames(1);

        var client = new StandardWebSocketClient();
        var stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        List<GameDTO> games = new ArrayList<>();
        var futureSession = stompClient.connectAsync(URL, new StompSessionHandlerAdapter() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return AllGames.class;
            }

            @Override
            public void afterConnected(StompSession session, StompHeaders headers) {
                session.subscribe("/topic/games", this);
            }
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                var allGames = (AllGames) payload;
                games.addAll(allGames.getGames());
            }
        });

        var getGames = new GetGames();
        var session = futureSession.get(1, TimeUnit.SECONDS);
        session.send("/app/games", getGames);

        Thread.sleep(100);
        assertEquals(1, games.size());
    }

    @Test
    public void testGetGamesMultipleGames() throws ExecutionException, InterruptedException, TimeoutException {
        createGames(3);

        var client = new StandardWebSocketClient();
        var stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        List<GameDTO> games = new ArrayList<>();
        var futureSession = stompClient.connectAsync(URL, new StompSessionHandlerAdapter() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return AllGames.class;
            }

            @Override
            public void afterConnected(StompSession session, StompHeaders headers) {
                session.subscribe("/topic/games", this);
            }
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                var allGames = (AllGames) payload;
                games.addAll(allGames.getGames());
            }
        });

        var getGames = new GetGames();
        var session = futureSession.get(1, TimeUnit.SECONDS);
        session.send("/app/games", getGames);

        Thread.sleep(100);
        assertEquals(3, games.size());
    }

    private void createGames(int amount) throws ExecutionException, InterruptedException, TimeoutException {
        var client = new StandardWebSocketClient();
        var stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        var futureSession = stompClient.connectAsync(URL, new StompSessionHandlerAdapter() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return GameCreated.class;
            }
        });

        var createGame = new CreateGame();

        var session = futureSession.get(1, TimeUnit.SECONDS);

        for(int i = 0; i < amount; i += 1) {
            session.send("/app/games/create", createGame);
            Thread.sleep(100);
        }
    }
}
