package foop.a1.client.inputs;

import foop.a1.client.main.Game;

import java.awt.event.*;

public class MouseInputs extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent e) {
        Game.instance().getState().mouseClicked(e);
    }
}
