package foop.a1.client.messages.response;

import foop.a1.client.dto.GameBoardDTO;
import foop.a1.client.dto.GameDTO;
import foop.a1.client.messages.Message;

public class StatusUpdate implements Message {
    private GameDTO game;
    private GameBoardDTO gameBoard;

    public StatusUpdate() {
    }

    public StatusUpdate(GameDTO game, GameBoardDTO gameBoard) {
        this.game = game;
        this.gameBoard = gameBoard;
    }

    public GameDTO getGame() {
        return game;
    }

    public void setGame(GameDTO game) {
        this.game = game;
    }

    public GameBoardDTO getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(GameBoardDTO gameBoard) {
        this.gameBoard = gameBoard;
    }


}
