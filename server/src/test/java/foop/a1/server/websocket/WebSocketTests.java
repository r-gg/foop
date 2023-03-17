package foop.a1.server.websocket;

import foop.a1.server.models.MessageModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Type;
import java.time.Duration;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class WebSocketTests {
    private Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @LocalServerPort
    private int port = 8081;

    private String URL = "";

    @BeforeEach
    public void setUp(){
        URL = "ws://localhost:"+port+"/game";

    }

    @Test
    public void testConnection() throws InterruptedException, ExecutionException, TimeoutException {
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);

        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSessionHandler sessionHandler = new MyStompSessionHandler();

        Future<StompSession> fSession = stompClient.connectAsync(URL, sessionHandler);
        StompSession session = fSession.get(2, TimeUnit.SECONDS);

        final boolean[] received = {false};
        session.subscribe("/topic", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                LOGGER.info("Got headers {} \n and payload {}", headers, payload);
                received[0] = true;
            }
        });

        Thread.sleep(Duration.ofSeconds(2));

        session.send("/app/update", new MessageModel("to", "from"));

        Thread.sleep(Duration.ofSeconds(2));
        session.disconnect();
        assertTrue(received[0]);
    }

    private class MyStompSessionHandler implements StompSessionHandler {
        @Override
        public void afterConnected(
                StompSession session, StompHeaders connectedHeaders) {
            LOGGER.info("Connected");

        }

        @Override
        public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        }

        @Override
        public void handleTransportError(StompSession session, Throwable exception) {

        }

        @Override
        public Type getPayloadType(StompHeaders headers) {
            return String.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            LOGGER.info("Got headers {} \n and payload {}", headers, payload);

        }
    }
}
