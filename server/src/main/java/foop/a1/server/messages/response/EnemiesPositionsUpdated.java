package foop.a1.server.messages.response;

import foop.a1.server.dto.PositionDTO;
import foop.a1.server.messages.Message;

import java.util.HashMap;
import java.util.Map;

public class EnemiesPositionsUpdated implements Message {
    Map<String, PositionDTO> newPositionsById = new HashMap<>();

    public Map<String, PositionDTO> getNewPositionsById() {
        return newPositionsById;
    }

    public void setNewPositionsById(Map<String, PositionDTO> newPositionsById) {
        this.newPositionsById = newPositionsById;
    }
}
