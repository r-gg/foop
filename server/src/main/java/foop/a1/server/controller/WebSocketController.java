package foop.a1.server.controller;

import foop.a1.server.dto.*;
import foop.a1.server.entities.Game;
import foop.a1.server.entities.Position;
import foop.a1.server.messages.request.*;
import foop.a1.server.messages.request.StartGame;
import foop.a1.server.messages.response.*;
import foop.a1.server.service.GameService;
import foop.a1.server.util.GameStatus;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @MessageMapping("/games")
    public void getGames(GetGames getGames) {
        logger.info("Request to get all games");

        var games = gameService.getAllGames();
        var gamesDtos = new ArrayList<GameDTO>();
        games.forEach(game -> gamesDtos.add(new GameDTO(game.getGameId(), game.getStatus().toString())));

        logger.info("Games: {}", games);
        messagingTemplate.convertAndSend("/topic/games", new AllGames(gamesDtos));
    }

    @MessageMapping("/games/create")
    public void createGame(CreateGame createGame) {
        logger.info("Request to create game");

        List<Game> games = gameService.getAllGames();
        String waitingGameId = null;
        if (!games.isEmpty()) {
           var game = games.get(0);
           if (game.getStatus() == GameStatus.ENDED) {
               gameService.removeGame(game);
           } else {
               waitingGameId = game.getGameId();
               logger.info("Game {}: fetched", waitingGameId);
           }
        }
        if (waitingGameId == null) {
            waitingGameId = gameService.createGame();
            logger.info("Game {}: Created", waitingGameId);
        }
        var gameDto = new GameDTO(waitingGameId, GameStatus.WAITING.toString());

        messagingTemplate.convertAndSend("/topic/games/create", new SingleGame(gameDto));
    }

    @MessageMapping("/{gameId}/register")
    public void registerForGame(@DestinationVariable String gameId, RegisterForGame registerForGame) {
        logger.info("Game {}: Trying to register new player", gameId);

        var gameOpt = gameService.getGame(gameId);
        if (gameOpt.isEmpty()) {
            logger.info("Game {}: Game not found", gameId);
            messagingTemplate.convertAndSendToUser(registerForGame.getUsername(),"/queue/"+gameId+"/register", new RegistrationResult(){{setSuccessful(false);}});
            return;
        }

        var game = gameOpt.get();
        var playerId = gameService.registerPlayer(game);

        var playerDto = new PlayerDTO(playerId, new PositionDTO(0, 0));
        var gameDto = new GameDTO(game.getGameId(), game.getStatus().toString());

        logger.info("Game {} Player {}: Registration successful", game.getGameId(), playerId);
        messagingTemplate.convertAndSendToUser( registerForGame.getUsername(), "/queue/"+gameId+"/register", new RegistrationResult(gameDto, playerDto));
    }

    @MessageMapping("/games/start")
    public void startGame(StartGame startGame) {
        logger.info("Request to start the game");
        var gameOpt = gameService.getGame(startGame.getGameId());
        if (gameOpt.isEmpty()) {
            logger.info("Game {}: Game not found", startGame.getGameId());
//            messagingTemplate.convertAndSendToUser(registerForGame.getUsername(),"/queue/"+gameId+"/register", new RegistrationResult(){{setSuccessful(false);}});
            return;
        }

        gameService.startGame(gameOpt.get());
        logger.info("Game {} : Started successful", startGame.getGameId());
    }

//    public void positionUpdate(Game game) throws ExecutionException, InterruptedException, TimeoutException {
//        var client = new StandardWebSocketClient();
//        var stompClient = new WebSocketStompClient(client);
//        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
//
//        var futureSession = stompClient.connectAsync(URL, new StompSessionHandlerAdapter() {
//            @Override
//            public Type getPayloadType(StompHeaders headers) {
//                return CurrentPosition.class;
//            }
//
//            @Override
//            public void afterConnected(StompSession session, StompHeaders headers) {
//                session.subscribe(String.format("/topic/%s/update", game.getGameId()), this);
//            }
//
//            @Override
//            public void handleFrame(StompHeaders headers, Object payload) {
//                logger.info("Client received: payload {}, headers {}", payload, headers);
//            }
//        });
//
//        var session = futureSession.get(1, TimeUnit.SECONDS);
//        session.send(String.format("/%s/update", game.getGameId()), new PositionUpdate());
//    }

//    public void gameStatusUpdate(Game game) throws ExecutionException, InterruptedException, TimeoutException {
//        var client = new StandardWebSocketClient();
//        var stompClient = new WebSocketStompClient(client);
//        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
//
//        var futureSession = stompClient.connectAsync(URL, new StompSessionHandlerAdapter() {
//            @Override
//            public Type getPayloadType(StompHeaders headers) {
//                return StatusUpdate.class;
//            }
//        });
//
//        var gameDTOGameBoardDTOPair = gameToGameDTO(game);
//        var statusUpdate = new StatusUpdate(gameDTOGameBoardDTOPair.getFirst(), gameDTOGameBoardDTOPair.getSecond());
//
//        var session = futureSession.get(1, TimeUnit.SECONDS);
//        session.send(String.format("/%s/status", game.getGameId()), statusUpdate);
//    }

//    private void handlePositionUpdate(String gameId, CurrentPosition currentPosition) {
//        logger.info("Game {} Player {}: Position update received", gameId, currentPosition.getPlayer().getPlayerId());
//
//        var gameOpt = gameService.getGame(gameId);
//        if (gameOpt.isEmpty()) {
//            logger.info("Game {}: Game not found", gameId);
//            return;
//        }
//
//        var game = gameOpt.get();
//        var playerId = currentPosition.getPlayer().getPlayerId();
//        var playerOpt = gameService.getPlayer(game, playerId);
//        if (playerOpt.isEmpty()) {
//            logger.info("Game {} Player {}: Player not found", gameId, playerId);
//            return;
//        }
//
//        var player = playerOpt.get();
//        player.setPosition(new Position(currentPosition.getPlayer().getPosition().getY(), currentPosition.getPlayer().getPosition().getY()));
//    }
}
