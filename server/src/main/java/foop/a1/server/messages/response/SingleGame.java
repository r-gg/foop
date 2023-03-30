package foop.a1.server.messages.response;

import foop.a1.server.dto.GameDTO;
import foop.a1.server.messages.Message;

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
