package foop.a1.client.states.playing.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class Player extends Entity {
    private static BufferedImage animation;
    private boolean left, up, right, down;

    public Player(String id, Position position) {
        super(id, position);
        if (animation == null) {
            animation = loadAnimation();
        }
    }

    public void update() {
        updatePos();
    }

    public void render(Graphics g) {
        g.drawImage(animation, position.getX(), position.getY(), 256, 160, null);
    }

    private void updatePos() {
        Integer playerSpeed = 2;
        if (left && !right) {
            position.setX(position.getX() - playerSpeed);
        } else if (right && !left) {
            position.setX(position.getX() + playerSpeed);
        }

        if (up && !down) {
            position.setY(position.getY() - playerSpeed);
        } else if (down && !up) {
            position.setY(position.getY() + playerSpeed);
        }
    }

    private BufferedImage loadAnimation() {
        try (InputStream is = getClass().getResourceAsStream("/static/cat.png")) {
            BufferedImage img = ImageIO.read(is);

            return img.getSubimage(50, 50, 50, 50);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

}

