package foop.a1.client.states.playing.entities;

import foop.a1.client.util.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class SubwayEntrance extends Entity {
    private BufferedImage backgroundImg;

    private boolean belongsToGoalSubway = false;

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

    public boolean isBelongsToGoalSubway() {
        return belongsToGoalSubway;
    }

    public void setBelongsToGoalSubway(boolean belongsToGoalSubway) {
        this.belongsToGoalSubway = belongsToGoalSubway;
    }

    public void render(Graphics g) {
        g.drawImage(backgroundImg, position.getX() - (Constants.SUBWAY_IMAGE_WIDTH/2) , position.getY() - (Constants.SUBWAY_IMAGE_HEIGHT/2), Constants.SUBWAY_IMAGE_WIDTH, Constants.SUBWAY_IMAGE_HEIGHT, null);
        if (belongsToGoalSubway) {
            g.setColor(Color.GREEN);
            g.drawRect(position.getX() - (Constants.SUBWAY_IMAGE_WIDTH/2) , position.getY() - (Constants.SUBWAY_IMAGE_HEIGHT/2), Constants.SUBWAY_IMAGE_WIDTH, Constants.SUBWAY_IMAGE_HEIGHT);
        }
    }
}

