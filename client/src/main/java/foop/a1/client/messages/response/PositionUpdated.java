package foop.a1.client.messages.response;

import foop.a1.client.dto.PositionDTO;
import foop.a1.client.messages.Message;

public class PositionUpdated implements Message {
    private String gameId;
    private String playerId;
    private PositionDTO newPosition;

    public PositionUpdated() {}

    public PositionUpdated(String gameId, String playerId, PositionDTO newPosition) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.newPosition = newPosition;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public PositionDTO getNewPosition() {
        return newPosition;
    }

    public void setNewPosition(PositionDTO newPosition) {
        this.newPosition = newPosition;
    }
}
