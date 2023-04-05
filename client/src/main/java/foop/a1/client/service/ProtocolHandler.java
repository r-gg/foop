package foop.a1.client.service;


import foop.a1.client.main.Game;
import foop.a1.client.messages.ServerMessage;
import foop.a1.client.messages.response.SingleGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Component;

@Component
public class ProtocolHandler {


    public void handleResponse(StompHeaders headers, ServerMessage response){
        response.handleMessage();
    }

}
