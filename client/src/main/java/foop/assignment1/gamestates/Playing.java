package foop.assignment1.gamestates;

import foop.assignment1.entities.Enemy;
import foop.assignment1.entities.Player;
import foop.assignment1.entities.SubwayEntrance;
import foop.assignment1.main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Playing extends State implements StateMethods {
    private BufferedImage backgroundImg;
    private Player[] players;
    private Enemy[] enemies = { // mice
            new Enemy(50, 300, 0),
            new Enemy(10, 100, 0),
            new Enemy(20, 250, 0),
    };
    private SubwayEntrance[] subwayEntrances = {
            new SubwayEntrance(120, 120),
            new SubwayEntrance(20, 40),
            new SubwayEntrance(180, 180),
    };

    private void loadBackground() {
        InputStream is = getClass().getResourceAsStream("/static/background2.jpg");
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

    public Playing(Game game, Player[] players) {
        super(game);
        this.players = players;
//        loadBackground();
    }

    @Override
    public void update() {
        for (Player player : players) {
            player.update();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
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
        System.out.println(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void draw(Graphics g) {
//        g.drawImage(backgroundImg, 0, 0, backgroundImg.getWidth(), backgroundImg.getHeight(), null);

        for (Player player : players) {
            player.render(g);
        }

        for (Enemy enemy : enemies) {
            enemy.render(g);
        }

        for (SubwayEntrance entrance : subwayEntrances) {
            entrance.render(g);
        }
    }
}
