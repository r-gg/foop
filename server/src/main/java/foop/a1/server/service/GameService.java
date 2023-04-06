package foop.a1.server.service;

import foop.a1.server.entities.Game;
import foop.a1.server.entities.GameBoard;
import foop.a1.server.entities.Player;
import foop.a1.server.messages.response.StartGame;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class GameService {
    private final List<Game> games = new ArrayList<>();

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Value("${game.players}")
    private int PLAYERS_NEEDED;

    public GameService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public String createGame() {
        var game = new Game() {{
            setBoard(new GameBoard(200, 200));
        }};
        games.add(game);
        return game.getGameId();
    }

    public List<Game> getAllGames() {
        return games;
    }

    public Optional<Game> getGame(String gameId) {
        return games.stream().filter(game -> Objects.equals(game.getGameId(), gameId)).findFirst();
    }

    public void startGame(Game game) {
        game.start();
        // broadcast to everyone
        simpMessagingTemplate.convertAndSend("/topic/"+game.getGameId()+"/start", new StartGame());
    }

    public String registerPlayer(Game game) {
        var player = new Player();
        game.addPlayer(player);
        if(game.getPlayers().size() == PLAYERS_NEEDED){
            startGame(game);
        }

        return player.getPlayerId();
    }

    public void deregisterPlayer(Game game, Player player) {
        game.removePlayer(player);
    }

    public Optional<Player> getPlayer(Game game, String playerId) {
        return game.getPlayers().stream().filter(player -> Objects.equals(player.getPlayerId(), playerId)).findFirst();
    }
}
