package foop.assignment1.gamestates;

import foop.assignment1.main.Game;
import foop.assignment1.ui.MenuButton;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Menu extends State implements StateMethods{

    private MenuButton[] buttons = new MenuButton[2];
    private BufferedImage backgroundImg;
    private int menuX, menuY, menuWidth, menuHeight;

    private void loadButtons() {
        //buttons[0] = new MenuButton(Game.GAME_WIDTH / 2, (int) (130 * Game.SCALE), 0, GameState.WAITINGFOREVERYONE);
        //buttons[1] = new MenuButton(Game.GAME_WIDTH / 2, (int) (340 * Game.SCALE), 1, GameState.QUIT);
    }

    private void loadBackground() {
        InputStream is = getClass().getResourceAsStream("/static/background.png");
        try {
            BufferedImage img = ImageIO.read(is);
            backgroundImg = img;
            //menuWidth = (int) (backgroundImg.getWidth() * Game.SCALE);
            //menuHeight = (int) (backgroundImg.getHeight() * Game.SCALE);
            //menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
            //menuY = (int) (25 * Game.SCALE);
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

    public Menu(Game game) {
        super(game);
        loadButtons();
        loadBackground();
    }

    @Override
    public void update() {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, backgroundImg.getWidth(), backgroundImg.getHeight(), null);

        /*for (MenuButton mb : buttons)
            mb.draw(g);*/
    }
}
