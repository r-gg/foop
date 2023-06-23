package foop.a1.client.states.gameover;

import foop.a1.client.states.State;
import foop.a1.client.util.Constants;

import java.awt.*;

public class GameOver extends State {
    private final String winnerTeamName;

    public GameOver(String winnerTeamName) {
        this.winnerTeamName = winnerTeamName;
    }

    @Override
    public void draw(Graphics g) {
        g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
        g.drawString("Game over!", Constants.WINDOW_WIDTH/2 - 100, Constants.WINDOW_HEIGHT/2 - 50);
        g.drawString("Winner: " + winnerTeamName, Constants.WINDOW_WIDTH/2 - 100, Constants.WINDOW_HEIGHT/2 + 50);
    }
}
