package foop.a1.server.service;

import foop.a1.server.entities.Game;
import foop.a1.server.entities.GameBoard;
import foop.a1.server.entities.Player;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class GameService {
    private final List<Game> games = new ArrayList<>();

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
    }

    public String registerPlayer(Game game) {
        var player = new Player();
        game.addPlayer(player);

        return player.getPlayerId();
    }

    public void deregisterPlayer(Game game, Player player) {
        game.removePlayer(player);
    }

    public Optional<Player> getPlayer(Game game, String playerId) {
        return game.getPlayers().stream().filter(player -> Objects.equals(player.getPlayerId(), playerId)).findFirst();
    }
}
