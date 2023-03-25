package foop.a1.server.messages.response;

import foop.a1.server.dto.GameDTO;

public class SingleGame {
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
