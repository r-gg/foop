package foop.a1.client.controller;

import foop.a1.client.dto.PlayerDTO;
import foop.a1.client.dto.PositionDTO;
import foop.a1.client.main.Game;
import foop.a1.client.messages.request.PositionUpdate;
import foop.a1.client.messages.request.StatusUpdate;
import foop.a1.client.messages.response.CurrentPosition;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    private final Logger logger;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketController(Logger logger, SimpMessagingTemplate messagingTemplate) {
        this.logger = logger;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/{gameId}/update")
    public void positionUpdate(@DestinationVariable String gameId, PositionUpdate positionUpdate) {
        logger.info("Game {}: Get position update", gameId);

        var game = Game.instance();
        if (!game.getGameId().equals(gameId)) {
            logger.info("No update needed");
            return;
        }

        var state = game.getState();
        //TODO return player if in correct state

        logger.info("Game {}: Current position", gameId);
        messagingTemplate.convertAndSend(String.format("/topic/%s/update", gameId), new CurrentPosition(new PlayerDTO()));
    }

    @MessageMapping("/{gameId}/status")
    public void gameStatusUpdate(@DestinationVariable String gameId, StatusUpdate statusUpdate) {
        logger.info("Game {}: Get status update", gameId);

        var game = Game.instance();
        if (!game.getGameId().equals(gameId)) {
            logger.info("No update needed");
            return;
        }
        //TODO update game
    }
}
