package foop.assignment1.main;

import foop.assignment1.entities.Player;
import foop.assignment1.gamestates.GameState;
import foop.assignment1.gamestates.Playing;
import foop.assignment1.gamestates.WaitingForEveryone;
import foop.assignment1.gamestates.Menu;

import java.awt.Graphics;


public class Game implements Runnable {

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;
    public final static float SCALE = 2f;

    public final static int TILES_DEFAULT_SIZE = 32;
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = 832;
    public final static int GAME_HEIGHT = 448;
    private Player player;

    private Menu menu;

    private WaitingForEveryone waiting;
    private Playing playing;

    public Game() {
        initClasses();

        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocusInWindow();

        startGameLoop();
    }

    private void initClasses() {
        menu = new Menu(this);
        //playing = new Playing(this);
        //waiting = new WaitingForEveryone(this);

    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        switch (GameState.state) {
            case MENU -> menu.update();
            //case PLAYING -> playing.update();
            //case WAITINGFOREVERYONE -> waiting.update();
            case QUIT -> System.exit(0);
        }
    }

    public void render(Graphics g) {
        switch (GameState.state) {
            case MENU -> menu.draw(g);
            //case PLAYING -> playing.draw(g);
            //the rest
        }
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

    public void windowFocusLost() {
        //player.resetDirBooleans();
    }

    public Player getPlayer() {
        return player;
    }

    public Menu getMenu() {
        return menu;
    }

}
