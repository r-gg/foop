package foop.a1.server.dto;

import org.springframework.data.util.Pair;

import java.util.List;

public class GameBoardDTO {
    private Pair<Integer, Integer> root;
    private Pair<Integer, Integer> dimensions;
    private List<SubwayDTO> subways;

    private List<PlayerDTO> players;

    private List<MouseDTO> mice;

    public GameBoardDTO(Pair<Integer, Integer> root, Pair<Integer, Integer> dimensions, List<SubwayDTO> subways, List<PlayerDTO> players, List<MouseDTO> mice) {
        this.root = root;
        this.dimensions = dimensions;
        this.subways = subways;
        this.players = players;
        this.mice = mice;
    }

    public List<PlayerDTO> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerDTO> players) {
        this.players = players;
    }

    public List<MouseDTO> getMice() {
        return mice;
    }

    public void setMice(List<MouseDTO> mice) {
        this.mice = mice;
    }

    public Pair<Integer, Integer> getRoot() {
        return root;
    }

    public void setRoot(Pair<Integer, Integer> root) {
        this.root = root;
    }

    public Pair<Integer, Integer> getDimensions() {
        return dimensions;
    }

    public void setDimensions(Pair<Integer, Integer> dimensions) {
        this.dimensions = dimensions;
    }

    public List<SubwayDTO> getSubways() {
        return subways;
    }

    public void setSubways(List<SubwayDTO> subways) {
        this.subways = subways;
    }
}
