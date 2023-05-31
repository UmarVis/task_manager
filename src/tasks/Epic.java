package tasks;

import util.Status;
import util.TypeTask;

import java.time.LocalDateTime;

public class Epic extends Task {

    private LocalDateTime endTime = LocalDateTime.of(2000, 1, 1, 0, 0);

    public Epic(String name, String description, Status status, Integer duration, LocalDateTime startTime) {
        super(name, description, status, duration, startTime);
    }

    public Epic(Integer id, String name, String description, Status status, Integer duration, LocalDateTime startTime) {
        super(id, name, description, status, duration, startTime);
    }

    public TypeTask getType() {
        return TypeTask.EPIC;
    }

    @Override
    public Integer getDuration() {
        return super.getDuration();
    }

    @Override
    public String getStartTimeFormat() {
        return super.getStartTimeFormat();
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getEndTimeFormat() {
        LocalDateTime endTime = this.endTime;
        String formatEnd = dateFormatter(endTime);
        return formatEnd;
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
                getEndTimeFormat();
    }

}
