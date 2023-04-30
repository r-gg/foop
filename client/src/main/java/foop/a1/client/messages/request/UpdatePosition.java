package foop.a1.client.messages.request;

import foop.a1.client.dto.PositionDTO;
import foop.a1.client.messages.Message;

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
