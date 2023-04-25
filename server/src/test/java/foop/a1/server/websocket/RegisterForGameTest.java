package foop.a1.server.websocket;

import foop.a1.server.messages.request.CreateGame;
import foop.a1.server.messages.request.RegisterForGame;
import foop.a1.server.messages.response.RegistrationResult;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RegisterForGameTest {
    @LocalServerPort
    private int port = 8082;
    private String URL;

    @BeforeEach
    public void setup() {
        URL = "ws://localhost:" + port + "/game";
    }

    @Test
    public void testRegisterForGameSuccessful() throws ExecutionException, InterruptedException, TimeoutException {
        var gameId = createGame();
        String username = "player1";

        var client = new StandardWebSocketClient();
        var stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        final RegistrationResult[] registrationResult = new RegistrationResult[1];
        var futureSession = stompClient.connectAsync(URL, new StompSessionHandlerAdapter() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return RegistrationResult.class;
            }

            @Override
            public void afterConnected(StompSession session, StompHeaders headers) {
                System.out.println("Connected for register");
                session.subscribe("/user/"+username+"/queue/"+gameId+"/register", this);
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                registrationResult[0] = (RegistrationResult) payload;
            }
        });

        var registerForGame = new RegisterForGame();
        registerForGame.setUsername("player1");
        var session = futureSession.get(1, TimeUnit.SECONDS);
        session.send(String.format("/app/%s/register", gameId), registerForGame);

        Thread.sleep(1000);
        assertNotNull(registrationResult[0]);
        assertTrue(registrationResult[0].isSuccessful());
        assertNotNull(registrationResult[0].getPlayer());
        assertNotNull(registrationResult[0].getGame());
    }

    @Test
    public void testRegisterForGameNotSuccessful() throws ExecutionException, InterruptedException, TimeoutException {
        var gameId = "12341234";
        String username = "player1";

        var client = new StandardWebSocketClient();
        var stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        final RegistrationResult[] registrationResult = new RegistrationResult[1];
        var futureSession = stompClient.connectAsync(URL, new StompSessionHandlerAdapter() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return RegistrationResult.class;
            }

            @Override
            public void afterConnected(StompSession session, StompHeaders headers) {
                session.subscribe("/user/"+username+"/queue/"+gameId+"/register", this);
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                registrationResult[0] = (RegistrationResult) payload;
            }
        });

        var registerForGame = new RegisterForGame();
        registerForGame.setUsername(username);
        var session = futureSession.get(1, TimeUnit.SECONDS);
        session.send(String.format("/app/%s/register", gameId), registerForGame);

        Thread.sleep(100);
        assertNotNull(registrationResult[0]);
        assertFalse(registrationResult[0].isSuccessful());
    }

    private String createGame() throws ExecutionException, InterruptedException, TimeoutException {
        var client = new StandardWebSocketClient();
        var stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        final String[] gameId = new String[1];
        var futureSession = stompClient.connectAsync(URL, new StompSessionHandlerAdapter() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return GameCreated.class;
            }

            @Override
            public void afterConnected(StompSession session, StompHeaders headers) {
                session.subscribe("/topic/games/create", this);
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                var game = (GameCreated) payload;
                gameId[0] = game.getGame().getGameId();
            }
        });

        var createGame = new CreateGame();
        var session = futureSession.get(1, TimeUnit.SECONDS);
        session.send("/app/games/create", createGame);
        Thread.sleep(100);

        return gameId[0];
    }
}
