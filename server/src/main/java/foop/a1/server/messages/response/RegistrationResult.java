package foop.a1.server.messages.response;

import foop.a1.server.dto.GameDTO;
import foop.a1.server.dto.PlayerDTO;
import foop.a1.server.messages.Message;

public class RegistrationResult implements Message {
    private GameDTO game;
    private PlayerDTO player;
    private boolean successful;

    public RegistrationResult() {
    }

    public RegistrationResult(GameDTO game, PlayerDTO player) {
        this.game = game;
        this.player = player;
        this.successful = true;
    }

    public GameDTO getGame() {
        return game;
    }

    public void setGame(GameDTO game) {
        this.game = game;
    }

    public PlayerDTO getPlayer() {
        return player;
    }

    public void setPlayer(PlayerDTO player) {
        this.player = player;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }
}
