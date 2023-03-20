package foop.a1.server.service;

import foop.a1.server.entities.Game;
import foop.a1.server.entities.GameBoard;
import foop.a1.server.entities.Player;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private final Logger logger;

    private final SimpMessagingTemplate messagingTemplate;

    // Currently only one game-session supported.
    private Game game; // todo: if support for concurrent games is needed, turn into a hashmap

    @Autowired
    public GameService(Logger logger, SimpMessagingTemplate messagingTemplate) {
        this.logger = logger;
        this.messagingTemplate = messagingTemplate;
        game = new Game();
    }

    public void registerPlayer(Player player){
        game.addPlayer(player);
    }

    public void createGame(){
        game.setBoard(new GameBoard(200,200));

    }

    public void startGame(){
        game.start();
    }

}
