package foop.a1.client.states.quit;

import foop.a1.client.states.State;

public class Quit extends State {
    @Override
    public void update() {
        System.exit(0);
    }
}
