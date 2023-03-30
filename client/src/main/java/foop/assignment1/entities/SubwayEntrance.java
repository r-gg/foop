package foop.assignment1.entities;

import java.awt.*;

public class SubwayEntrance extends Entity {
    public SubwayEntrance(float x, float y) {
        super(x, y);
//        loadAnimations();
    }

    // needed?
//    public void update() {
//
//    }

    public void render(Graphics g) {
        g.drawRect((int)x, (int)y, 10, 10);
    }
}
