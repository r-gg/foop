package foop.assignment1.entities;

public class Enemy extends Entity{
    protected int behaviorType;

    public Enemy(float x, float y, int behaviorType)
    {
        super(x,y);
        this.behaviorType = behaviorType;
    }
}
