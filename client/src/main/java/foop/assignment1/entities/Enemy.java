package foop.assignment1.entities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Enemy extends Entity{
    protected int behaviorType;
    private BufferedImage img;

    public Enemy(float x, float y, int behaviorType)
    {
        super(x,y);
        this.behaviorType = behaviorType;
        this.loadImage();
    }

    public void render(Graphics g) {
        g.drawImage(img, (int) x, (int) y, 50, 30, null);
    }

    private void loadImage() {
        InputStream is = getClass().getResourceAsStream("/static/mouse.png");
        try {
            BufferedImage img = ImageIO.read(is);
            this.img = img;
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
}
