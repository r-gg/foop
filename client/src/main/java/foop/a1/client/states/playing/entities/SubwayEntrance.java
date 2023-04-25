package foop.a1.client.states.playing.entities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class SubwayEntrance extends Entity {
    private BufferedImage backgroundImg;

    public SubwayEntrance(Position position) {
        super(position);
        loadImage();
    }

    private void loadImage() {
        try (InputStream is = getClass().getResourceAsStream("/static/entrance.jpg")){
            backgroundImg = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void render(Graphics g) {
        g.drawImage(backgroundImg, position.getX(), position.getY(), 40, 40, null);
    }
}

