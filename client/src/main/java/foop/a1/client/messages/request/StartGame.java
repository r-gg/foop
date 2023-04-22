package foop.a1.client.messages.request;

public class StartGame {
    private String gameId;

    public StartGame() {}

    public StartGame(String gameId) {
        this.gameId = gameId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
