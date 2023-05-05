package foop.a1.server.dto;

import java.util.List;

public class SubwayDTO {
    private List<PositionDTO> entrances;
    private List<MouseDTO> mice;

    private boolean isGoalSubway =false;

    public SubwayDTO() {
    }

    public SubwayDTO(List<PositionDTO> entrances, List<MouseDTO> mice) {
        this.entrances = entrances;
        this.mice = mice;
    }

    public boolean isGoalSubway() {
        return isGoalSubway;
    }

    public void setGoalSubway(boolean goalSubway) {
        isGoalSubway = goalSubway;
    }

    public List<PositionDTO> getEntrances() {
        return entrances;
    }

    public void setEntrances(List<PositionDTO> entrances) {
        this.entrances = entrances;
    }

    public List<MouseDTO> getMice() {
        return mice;
    }

    public void setMice(List<MouseDTO> mice) {
        this.mice = mice;
    }
}
