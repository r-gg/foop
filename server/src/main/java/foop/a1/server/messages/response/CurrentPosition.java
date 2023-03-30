package foop.a1.server.messages.response;

import foop.a1.server.dto.PlayerDTO;
import foop.a1.server.messages.Message;

public class CurrentPosition implements Message {
    private PlayerDTO player;

    public CurrentPosition(){}

    public CurrentPosition(PlayerDTO player){
        this.player = player;
    }

    public PlayerDTO getPlayer() {
        return player;
    }

    public void setPlayer(PlayerDTO player) {
        this.player = player;
    }
}
