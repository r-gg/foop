package foop.a1.server.dto;

public class PlayerDTO {
    private String playerId;
    private PositionDTO position;

    public PlayerDTO() {
    }

    public PlayerDTO(String playerId, PositionDTO position) {
        this.playerId = playerId;
        this.position = position;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public PositionDTO getPosition() {
        return position;
    }

    public void setPosition(PositionDTO position) {
        this.position = position;
    }
}
