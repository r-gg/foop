package foop.a1.server.dto;

public class MouseDTO {
    private PositionDTO position;

    public MouseDTO(){
    }

    public MouseDTO(PositionDTO position){
        this.position = position;
    }

    public PositionDTO getPosition() {
        return position;
    }

    public void setPosition(PositionDTO position) {
        this.position = position;
    }
}
