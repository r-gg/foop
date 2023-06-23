package foop.a1.client.main;

import foop.a1.client.messages.request.RegisterForGame;
import foop.a1.client.service.WebsocketService;
import foop.a1.client.states.State;
import foop.a1.client.states.menu.Menu;
import foop.a1.client.states.playing.entities.Player;
import org.springframework.messaging.simp.stomp.StompSession;

import java.awt.Graphics;


public class Game implements Runnable {

    private static WebsocketService websocketService;

    private final GameWindow gameWindow;
    private final GamePanel gamePanel;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;
    public final static int GAME_WIDTH = 832;
    public final static int GAME_HEIGHT = 448;
    private static Game instance;
    private static final Object mutex = new Object();
    private String gameId;
    private State state;
    private StompSession.Subscription createGameSubscription;
    private Player currentPlayer;

    private Game() {
        gamePanel = new GamePanel();
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocusInWindow();

        state = new Menu();

        startGameLoop();
    }

    public static Game instance() {
        Game _instance = instance;
        if (_instance == null) {
            synchronized (mutex) {
                _instance = instance;
                if (_instance == null)
                    instance = _instance = new Game();
            }
        }

        return _instance;
    }

    public void render(Graphics g) {
        state.draw(g);
    }

    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;

        long previousTime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while (true) {
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }

            if (deltaF >= 1) {
                gamePanel.repaint();
                frames++;
                deltaF--;
            }

            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;

            }
        }
    }

    private void startGameLoop() {
        Thread gameThread = new Thread(this);
        gameThread.start();
    }

    private void update() {
        state.update();
    }

    public State getState() {
        return state;
    }

    public void nextState(State state) {
        this.state = state;
    }

    public void subscribeToGame(){
        websocketService.subscribe("/user/queue/register");
        websocketService.subscribe("/topic/" + gameId + "/start");
        websocketService.subscribe("/topic/" + gameId + "/update");

        var registerForGame = new RegisterForGame();
        Game.service().sendRegisterForGame(this.gameId, registerForGame);
    }

    public void subscribeToPositionUpdates() {
        websocketService.subscribe("/topic/games/" + gameId + "/position-updated");
        websocketService.subscribe("/topic/games/" + gameId + "/enemies-positions-updated");
    }

    public void subscribeToGameOver() {
        websocketService.subscribe("/topic/games/" + gameId + "/over");
    }

    public void setWebsocketService(WebsocketService websocketService) {
        Game.websocketService = websocketService;
        createGameSubscription = websocketService.subscribe("/topic/games/create");
    }

    public static WebsocketService service() {
        return websocketService;
    }

    public String getGameId() {
        return gameId != null ? gameId : "";
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}
