package foop.a1.client.states.playing.entities;

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
        g.drawImage(img, position.getX(), position.getY(), 50, 30, null);
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
