package foop.a1.client.main;

import foop.a1.client.service.WebsocketService;
import foop.a1.client.states.State;
import foop.a1.client.states.menu.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private String gameId;
    private State state;

    private Game() {
        gamePanel = new GamePanel();
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocusInWindow();

        state = new Menu();

        startGameLoop();
    }

    public static Game instance() {
        if (instance == null)
            instance = new Game();

        return instance;
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
        // TODO: Maybe redraw here because it is called from the SingleGame.class
    }

    public void subscribeToGame(){
        websocketService.subscribe("/topic/"+instance().gameId+"/register");
        websocketService.subscribe("/topic/"+instance().gameId+"/update");
    }

    public static void setWebsocketService(WebsocketService websocketService) {
        Game.websocketService = websocketService;
    }

    public String getGameId() {
        return gameId != null ? gameId : "";
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
