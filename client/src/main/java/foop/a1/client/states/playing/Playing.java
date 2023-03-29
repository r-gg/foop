package foop.a1.client.states.playing;

import foop.a1.client.states.playing.entities.Player;
import foop.a1.client.states.State;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Playing extends State {
    private final Player player;

    public Playing(){
        player = new Player();
    }

    @Override
    public void draw(Graphics g) {
        player.render(g);
    }

    @Override
    public void update() {
        player.update();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> player.setUp(true);
            case KeyEvent.VK_A -> player.setLeft(true);
            case KeyEvent.VK_S -> player.setDown(true);
            case KeyEvent.VK_D -> player.setRight(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> player.setUp(false);
            case KeyEvent.VK_A -> player.setLeft(false);
            case KeyEvent.VK_S -> player.setDown(false);
            case KeyEvent.VK_D -> player.setRight(false);
        }
    }
}
