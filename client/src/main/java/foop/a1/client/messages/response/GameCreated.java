package foop.a1.client.messages.response;

import foop.a1.client.dto.GameDTO;
import foop.a1.client.messages.Message;

public class GameCreated implements Message {
    private GameDTO game;

    public GameCreated() {
    }

    public GameCreated(GameDTO game) {
        this.game = game;
    }

    public GameDTO getGame() {
        return game;
    }

    public void setGame(GameDTO game) {
        this.game = game;
    }
}
