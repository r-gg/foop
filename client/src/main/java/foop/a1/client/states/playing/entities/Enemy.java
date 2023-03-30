package foop.a1.client.states.playing.entities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Enemy extends Entity {
    protected int behaviorType;
    private BufferedImage img;

    public Enemy(int x, int y, int behaviorType) {
        super(x, y);
        this.behaviorType = behaviorType;
        this.loadImage();
    }

    public void render(Graphics g) {
        g.drawImage(img, x, y, 50, 30, null);
    }

    private void loadImage() {
        try (InputStream is = getClass().getResourceAsStream("/static/mouse.png")) {
            this.img = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
