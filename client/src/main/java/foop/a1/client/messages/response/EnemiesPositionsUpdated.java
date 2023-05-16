package foop.a1.client.messages.response;

import foop.a1.client.dto.PositionDTO;
import foop.a1.client.messages.Message;

import java.util.HashMap;
import java.util.Map;

public class EnemiesPositionsUpdated implements Message {
    Map<String, PositionDTO> newPositionsById = new HashMap<>();

    Map<String, Integer> scores = new HashMap<>();

    public Map<String, PositionDTO> getNewPositionsById() {
        return newPositionsById;
    }

    public void setNewPositionsById(Map<String, PositionDTO> newPositionsById) {
        this.newPositionsById = newPositionsById;
    }

    public Map<String, Integer> getScores() {
        return scores;
    }

    public void setScores(Map<String, Integer> scores) {
        this.scores = scores;
    }
}

