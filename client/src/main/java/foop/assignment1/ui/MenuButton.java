package foop.assignment1.ui;

import foop.assignment1.gamestates.GameState;

import static foop.assignment1.utils.Constants.UI.Buttons.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MenuButton {

    private int xPos, yPos, rowIndex;
    private int xOffsetCenter = B_WIDTH / 2;
    private GameState state;
    private BufferedImage img;
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
        bounds = new Rectangle(xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT);
    }

    private void loadImg() {
        /*imgs = new BufferedImage[3];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.MENU_BUTTONS);
        for (int i = 0; i < imgs.length; i++)
            imgs[i] = temp.getSubimage(i * B_WIDTH_DEFAULT, rowIndex * B_HEIGHT_DEFAULT, B_WIDTH_DEFAULT, B_HEIGHT_DEFAULT);*/
    }

    public void draw(Graphics g) {
        g.drawImage(img, xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT, null);
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
