package foop.a1.client.states.playing.entities;

import foop.a1.client.util.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Enemy extends Entity {
    private static BufferedImage img;

    public Enemy(String id, Position position) {
        super(id, position);
        if (img == null) { // avoid reloading on each instantiation
            img = this.loadImage();
        }
    }

    public void render(Graphics g) {
        g.drawImage(img, position.getX() - (Constants.ENEMY_IMAGE_WIDTH/2) , position.getY() - (Constants.ENEMY_IMAGE_HEIGHT/2), Constants.ENEMY_IMAGE_WIDTH, Constants.ENEMY_IMAGE_HEIGHT, null);
        g.setColor(Color.BLUE);
        g.drawRect(position.getX() - (4/2) , position.getY() - (4/2), 4, 4);
    }

    private BufferedImage loadImage() {
        try (InputStream is = getClass().getResourceAsStream("/static/mouse.png")) {
            return ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
