package foop.a1.client.dto;

public class MouseDTO {
    private String id;
    private PositionDTO position;

    public MouseDTO(){
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public PositionDTO getPosition() {
        return position;
    }

    public void setPosition(PositionDTO position) {
        this.position = position;
    }
}
