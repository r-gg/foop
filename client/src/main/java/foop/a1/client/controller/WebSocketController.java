package foop.a1.client.controller;

import foop.a1.client.dto.PlayerDTO;
import foop.a1.client.dto.PositionDTO;
import foop.a1.client.messages.request.PositionUpdate;
import foop.a1.client.messages.response.CurrentPosition;
import foop.a1.client.service.GameService;
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
    private final GameService gameService;

    @Autowired
    public WebSocketController(Logger logger, SimpMessagingTemplate messagingTemplate, GameService gameService) {
        this.logger = logger;
        this.messagingTemplate = messagingTemplate;
        this.gameService = gameService;
    }

    @MessageMapping("/{gameId}/update")
    public void positionUpdate(@DestinationVariable String gameId, PositionUpdate positionUpdate) {
        logger.info("Game {}: Get position update", gameId);

        var gameOpt = gameService.getGame(gameId);
        if (gameOpt.isEmpty()) {
            logger.info("Game {}: Game not found", gameId);
            messagingTemplate.convertAndSend("/topic/{gameId}/update", new CurrentPosition());
            return;
        }

        var game = gameOpt.get();
        var player = game.getPlayer();

        var positionDto = new PositionDTO(player.getX(), player.getY());
        var playerDto = new PlayerDTO(player.getId(), positionDto);

        logger.info("Game {}: Current position", gameId);
        messagingTemplate.convertAndSend("/topic/{gameId}/update", new CurrentPosition(playerDto));
    }
}
