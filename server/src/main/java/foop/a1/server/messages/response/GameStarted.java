package foop.a1.server.messages.response;

import foop.a1.server.dto.GameBoardDTO;
import foop.a1.server.dto.GameDTO;
import foop.a1.server.messages.Message;

public class GameStarted implements Message {


    private GameBoardDTO gameBoardDTO;

    private GameDTO gameDTO;

    public GameStarted(){

    }

    public GameStarted(GameBoardDTO gameBoardDTO, GameDTO gameDTO) {
        this.gameBoardDTO = gameBoardDTO;
        this.gameDTO = gameDTO;
    }

    public GameBoardDTO getGameBoardDTO() {
        return gameBoardDTO;
    }

    public void setGameBoardDTO(GameBoardDTO gameBoardDTO) {
        this.gameBoardDTO = gameBoardDTO;
    }

    public GameDTO getGameDTO() {
        return gameDTO;
    }

    public void setGameDTO(GameDTO gameDTO) {
        this.gameDTO = gameDTO;
    }
}
