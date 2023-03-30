package foop.a1.client.dto;

import org.springframework.data.util.Pair;

import java.util.List;

public class GameBoardDTO {
    private Pair<Integer, Integer> root;
    private Pair<Integer, Integer> dimensions;
    private List<SubwayDTO> subways;

    public GameBoardDTO(Pair<Integer, Integer> root, Pair<Integer, Integer> dimensions, List<SubwayDTO> subways) {
        this.root = root;
        this.dimensions = dimensions;
        this.subways = subways;
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
