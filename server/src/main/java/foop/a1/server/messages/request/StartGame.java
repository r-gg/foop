package foop.a1.server.messages.request;

public class StartGame {
    private String gameId;

    StartGame() {}

    StartGame(String gameId) {
        this.gameId = gameId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
