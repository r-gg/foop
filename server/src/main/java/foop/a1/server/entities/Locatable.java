package foop.a1.server.entities;

public interface Locatable {
    void setPosition(Position var1);

    Position getPosition();

    boolean isAboveGround();
}
