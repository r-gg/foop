package foop.a1.client.states.playing.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class Player extends Entity {
    private BufferedImage animation;
    private boolean left, up, right, down;

    public Player() {
        super(0, 0);
        loadAnimation();
    }

    public Player(int x, int y) {
        super(x, y);
    }

    public Player(String id, int x, int y) {
        super(id, x, y);
    }

    public void update() {
        updatePos();
    }

    public void render(Graphics g) {
        g.drawImage(animation, x, y, 256, 160, null);
    }

    private void updatePos() {
        float playerSpeed = 2.0f;
        if (left && !right) {
            x -= playerSpeed;
        } else if (right && !left) {
            x += playerSpeed;
        }

        if (up && !down) {
            y -= playerSpeed;
        } else if (down && !up) {
            y += playerSpeed;
        }
    }

    private void loadAnimation() {
        try (InputStream is = getClass().getResourceAsStream("/static/cat.png")) {
            BufferedImage img = ImageIO.read(is);

            animation = img.getSubimage(50, 50, 50, 50);

        } catch (IOException e) {
            e.printStackTrace();
        }
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

