package foop.a1.client.inputs;

import foop.a1.client.main.Game;
import foop.a1.client.main.GamePanel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class KeyboardInputs extends KeyAdapter {
    @Override
    public void keyReleased(KeyEvent e) {
        Game.instance().getState().keyReleased(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Game.instance().getState().keyPressed(e);
    }
}