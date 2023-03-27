package foop.a1.client.dto;

public class GameDTO {
    private String gameId;
    private String status;

    public GameDTO() {
    }

    public GameDTO(String gameId, String status) {
        this.gameId = gameId;
        this.status = status;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
