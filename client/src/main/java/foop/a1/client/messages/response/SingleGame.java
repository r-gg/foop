package foop.a1.client.messages.response;

import foop.a1.client.dto.GameDTO;
import foop.a1.client.main.Game;
import foop.a1.client.messages.Message;
import foop.a1.client.states.menu.Menu;
import foop.a1.client.states.waiting.Waiting;

public class SingleGame implements Message {
    private GameDTO game;

    public SingleGame() {
    }

    public SingleGame(GameDTO game) {
        this.game = game;
    }

    public GameDTO getGame() {
        return game;
    }

    public void setGame(GameDTO game) {
        this.game = game;
    }
}
