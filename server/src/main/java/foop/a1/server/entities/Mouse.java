package foop.a1.server.entities;

import foop.a1.server.util.Constants;

import java.util.List;

public class Mouse {
    private String id;
    private Position position;

    private boolean isAboveGround = true;

    private List<Position> lastSeenCatPositions;

    private int remainingDelay = -1; // always -1 if not in subway (if above ground)

    private Subway currentSubway;

    public Subway getCurrentSubway() {
        return currentSubway;
    }

    public void setCurrentSubway(Subway currentSubway) {
        this.currentSubway = currentSubway;
    }

    public int getRemainingDelay() {
        return remainingDelay;
    }

    public void setRemainingDelay(int remainingDelay) {
        this.remainingDelay = remainingDelay;
    }

    public void resetDelay() {
        this.remainingDelay = -1;
    }

    public void decreaseRemainingDelay() {
        if (remainingDelay > 0) remainingDelay--;
    }

    public void setPosition(Position position) {

        this.position = new Position(
                Math.max(Math.min(position.x(), Constants.GAMEBOARD_WIDTH) , 0),
                Math.max(Math.min(position.y(), Constants.GAMEBOARD_HEIGHT) , 0)
        );
        if(this.position.y()==0){
            System.out.println("what happened");
        }
    }

    public Position getPosition() {
        return this.position;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isAboveGround() {
        return isAboveGround;
    }

    public void setAboveGround(boolean aboveGround) {
        isAboveGround = aboveGround;
    }

    /**
     * Exits the current subway: resets delay, sets above ground to true, and sets current subway to null.
     */
    public void exitSubway() {
        this.isAboveGround = true;
        this.resetDelay();
        this.currentSubway = null;
    }

    /**
     * Enters the given subway: sets above ground to false, sets current subway to the given subway, and sets remaining delay to the subway's delay.
     * @param subway
     */
    public void enterSubway(Subway subway){
        this.currentSubway = subway;
        this.isAboveGround = false;
        this.remainingDelay = subway.getDelay();
    }

    public void inform(List<Position> catPositions) {
        this.lastSeenCatPositions = catPositions;
    }

}
