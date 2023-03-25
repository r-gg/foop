package foop.a1.server.messages.response;

import foop.a1.server.dto.GameDTO;
import foop.a1.server.messages.Message;

import java.util.List;

public class AllGames implements Message {
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
}
