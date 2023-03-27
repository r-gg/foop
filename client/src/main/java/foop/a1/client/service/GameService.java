package foop.a1.client.service;

import foop.a1.client.entities.Player;
import foop.a1.client.main.Game;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameService {
    private Game game;

    public Optional<Game> getGame(String gameId) {
        return Optional.ofNullable(game);
    }

    public Optional<Player> getPlayer(Game game, String playerId) {
        return Optional.ofNullable(game.getPlayer());
    }
}
