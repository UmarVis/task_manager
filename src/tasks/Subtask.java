package tasks;

import util.Status;
import util.TypeTask;

import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    private Integer epicId;

    public Subtask(String name, String description, Status status, Integer duration, LocalDateTime startTime, Integer epicId) {
        super(name, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(Integer id, String name, String description, Status status, Integer duration, LocalDateTime startTime,
                   Integer epicId) {
        super(id, name, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return Objects.equals(epicId, subtask.epicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public TypeTask getType() {
        return TypeTask.SUBTASK;
    }

    @Override
    public String toString() {
        return super.getId() + "," +
                getType() + "," +
                super.getName() + "," +
                super.getStatus() + "," +
                super.getDescription() + "," +
                super.getDuration() + "," +
                super.getStartTimeFormat() + "," +
                super.getEndTimeFormat() + "," +
                getEpicId();
    }
}
