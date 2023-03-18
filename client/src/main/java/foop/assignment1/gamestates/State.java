package foop.assignment1.gamestates;

import foop.assignment1.main.Game;
import foop.assignment1.ui.MenuButton;


import java.awt.event.MouseEvent;

public class State {
    protected Game game;

    public State(Game game) {
        this.game = game;
    }

    public boolean isIn(MouseEvent e, MenuButton mb) {
        return mb.getBounds().contains(e.getX(), e.getY());
    }

    public Game getGame() {
        return game;
    }

    public void setGameState(GameState state) {
        GameState.state = state;
    }


}
