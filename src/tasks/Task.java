package tasks;

import util.Status;
import util.TypeTask;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    protected Integer id;
    protected String name;
    protected String description;
    protected Status status;
    protected Integer duration;
    protected LocalDateTime startTime;

    public Task(String name, String description, Status status, Integer duration, LocalDateTime startTime) {

        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(Integer id, String name, String description, Status status, Integer duration, LocalDateTime startTime) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TypeTask getType() {
        return TypeTask.TASK;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getStartTimeFormat() {
        String formatStart = dateFormatter(startTime);
        return formatStart;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public String getEndTimeFormat() {
        LocalDateTime endTime = startTime.plusMinutes(duration);
        String formatEnd = dateFormatter(endTime);
        return formatEnd;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration);
    }

    public String dateFormatter(LocalDateTime format) {
        LocalDateTime time = format;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
        String formatDate = time.format(formatter);
        return formatDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(name, task.name) && Objects.equals(description,
                task.description) && status == task.status && Objects.equals(duration, task.duration)
                && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status, duration, startTime);
    }

    @Override
    public String toString() {
        return id + "," +
                getType() + "," +
                name + "," +
                status + "," +
                description + "," +
                duration + "," +
                getStartTimeFormat() + "," +
                getEndTimeFormat();
    }
}
