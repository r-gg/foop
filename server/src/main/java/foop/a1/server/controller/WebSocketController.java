package foop.a1.server.controller;

import foop.a1.server.messages.CurrentPosition;
import foop.a1.server.messages.PositionUpdate;
import foop.a1.server.messages.RegisterForGame;
import foop.a1.server.messages.RegistrationSuccessful;
import foop.a1.server.service.GameService;
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

    @MessageMapping("/register")
    public void registerForGame(RegisterForGame registerForGame) {
        logger.info("Player {} trying to register", registerForGame.getPlayer().getPlayerId());
        gameService.registerPlayer(registerForGame.getPlayer());

        RegistrationSuccessful registration = new RegistrationSuccessful();
        // send back game-id
        registration.setPlayer(registerForGame.getPlayer());
        messagingTemplate.convertAndSend("/topic/register", registration);
    }


    @MessageMapping("/{gameId}/update")
    public void positionUpdate(@DestinationVariable String gameId, CurrentPosition currentPosition) {

        //TODO send RequestPositionUpdate to client /{game-id}/update and subscribe to /topic/{game-id}/update receiving CurrentPosition
    }

    public void redrawGameBoard() {
        //TODO send RedrawGameBoard to client /redraw
    }

    public void gameFinished() {
        //TODO send GameFinished to client /{game-id}/state
    }
}
