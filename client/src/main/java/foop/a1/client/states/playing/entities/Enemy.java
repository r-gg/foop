package foop.a1.client.states.playing.entities;

public class Enemy extends Entity{
    protected int behaviorType;

    public Enemy(int x, int y, int behaviorType)
    {
        super(x,y);
        this.behaviorType = behaviorType;
    }
}
