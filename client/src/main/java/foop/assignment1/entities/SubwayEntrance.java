package foop.assignment1.entities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class SubwayEntrance extends Entity {
    private BufferedImage backgroundImg;

    public SubwayEntrance(float x, float y) {
        super(x, y);
        loadImage();
    }

    private void loadImage() {
        InputStream is = getClass().getResourceAsStream("/static/entrance.jpg");
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

    public void render(Graphics g) {
        g.drawImage(backgroundImg, (int)x, (int)y, 40, 40, null);
    }
}
