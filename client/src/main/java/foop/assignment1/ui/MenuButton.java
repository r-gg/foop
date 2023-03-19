package foop.assignment1.ui;

import foop.assignment1.gamestates.GameState;

import javax.imageio.ImageIO;

import static foop.assignment1.utils.Constants.UI.Buttons.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MenuButton {

    private int xPos, yPos, rowIndex;
    private int xOffsetCenter = B_WIDTH / 2;
    private GameState state;
    private BufferedImage[] imgs;
    private boolean mouseOver, mousePressed;
    private Rectangle bounds;

    public MenuButton(int xPos, int yPos, int rowIndex, GameState state) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.rowIndex = rowIndex;
        this.state = state;
        loadImg();
        initBounds();
    }

    private void initBounds() {
        bounds = new Rectangle(xPos, yPos, 200, 50);
    }

    private void loadImg() {
        imgs = new BufferedImage[2];
        try{
            imgs[0] = ImageIO.read(getClass().getResourceAsStream("/static/play.png"));
            imgs[1] = ImageIO.read(getClass().getResourceAsStream("/static/quit.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void draw(Graphics g) {
        g.drawImage(imgs[rowIndex], xPos, yPos, imgs[rowIndex].getWidth(), imgs[rowIndex].getHeight(), null);
    }


    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void applyGamestate() {
        GameState.state = state;
    }

    public void resetBools() {
        mousePressed = false;
    }
    public GameState getState() {
        return state;
    }
}
