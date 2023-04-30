package foop.a1.server.entities;

import org.springframework.data.util.Pair;

import java.util.Arrays;
import java.util.List;

public class GameBoard {
    private Pair<Integer, Integer> root;
    private Pair<Integer, Integer> dimensions;
    private List<Subway> subways = Arrays.asList(
            new Subway(Arrays.asList(new Position(120, 120),new Position(20, 40))),
            new Subway(Arrays.asList(new Position(180, 180),new Position(300, 350))),
            new Subway(Arrays.asList(new Position(230, 270),new Position(100, 150)))
    );

    public GameBoard(int width, int height) {
        root = Pair.of(0, 0);
        dimensions = Pair.of(width, height);
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

    public List<Subway> getSubways() {
        return subways;
    }

    public void setSubways(List<Subway> subways) {
        this.subways = subways;
    }

    public void addSubway(Subway subway) {
        if (subway != null) subways.add(subway);
    }

}

