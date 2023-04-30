package foop.a1.client.states.menu;

import foop.a1.client.states.State;
import foop.a1.client.main.Game;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Function;

public class MenuButton {
    private final int xPos;
    private final int yPos;
    private final int rowIndex;
    private final Function<Void, Void> _onClick;
    private BufferedImage[] images;
    private Rectangle bounds;

    public MenuButton(int xPos, int yPos, int rowIndex, Function<Void, Void> onClick) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.rowIndex = rowIndex;
        this._onClick = onClick;

        loadImg();
        initBounds();
    }

    private void initBounds() {
        bounds = new Rectangle(xPos, yPos, 200, 50);
    }

    private void loadImg() {
        images = new BufferedImage[2];
        try{
            images[0] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/static/play.png")));
            images[1] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/static/quit.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void draw(Graphics g) {
        g.drawImage(images[rowIndex], xPos, yPos, images[rowIndex].getWidth(), images[rowIndex].getHeight(), null);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void onClick() {
        this._onClick.apply(null);
    }
}
