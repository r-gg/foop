package foop.assignment1.gamestates;

import foop.assignment1.main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class WaitingForEveryone extends State implements StateMethods {
    private BufferedImage backgroundImg;
    // temporary button to be able to start the game manually(for gui development)
    private Rectangle startButton = new Rectangle(100, 100, 100, 50);

    private void loadBackground() {
        InputStream is = getClass().getResourceAsStream("/static/background.png");
        try {
            BufferedImage img = ImageIO.read(is);
            backgroundImg = img;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public WaitingForEveryone(Game game) {
        super(game);
        loadBackground();
    }

    @Override
    public void update() {


    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (startButton.getBounds().contains(e.getX(), e.getY())) {
            GameState.state = GameState.PLAYING;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, backgroundImg.getWidth(), backgroundImg.getHeight(), null);
        g.drawRect((int)startButton.getX(), (int)startButton.getY(), (int)startButton.getWidth(), (int)startButton.getHeight());
        g.drawString("Start", (int)startButton.getX() + 20, (int)startButton.getY() + 20);

        g.drawString("Waiting for all players", backgroundImg.getWidth() / 2, backgroundImg.getHeight() / 2);
    }
}
