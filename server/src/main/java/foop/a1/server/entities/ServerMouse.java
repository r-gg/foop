package foop.a1.server.entities;


public class ServerMouse extends Mouse implements Locatable, Runnable{


    private boolean isAboveGround;

    public void inform(Game game){
        // update mouse model with
        game.getCatLocations();
    }

    @Override
    public void run() {
        // run, eat cheese

        // squeak


    }

    @Override
    public boolean isAboveGround() {
        return false;
    }
}
