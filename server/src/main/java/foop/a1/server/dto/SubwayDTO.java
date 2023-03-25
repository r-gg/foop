package foop.a1.server.dto;

import java.util.List;

public class SubwayDTO {
    private PositionDTO[] entrances;
    private List<MouseDTO> mice;

    public SubwayDTO() {
    }

    public SubwayDTO(PositionDTO[] entrances, List<MouseDTO> mice) {
        this.entrances = entrances;
        this.mice = mice;
    }

    public PositionDTO[] getEntrances() {
        return entrances;
    }

    public void setEntrances(PositionDTO[] entrances) {
        this.entrances = entrances;
    }

    public List<MouseDTO> getMice() {
        return mice;
    }

    public void setMice(List<MouseDTO> mice) {
        this.mice = mice;
    }
}
