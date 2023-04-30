package foop.a1.client.states.gameover;

import foop.a1.client.states.State;

import java.awt.*;

public class GameOver extends State {
    private String winnerTeamName;

    public GameOver(String winnerTeamName) {
        this.winnerTeamName = winnerTeamName;
    }

    @Override
    public void draw(Graphics g) {
        g.drawString("Game over!", 200, 100);
        g.drawString("Winner: " + winnerTeamName, 200, 200);
    }
}
