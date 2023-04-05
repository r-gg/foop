package foop.a1.client.messages.response;

import foop.a1.client.dto.GameDTO;
import foop.a1.client.main.Game;
import foop.a1.client.messages.ServerMessage;
import foop.a1.client.states.menu.Menu;
import foop.a1.client.states.waiting.Waiting;

public class SingleGame implements ServerMessage {
    private GameDTO game;

    public SingleGame() {
    }

    public SingleGame(GameDTO game) {
        this.game = game;
    }

    public GameDTO getGame() {
        return game;
    }

    public void setGame(GameDTO game) {
        this.game = game;
    }

    public void handleMessage(){
        Game clientGame = Game.instance();
        if(clientGame.getState().getClass().equals(Menu.class)){
            clientGame.setGameId(game.getGameId());
            clientGame.nextState(new Waiting());
        }
    }
}
