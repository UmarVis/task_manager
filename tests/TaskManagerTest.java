import manager.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import util.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    protected Task task;
    protected Epic epic;
    protected Subtask subtask;

    protected void taskManagerSetUp() {
        task = new Task("Задача 1", "Описание задачи 1", Status.NEW, 25,
                LocalDateTime.of(2022, 9, 21, 13, 30));
        taskManager.createTask(task);
        epic = new Epic("Эпик 1", "Описание эпика 1", Status.NEW, 0,
                LocalDateTime.of(2000, 1, 1, 0, 0));
        taskManager.createEpic(epic);
        subtask = new Subtask("Подзадача 1 эпика 1", "Описание подзадачи 1",
                Status.NEW, 35, LocalDateTime.of(2022, 9, 22, 12, 0), 2);
        taskManager.createSubtask(subtask);
    }

    @Test
    void nextId() {
        assertEquals(4, taskManager.nextId(), "Не вероно возвращается ИД");
    }

    @Test
    void getHistory() {
        Task task2 = new Task("Задача 1", "Описание задачи 1", Status.NEW, 25,
                LocalDateTime.of(2022, 9, 22, 13, 30));
        Task task3 = new Task("Задача 1", "Описание задачи 1", Status.NEW, 25,
                LocalDateTime.of(2022, 9, 23, 13, 30));
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.getTask(2);
        taskManager.getTask(3);
        List<Task> tasks = taskManager.getAllTasks();
        assertNotNull(tasks, "Нет истории");
        assertEquals(3, tasks.size(), "Не верное количество задач в истории");
        assertEquals(task, tasks.get(0), "Задачи не совпадают");
        assertEquals(task2, tasks.get(1), "Задачи не совпадают");
        assertEquals(task3, tasks.get(2), "Задачи не совпадают");
    }

    @Test
    void getAllTasks() {
        List<Task> allTasks = taskManager.getAllTasks();
        assertNotNull(allTasks, "Задачи не возвращаются.");
        assertEquals(1, allTasks.size(), "Неверное количество задач.");
    }

    @Test
    void clearTasks() {
        taskManager.clearTasks();
        List<Task> allTasks = taskManager.getAllTasks();
        assertNotNull(allTasks, "Задачи не возвращаются.");
        assertEquals(0, allTasks.size(), "Задачи не удалены.");
    }

    @Test
    void getTask() {
        List<Task> tasks = taskManager.getAllTasks();
        Task getTask = taskManager.getTask(1);
        assertEquals(1, tasks.size(), "Не верное количество задач");
        assertEquals(getTask, tasks.get(0), "Задачи не совпадают");
    }

    @Test
    void createTask() {
        Task task = new Task("Задача 1", "Описание задачи 1", Status.NEW, 25,
                LocalDateTime.of(2022, 9, 22, 13, 30));
        taskManager.createTask(task);
        final int taskId = task.getId();

        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(2, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(1), "Задачи не совпадают.");
    }

    @Test
    void updateTask() {
        Task task1 = new Task(1, "Задача 1", "Описание задачи 1", Status.NEW, 50,
                LocalDateTime.of(2022, 10, 1, 13, 0));
        taskManager.updateTask(task1);
        final List<Task> tasks = taskManager.getAllTasks();
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertNotEquals(task, tasks.get(0), "Задачи не совпадают.");
        assertEquals(task1, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void removeTask() {
        Task task2 = new Task("Задача 1", "Описание задачи 1", Status.NEW, 25,
                LocalDateTime.of(2022, 9, 22, 13, 30));
        Task task3 = new Task("Задача 1", "Описание задачи 1", Status.NEW, 25,
                LocalDateTime.of(2022, 9, 23, 13, 30));
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.removeTask(1);
        List<Task> tasks = taskManager.getAllTasks();
        assertEquals(2, tasks.size(), "Задача не удалена");
    }

    @Test
    void getAllEpics() {
        List<Epic> allEpics = taskManager.getAllEpics();
        assertNotNull(allEpics, "Эпики не возвращаются.");
        assertEquals(1, allEpics.size(), "Неверное количество эпиков.");
    }

    @Test
    void clearEpics() {
        taskManager.clearEpics();
        List<Epic> allEpics = taskManager.getAllEpics();
        assertNotNull(allEpics, "Эпики не возвращаются.");
        assertEquals(0, allEpics.size(), "Эпики не удалены.");
    }

    @Test
    void getEpic() {
        List<Epic> allEpics = taskManager.getAllEpics();
        Epic epic = taskManager.getEpic(2);
        assertEquals(1, allEpics.size(), "Не верное количество эпиков");
        assertEquals(epic, allEpics.get(0), "Эпики не совпадают");
    }

    @Test
    void createEpic() {
        Epic epic1 = new Epic("Эпик 2", "Описание эпика 2", Status.NEW, 0,
                LocalDateTime.of(2000, 1, 1, 0, 0));
        taskManager.createEpic(epic1);
        final int epicId = epic1.getId();

        final Task savedEpic = taskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Эпик не найдена.");
        assertEquals(epic1, savedEpic, "Эпики не совпадают.");

        final List<Epic> epics = taskManager.getAllEpics();

        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(2, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic1, epics.get(1), "Эпики не совпадают.");
    }

    @Test
    void updateEpic() {
        Epic epic1 = new Epic(2, "Эпик 1", "Новое описание эпика 1", Status.NEW, 10,
                LocalDateTime.of(2022, 10, 1, 13, 0));
        taskManager.updateEpic(epic1);
        final List<Epic> epics = taskManager.getAllEpics();
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic1, epics.get(0), "Эпики не совпадают.");
    }

    @Test
    void removeEpic() {
        Epic epic1 = new Epic("Эпик 2", "Описание эпика 2", Status.NEW, 5,
                LocalDateTime.of(2000, 1, 1, 10, 0));
        Epic epic2 = new Epic("Эпик 3", "Описание эпика 3", Status.NEW, 15,
                LocalDateTime.of(2000, 1, 1, 13, 0));
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.removeEpic(2);
        List<Epic> epics = taskManager.getAllEpics();
        assertEquals(2, epics.size(), "Эпики не удалены");
    }

    @Test
    void getAllSubtasks() {
        List<Subtask> allSubtasks = taskManager.getAllSubtasks();
        assertNotNull(allSubtasks, "Подзадачи не возвращаются.");
        assertEquals(1, allSubtasks.size(), "Неверное количество подзадач.");
    }

    @Test
    void removeSubtask() {
        Subtask subtask1 = new Subtask("Подзадача 2 эпика 1", "Описание подзадачи 2",
                Status.DONE, 35, LocalDateTime.of(2022, 10, 1, 12, 0), 2);
        Subtask subtask2 = new Subtask("Подзадача 3 эпика 1", "Описание подзадачи 2",
                Status.DONE, 25, LocalDateTime.of(2022, 10, 2, 12, 30), 2);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.removeSubtask(5);
        List<Subtask> subtasks = taskManager.getAllSubtasks();
        assertEquals(2, subtasks.size(), "Подзадачи не удалены");
    }

    @Test
    void createSubtask() {
        Subtask subtask1 = new Subtask("Подзадача 2 эпика 1", "Описание подзадачи 2",
                Status.DONE, 35, LocalDateTime.of(2022, 10, 1, 12, 0), 2);
        taskManager.createSubtask(subtask1);
        final int epicId = subtask1.getId();

        final Task savedSub = taskManager.getSubtask(epicId);

        assertNotNull(savedSub, "Подзадача не найдена.");
        assertEquals(subtask1, savedSub, "Подзадачи не совпадают.");

        final List<Subtask> subtasks = taskManager.getAllSubtasks();

        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(2, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask1, subtasks.get(1), "Подзадачи не совпадают.");
    }

    @Test
    void updateSubtask() {
        Subtask subtask1 = new Subtask(3, "Подзадача 2 эпика 1", "Новое описание подзадачи 2",
                Status.DONE, 35, LocalDateTime.of(2022, 10, 1, 12, 0), 2);
        taskManager.updateSubtask(subtask1);
        final List<Subtask> subtasks = taskManager.getAllSubtasks();
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask1, subtasks.get(0), "Подзадачи не совпадают.");
    }

    @Test
    void getSubtask() {
        List<Subtask> allSub = taskManager.getAllSubtasks();
        Subtask subtask = taskManager.getSubtask(3);
        assertEquals(1, allSub.size(), "Не верное количество подзадач");
        assertEquals(subtask, allSub.get(0), "Подзадачи не совпадают");
    }

    @Test
    void clearAllSubtasks() {
        taskManager.clearAllSubtasks();
        List<Subtask> allSub = taskManager.getAllSubtasks();
        assertNotNull(allSub, "Подзадачи не возвращаются.");
        assertEquals(0, allSub.size(), "Подзадачи не удалены.");
    }

    @Test
    void getEpicSubtasks() {
        Subtask subtask1 = new Subtask("Подзадача 2 эпика 1", "Описание подзадачи 2",
                Status.NEW, 35, LocalDateTime.of(2022, 10, 1, 12, 0), 2);
        Subtask subtask2 = new Subtask("Подзадача 3 эпика 1", "Описание подзадачи 3",
                Status.DONE, 25, LocalDateTime.of(2022, 10, 1, 8, 0), 2);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        ArrayList<Subtask> subtasksOfEpic = taskManager.getEpicSubtasks(2);
        assertNotNull(subtasksOfEpic, "Подзадачи не возвращаются.");
        assertEquals(3, subtasksOfEpic.size(), "Не верное количество подзадач");
    }

    // Пустой список подзадач
    @Test
    void emptyListSubtask() {
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1", Status.DONE, 5,
                LocalDateTime.of(2022, 10, 15, 13, 0));
        taskManager.clearEpics();
        taskManager.clearAllSubtasks();
        taskManager.createEpic(epic1);
        taskManager.findEpicStatus(epic1);
        final List<Subtask> subtasks = taskManager.getAllSubtasks();
        Status epicStatus = epic1.getStatus();
        assertTrue(subtasks.isEmpty(), "Присутствуют подзадачи");
        assertEquals(Status.NEW, epicStatus, "Не верный статус");
    }

    // Все подзадачи со статусом NEW
    @Test
    void allSubtasksStatusNew() {
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1", Status.DONE, 5,
                LocalDateTime.of(2022, 10, 15, 13, 0));
        Subtask subtask1 = new Subtask("Подзадача 1 эпика 1", "Описание подзадачи 1",
                Status.NEW, 30, LocalDateTime.of(2022, 9, 22, 8, 0), 4);
        Subtask subtask2 = new Subtask("Подзадача 2 эпика 1", "Описание подзадачи 2",
                Status.NEW, 35, LocalDateTime.of(2022, 9, 22, 10, 0), 4);
        Subtask subtask3 = new Subtask("Подзадача 3 эпика 1", "Описание подзадачи 3",
                Status.NEW, 40, LocalDateTime.of(2022, 9, 22, 14, 0), 4);
        taskManager.clearAllSubtasks();
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        taskManager.findEpicStatus(epic1);
        final List<Subtask> subtasks = taskManager.getAllSubtasks();
        Status epicStatus = epic1.getStatus();
        assertFalse(subtasks.isEmpty(), "Отсутствуют подзадачи");
        assertEquals(Status.NEW, epicStatus, "Не верный статус");
    }

    // Все подзадачи со статусом DONE
    @Test
    void allSubtasksStatusDone() {
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1", Status.NEW, 5,
                LocalDateTime.of(2022, 10, 15, 13, 0));
        Subtask subtask1 = new Subtask("Подзадача 1 эпика 1", "Описание подзадачи 1",
                Status.DONE, 30, LocalDateTime.of(2022, 9, 22, 8, 0), 4);
        Subtask subtask2 = new Subtask("Подзадача 2 эпика 1", "Описание подзадачи 2",
                Status.DONE, 35, LocalDateTime.of(2022, 9, 22, 10, 0), 4);
        Subtask subtask3 = new Subtask("Подзадача 3 эпика 1", "Описание подзадачи 3",
                Status.DONE, 40, LocalDateTime.of(2022, 9, 22, 14, 0), 4);
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        taskManager.findEpicStatus(epic1);
        final List<Subtask> subtasks = taskManager.getAllSubtasks();
        Status epicStatus = epic1.getStatus();
        assertFalse(subtasks.isEmpty(), "Отсутствуют подзадачи");
        assertEquals(Status.DONE, epicStatus, "Не верный статус");
    }

    // Все подзадачи со статусом NEW/DONE
    @Test
    void allSubtasksStatusNewAndDone() {
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1", Status.NEW, 5,
                LocalDateTime.of(2022, 10, 15, 13, 0));
        Subtask subtask1 = new Subtask("Подзадача 1 эпика 1", "Описание подзадачи 1",
                Status.NEW, 30, LocalDateTime.of(2022, 9, 22, 8, 0), 4);
        Subtask subtask2 = new Subtask("Подзадача 2 эпика 1", "Описание подзадачи 2",
                Status.DONE, 35, LocalDateTime.of(2022, 9, 22, 10, 0), 4);
        Subtask subtask3 = new Subtask("Подзадача 3 эпика 1", "Описание подзадачи 3",
                Status.NEW, 40, LocalDateTime.of(2022, 9, 22, 14, 0), 4);
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        taskManager.findEpicStatus(epic1);
        final List<Subtask> subtasks = taskManager.getAllSubtasks();
        Status epicStatus = epic1.getStatus();
        assertFalse(subtasks.isEmpty(), "Отсутствуют подзадачи");
        assertEquals(Status.IN_PROGRESS, epicStatus, "Не верный статус");
    }

    // Все подзадачи со статусом InProgress
    @Test
    void allSubtasksStatusInProgress() {
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1", Status.NEW, 5,
                LocalDateTime.of(2022, 10, 15, 13, 0));
        Subtask subtask1 = new Subtask("Подзадача 1 эпика 1", "Описание подзадачи 1",
                Status.IN_PROGRESS, 30, LocalDateTime.of(2022, 9, 22, 8, 0), 4);
        Subtask subtask2 = new Subtask("Подзадача 2 эпика 1", "Описание подзадачи 2",
                Status.IN_PROGRESS, 35, LocalDateTime.of(2022, 9, 22, 10, 0), 4);
        Subtask subtask3 = new Subtask("Подзадача 3 эпика 1", "Описание подзадачи 3",
                Status.IN_PROGRESS, 40, LocalDateTime.of(2022, 9, 22, 14, 0), 4);
        taskManager.clearAllSubtasks();
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        taskManager.findEpicStatus(epic1);
        final List<Subtask> subtasks = taskManager.getAllSubtasks();
        Status epicStatus = epic1.getStatus();
        assertFalse(subtasks.isEmpty(), "Отсутствуют подзадачи");
        assertEquals(Status.IN_PROGRESS, epicStatus, "Не верный статус");
    }

    @Test
    void durationEpic() {
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1", Status.DONE, 5,
                LocalDateTime.of(2022, 10, 15, 13, 0));
        Subtask subtask1 = new Subtask("Подзадача 1 эпика 1", "Описание подзадачи 1",
                Status.NEW, 10, LocalDateTime.of(2022, 9, 22, 8, 0), 4);
        Subtask subtask2 = new Subtask("Подзадача 2 эпика 1", "Описание подзадачи 2",
                Status.NEW, 10, LocalDateTime.of(2022, 9, 22, 10, 0), 4);
        Subtask subtask3 = new Subtask("Подзадача 3 эпика 1", "Описание подзадачи 3",
                Status.NEW, 10, LocalDateTime.of(2022, 9, 22, 14, 0), 4);
        taskManager.clearAllSubtasks();
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        taskManager.updateEpic(epic1);
        final List<Subtask> subtasks = taskManager.getAllSubtasks();
        Integer epicDuration = epic1.getDuration();
        assertFalse(subtasks.isEmpty(), "Отсутствуют подзадачи");
        assertEquals(30, epicDuration, "Не верно возвращается продолжительность эпика");
    }

    @Test
    void startTimeEpic() {
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1", Status.DONE, 5,
                LocalDateTime.of(2000, 1, 1, 8, 0));
        Subtask subtask1 = new Subtask("Подзадача 1 эпика 1", "Описание подзадачи 1",
                Status.NEW, 10, LocalDateTime.of(2022, 9, 1, 9, 0), 4);
        Subtask subtask2 = new Subtask("Подзадача 2 эпика 1", "Описание подзадачи 2",
                Status.NEW, 10, LocalDateTime.of(2022, 9, 10, 10, 0), 4);
        Subtask subtask3 = new Subtask("Подзадача 3 эпика 1", "Описание подзадачи 3",
                Status.NEW, 10, LocalDateTime.of(2022, 9, 20, 14, 0), 4);
        taskManager.clearAllSubtasks();
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        taskManager.updateEpic(epic1);
        final List<Subtask> subtasks = taskManager.getAllSubtasks();
        LocalDateTime startTime = epic1.getStartTime();
        assertFalse(subtasks.isEmpty(), "Отсутствуют подзадачи");
        assertEquals(LocalDateTime.of(2022, 9, 1, 9, 0), startTime,
                "Не верно возвращается время начало эпика");
    }

    @Test
    void endTimeEpic() {
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1", Status.DONE, 5,
                LocalDateTime.of(2000, 1, 1, 8, 0));
        Subtask subtask1 = new Subtask("Подзадача 1 эпика 1", "Описание подзадачи 1",
                Status.NEW, 10, LocalDateTime.of(2022, 9, 1, 9, 0), 4);
        Subtask subtask2 = new Subtask("Подзадача 2 эпика 1", "Описание подзадачи 2",
                Status.NEW, 10, LocalDateTime.of(2022, 9, 10, 10, 0), 4);
        Subtask subtask3 = new Subtask("Подзадача 3 эпика 1", "Описание подзадачи 3",
                Status.NEW, 10, LocalDateTime.of(2022, 9, 20, 14, 0), 4);
        taskManager.clearAllSubtasks();
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        taskManager.updateEpic(epic1);
        final List<Subtask> subtasks = taskManager.getAllSubtasks();
        String endTime = epic1.getEndTimeFormat();
        assertFalse(subtasks.isEmpty(), "Отсутствуют подзадачи");
        assertEquals("20.09.2022, 14:10", endTime,
                "Не верно возвращается продолжительность эпика");
    }
}