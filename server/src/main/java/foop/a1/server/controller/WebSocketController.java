package foop.a1.server.controller;

import foop.assignment1.messages.RegisterForGame;
import foop.assignment1.messages.RegistrationSuccessful;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

    @MessageMapping("/register")
    public void registerForGame(RegisterForGame registerForGame) {
        logger.info("Player {} registered", registerForGame.getPlayer().getPlayerId().getId());

        var registration = new RegistrationSuccessful();
        registration.setPlayer(registerForGame.getPlayer());

        messagingTemplate.convertAndSend("/topic/register", registration);
    }

    public void positionUpdate() {
        //TODO send PositionUpdate to client /update and subscribe to /topic/update receiving CurrentPosition
    }

    public void redrawGameBoard() {
        //TODO send RedrawGameBoard to client /redraw
    }

    public void gameWon() {
        //TODO send GameWon to client /state
    }

    public void gameLost() {
        //TODO send GameLost to client /state
    }

}
