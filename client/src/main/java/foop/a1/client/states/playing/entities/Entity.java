package foop.a1.client.states.playing.entities;

public abstract class Entity {
    protected String id;
    protected Position position;

    public Entity(Position position) {
        this.position = position;
    }

    public Entity(String id, Position position) {
        this.id = id;
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
