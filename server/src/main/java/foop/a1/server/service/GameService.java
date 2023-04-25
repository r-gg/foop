package foop.a1.server.service;

import foop.a1.server.dto.*;
import foop.a1.server.entities.Game;
import foop.a1.server.entities.GameBoard;
import foop.a1.server.entities.Player;
import foop.a1.server.entities.Position;
import foop.a1.server.messages.response.GameStarted;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameService {
    private final Logger logger;
    private final List<Game> games = new ArrayList<>();

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Value("${game.players}")
    private int PLAYERS_NEEDED;

    public GameService(Logger logger, SimpMessagingTemplate simpMessagingTemplate) {
        this.logger = logger;
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
        var gameStarted = new GameStarted();
        Pair<GameDTO, GameBoardDTO> dtos =this.gameToGameDTO(game);
        gameStarted.setGameDTO(dtos.getFirst());
        gameStarted.setGameBoardDTO(dtos.getSecond());
        // broadcast to everyone
        simpMessagingTemplate.convertAndSend("/topic/"+game.getGameId()+"/start", gameStarted);
    }

    public void removeGame(Game game) {
        this.games.remove(game);
    }

    public String registerPlayer(Game game, String userId) {
        var player = new Player(userId);
        // initial position
        player.setPosition(new Position(40, 40));
        game.addPlayer(player);

        return player.getPlayerId();
    }

    public void deregisterPlayer(Game game, Player player) {
        game.removePlayer(player);
    }

    public Optional<Player> getPlayer(Game game, String playerId) {
        return game.getPlayers().stream().filter(player -> Objects.equals(player.getPlayerId(), playerId)).findFirst();
    }

    public void updatePlayerPosition(Game game, String playerId, Position newPosition) {
        var player = getPlayer(game, playerId);
        if (player.isEmpty()) {
            this.logger.error("Player with id " + playerId + " not found in game" + game.getGameId());
            return;
        }
        player.get().setPosition(newPosition);
    }

    private Pair<GameDTO, GameBoardDTO> gameToGameDTO(Game game){
        var subwaysDtos = game.getBoard().getSubways()
                .stream().map(subway -> new SubwayDTO(
                        Arrays.stream(subway.getEntrances()).map(position -> new PositionDTO(position.x(), position.y())).toList(),
                        subway.getMice().stream().map(mouse -> new MouseDTO(new PositionDTO(mouse.getPosition().x(), mouse.getPosition().y()))).toList()
                )).toList();

        var playersDtos = game.getPlayers().stream()
                .map(player -> new PlayerDTO(player.getPlayerId() ,
                        new PositionDTO(player.getPosition().x(), player.getPosition().y())))
                .toList();

        var miceDtos = game.getMice().stream()
                .map(mouse -> new MouseDTO(new PositionDTO(mouse.getPosition().x(), mouse.getPosition().y())))
                .toList();

        var gameBoardDto = new GameBoardDTO(game.getBoard().getRoot(),
                game.getBoard().getDimensions(),
                subwaysDtos,
                playersDtos,
                miceDtos
        );
        return Pair.of(new GameDTO(game.getGameId(), game.getStatus().toString()), gameBoardDto);
    }
}
