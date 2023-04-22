package foop.a1.client.states.menu;

import foop.a1.client.messages.request.CreateGame;
import foop.a1.client.states.quit.Quit;
import foop.a1.client.states.State;
import foop.a1.client.main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Menu extends State {
    private final MenuButton[] buttons = new MenuButton[2];
    private BufferedImage backgroundImg;

    public Menu() {
        loadButtons();
        loadBackground();
    }

    private void loadButtons() {
        buttons[0] = new MenuButton(Game.GAME_WIDTH / 2 - 100, 50, 0, (Void v) -> {
            // create game button
            var createGame = new CreateGame();
            Game.service().sendGameCreate(createGame);
            return null;
        });
        buttons[1] = new MenuButton(Game.GAME_WIDTH / 2 - 100, 150, 1, (Void v) -> {
            // quit button
            this.applyNextState(new Quit());
            return null;
        });
    }

    private void loadBackground() {
        try (InputStream is = getClass().getResourceAsStream("/static/background.png")){
            backgroundImg = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, backgroundImg.getWidth(), backgroundImg.getHeight(), null);

        for (MenuButton mb : buttons)
            mb.draw(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        for (MenuButton mb : buttons) {
            if (isIn(e, mb)) {
                mb.onClick();
                break;
            }
        }
    }

    void applyNextState(State state) {
        Game.instance().nextState(state);
    }
}
