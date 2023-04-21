package foop.a1.client.messages.response;

import foop.a1.client.dto.GameBoardDTO;
import foop.a1.client.dto.GameDTO;
import foop.a1.client.messages.Message;

public class StartGame implements Message {


    private GameBoardDTO gameBoardDTO;

    private GameDTO game;

    public StartGame(){

    }

    public StartGame(GameBoardDTO gameBoardDTO, GameDTO game) {
        this.gameBoardDTO = gameBoardDTO;
        this.game = game;
    }

    public GameBoardDTO getGameBoardDTO() {
        return gameBoardDTO;
    }

    public void setGameBoardDTO(GameBoardDTO gameBoardDTO) {
        this.gameBoardDTO = gameBoardDTO;
    }

    public GameDTO getGame() {
        return game;
    }

    public void setGame(GameDTO game) {
        this.game = game;
    }
}
