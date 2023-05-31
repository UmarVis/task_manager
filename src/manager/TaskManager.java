package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface TaskManager {
    int nextId();

    List<Task> getHistory();

    ArrayList<Task> getAllTasks();

    void clearTasks();

    Task getTask(int id);

    Task createTask(Task task);

    Task updateTask(Task taskUpdate);

    Task removeTask(int id);

    ArrayList<Epic> getAllEpics();

    void clearEpics();

    Epic getEpic(int epicId);

    Epic createEpic(Epic newEpic);

    Epic updateEpic(Epic epic);

    void removeEpic(int epicId);

    ArrayList<Subtask> getAllSubtasks();

    Subtask removeSubtask(int subTaskId);

    Subtask createSubtask(Subtask subtask);

    void updateSubtask(Subtask updateSubtask);

    Subtask getSubtask(int subTaskId);

    void clearAllSubtasks();

    ArrayList<Subtask> getEpicSubtasks(int epicId);

    void findEpicStatus(Epic epic);

    void durationEpic(Task task);

    Set<Task> getPrioritizedTasks();
}
