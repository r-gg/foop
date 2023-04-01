package foop.a1.client.messages.response;

import foop.a1.client.dto.GameDTO;
import foop.a1.client.messages.ServerMessage;

import java.util.List;

// do we need all games?
public class AllGames implements ServerMessage {
    private List<GameDTO> games;

    public AllGames() {
    }

    public AllGames(List<GameDTO> games) {
        this.games = games;
    }

    public List<GameDTO> getGames() {
        return games;
    }

    public void setGames(List<GameDTO> games) {
        this.games = games;
    }

    @Override
    public void handleMessage() {

    }
}
