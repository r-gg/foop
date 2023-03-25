package foop.a1.client.controller;

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
}
