package foop.a1.client.websocket;

import foop.a1.client.main.Game;
import foop.a1.client.messages.request.CreateGame;
import foop.a1.client.service.WebsocketService;
import static org.junit.jupiter.api.Assertions.*;

import foop.a1.client.states.waiting.Waiting;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.stomp.StompSession;

import java.lang.invoke.MethodHandles;

@SpringBootTest
public class ProtocolTests {

    @Autowired
    private WebsocketService websocketService;

    private Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @BeforeAll
    public static void notification(){
        System.out.println("  ############################################################################################");
        System.out.println("  >>>>>>>>> ========  The tests assume that server is running on port 8081  ========== <<<<<<<");
        System.out.println("  ############################################################################################");

    }

    @Test
    public void createGame_createsGame() throws InterruptedException {
        StompSession.Subscription subscription = websocketService.subscribe("/topic/games/create");
        websocketService.send("/app/games/create", new CreateGame());
        Thread.sleep(1000);
        assertFalse(Game.instance().getGameId().isEmpty());
        assertEquals(Game.instance().getState().getClass(), Waiting.class);
    }



}
