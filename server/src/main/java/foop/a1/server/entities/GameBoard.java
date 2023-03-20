package foop.a1.server.entities;

import org.springframework.data.util.Pair;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GameBoard {

    private Pair<Integer, Integer> dimensions;

    private Pair<Integer, Integer> root;

    private Collection<Locatable> locatable = new ArrayList();

    private List<Subway> subways;

    public GameBoard(int width, int height) {
        root = Pair.of(0,0);
        dimensions = Pair.of(width, height);
        this.subways = new ArrayList<>();
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

    public Collection<Locatable> getLocatable() {
        return this.locatable;
    }

    public void setLocatable(Collection<Locatable> locatable) {
        this.locatable = locatable;
    }


}

