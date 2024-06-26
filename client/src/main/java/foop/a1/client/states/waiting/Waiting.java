package foop.a1.client.states.waiting;

import foop.a1.client.main.Game;
import foop.a1.client.messages.request.StartGame;
import foop.a1.client.states.State;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Waiting extends State {
    private BufferedImage backgroundImg;
    private BufferedImage waitingImg;
    private final Rectangle startButton = new Rectangle(10, 10, 100, 50);

    public Waiting() {
        loadImages();
        Game.instance().subscribeToGame();
    }

    private void loadImages() {
        try (InputStream is = getClass().getResourceAsStream("/static/background.png")) {
            backgroundImg = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (InputStream is2 = getClass().getResourceAsStream("/static/waiting.png")) {
            waitingImg = ImageIO.read(is2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        StartGame startGame = new StartGame(Game.instance().getGameId());
        Game.service().sendGameStart(startGame);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, backgroundImg.getWidth(), backgroundImg.getHeight(), null);
        g.drawImage(waitingImg, 0, 0, backgroundImg.getWidth(), backgroundImg.getHeight(), null);
        g.drawRect((int) startButton.getX(), (int) startButton.getY(), (int) startButton.getWidth(), (int) startButton.getHeight());
        g.drawString("Start", (int) startButton.getX() + 20, (int) startButton.getY() + 20);
    }
}
