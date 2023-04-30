package foop.a1.server.messages.request;

import foop.a1.server.dto.PositionDTO;
import foop.a1.server.messages.Message;

public class UpdatePosition implements Message {
    private String gameId;
    private PositionDTO newPosition;

    public UpdatePosition() {}

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameId() {
        return gameId;
    }

    public PositionDTO getNewPosition() {
        return newPosition;
    }

    public void setNewPosition(PositionDTO newPosition) {
        this.newPosition = newPosition;
    }
}
