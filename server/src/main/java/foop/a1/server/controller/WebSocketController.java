package foop.a1.server.controller;

import foop.a1.server.models.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;
import foop.assignment1.messages.RedrawRequest;

@Controller
public class WebSocketController {

    private Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final SimpMessagingTemplate template;


    public WebSocketController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("/update")
    @SendTo("/topic")
    public void updateLocation(MessageModel message){
        LOGGER.info("Got something in Controller: {}", message);
        template.convertAndSend("/topic", message);
    }

    public void redrawGameBoard(RedrawRequest redrawRequest){

    }


}
