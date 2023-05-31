package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import util.Status;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected HashMap<Integer, Task> tableTask;
    protected HashMap<Integer, Subtask> tableSubtask;
    protected HashMap<Integer, Epic> tableEpic;
    protected int newId = 0;

    private Status statusNew = Status.NEW;
    private Status inProgress = Status.IN_PROGRESS;
    private Status done = Status.DONE;
    private TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime,
            Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(Task::getId));

    HistoryManager historyManager = Managers.getDefaultHistory();

    public InMemoryTaskManager() {
        this.tableTask = new HashMap<>();
        this.tableSubtask = new HashMap<>();
        this.tableEpic = new HashMap<>();
    }

    @Override
    public int nextId() {
        return ++newId;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    // Получение списка всех задач.
    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tableTask.values());
    }

    // Удаление всех задач.
    @Override
    public void clearTasks() {
        tableTask.clear();
    }

    // Получение по идентификатору.
    @Override
    public Task getTask(int id) {
        Task task = tableTask.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return tableTask.get(id);
    }

    // Создание.
    @Override
    public Task createTask(Task task) {
        int taskId = nextId();
        task.setId(taskId);
        LocalDateTime taskStartTime = task.getStartTime();
        for (Task tasks : getPrioritizedTasks()) {
            if (tasks.getStartTime().equals(taskStartTime)) {
                System.out.println("Задачи пересекаются по времени выполнения.");
                return null;
            }
        }
        tableTask.put(taskId, task);
        prioritizedTasks.add(task);
        return task;
    }

    // Обновление.
    @Override
    public Task updateTask(Task taskUpdate) {
        int taskId = taskUpdate.getId();
        Task findTask = tableTask.get(taskId);
        if (findTask == null) {
            System.out.println("Такой задачи для обновления нет.");
            return null;
        }
        tableTask.put(taskId, taskUpdate);
        return taskUpdate;
    }

    // Удаление по идентификатору.
    @Override
    public Task removeTask(int id) {
        historyManager.remove(tableTask.get(id));
        return tableTask.remove(id);
    }

    // ЭПИКИ
    // Получение списка всех эпиков.
    @Override
    public ArrayList<Epic> getAllEpics() {
        for (Epic epics : tableEpic.values()) {
            durationEpic(epics);
            setStartAndEndTimeEpic(epics);
        }
        return new ArrayList<>(tableEpic.values());
    }

    // Удаление всех эпиков.
    @Override
    public void clearEpics() {
        tableEpic.clear();
        tableSubtask.clear();
    }

    // Получение по идентификатору.
    @Override
    public Epic getEpic(int epicId) {
        Epic epic = tableEpic.get(epicId);
        if (epic != null) {
            historyManager.add(epic);
        }
        durationEpic(epic);
        setStartAndEndTimeEpic(epic);
        return epic;
    }

    // Создание.
    @Override
    public Epic createEpic(Epic newEpic) {
        int epicId = nextId();
        newEpic.setId(epicId);
        tableEpic.put(epicId, newEpic);
        updateEpic(newEpic);
        return newEpic;
    }

    // Обновление.
    @Override
    public Epic updateEpic(Epic epic) {
        int epicId = epic.getId();
        Epic findEpic = tableEpic.get(epicId);
        if (findEpic == null) {
            System.out.println("Такого эпика для обновления нет.");
            return null;
        }
        findEpicStatus(epic);
        durationEpic(epic);
        setStartAndEndTimeEpic(epic);
        tableEpic.put(epicId, epic);
        return epic;
    }

    // Удаление по идентификатору.
    @Override
    public void removeEpic(int epicId) {
        ArrayList<Subtask> getAllSubtasks = getAllSubtasks();
        for (Subtask subtasks : getAllSubtasks) {
            if (epicId == subtasks.getEpicId()) {
                tableSubtask.remove(subtasks.getId());
            }
        }
        historyManager.remove(tableEpic.get(epicId));
        tableEpic.remove(epicId);
    }

    // Подзадачи
    // Получение списка всех подзадач.
    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(tableSubtask.values());
    }

    // Удаление подзадачи.
    public Subtask removeSubtask(int subTaskId) {
        Subtask subtask = tableSubtask.get(subTaskId);
        Epic epic = tableEpic.get(subtask.getEpicId());
        if (epic == null) {
            System.out.println("Такой подзадачи для удаления в эпике нет");
            return null;
        }
        updateEpic(epic);
        historyManager.remove(tableSubtask.get(subTaskId));
        return tableSubtask.remove(subTaskId);
    }

    // Создание подзадачи
    @Override
    public Subtask createSubtask(Subtask subtask) {
        Epic epic = tableEpic.get(subtask.getEpicId());
        if (epic == null) {
            System.out.println("Не возможно создать подзадачу (отс-ет ИД эпика)");
            return null;
        }
        int taskId = nextId();
        subtask.setId(taskId);
        if (!tableSubtask.isEmpty()) {
            LocalDateTime subtaskStartTime = subtask.getStartTime();
            for (Task tasks : getPrioritizedTasks()) {
                if (tasks.getStartTime().equals(subtaskStartTime)) {
                    System.out.println("Подзадачи пересекаются по времени выполнения.");
                    return null;
                }
            }
        }
        tableSubtask.put(taskId, subtask);
        updateEpic(epic);
        prioritizedTasks.add(subtask);
        return subtask;
    }

    // Обновление подзадачи
    @Override
    public void updateSubtask(Subtask updateSubtask) {
        int subTaskId = updateSubtask.getId();
        Subtask findSubTask = tableSubtask.get(subTaskId);
        if (findSubTask == null) {
            System.out.println("Такая подзадача для обновления не найдена.");
            return;
        }
        if (updateSubtask.getEpicId().equals(findSubTask.getEpicId())) {
            Epic epicSubtask = tableEpic.get(findSubTask.getEpicId());
            updateEpic(epicSubtask);
        }
        tableSubtask.put(subTaskId, updateSubtask);
        Epic epic = tableEpic.get(updateSubtask.getEpicId());
        updateEpic(epic);
    }

    // Получение по идентификатору.
    @Override
    public Subtask getSubtask(int subTaskId) {
        Subtask subtask = tableSubtask.get(subTaskId);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    // Удаление всех подзадач.
    @Override
    public void clearAllSubtasks() {
        tableSubtask.clear();
        ArrayList<Epic> allEpics = getAllEpics();
        for (Epic epics : allEpics) {
            int epicId = epics.getId();
            Epic updateEpic = tableEpic.get(epicId);
            findEpicStatus(updateEpic);
        }
    }

    // Получение списка всех подзадач определённого эпика.
    @Override
    public ArrayList<Subtask> getEpicSubtasks(int epicId) {
        ArrayList<Subtask> allSubtasks = new ArrayList<>();
        ArrayList<Subtask> getAllSubtasks = getAllSubtasks();
        for (Subtask subtasks : getAllSubtasks) {
            if (epicId == subtasks.getEpicId()) {
                allSubtasks.add(subtasks);
            }
        }
        return allSubtasks;
    }

    // Управление статусами.
    @Override
    public void findEpicStatus(Epic epic) {
        ArrayList<Subtask> allSubtasks = new ArrayList<>();
        ArrayList<Subtask> getAllSubtasks = getAllSubtasks();
        Integer statusEpicId = epic.getId();
        Epic statusEpic = tableEpic.get(statusEpicId);
        for (Subtask allSubtasksArr : getAllSubtasks) {
            if (statusEpicId == allSubtasksArr.getEpicId()) {
                allSubtasks.add(allSubtasksArr);
            }
        }
        int countEpicIdEpicSub = 0;
        int countStatusNew = 0;
        int countStatusDone = 0;
        if (allSubtasks.isEmpty()) {
            statusEpic.setStatus(statusNew);
        }
        for (Subtask subtasks : allSubtasks) {
            if (statusEpicId == subtasks.getEpicId()) {
                countEpicIdEpicSub += 1;
                if (subtasks.getStatus().equals(statusNew)) {
                    countStatusNew += 1;
                } else if (subtasks.getStatus().equals(done)) {
                    countStatusDone += 1;
                }
            }
        }

        if (countEpicIdEpicSub == countStatusNew) {
            statusEpic.setStatus(statusNew);
        } else if (countEpicIdEpicSub == countStatusDone) {
            statusEpic.setStatus(done);
        } else {
            statusEpic.setStatus(inProgress);
        }
    }

    //Продолжительность эпика
    @Override
    public void durationEpic(Task task) {
        Integer duration = 0;
        ArrayList<Subtask> getAllSubtasks = getAllSubtasks();
        Integer epicId = task.getId();
        Epic durationEpic = tableEpic.get(epicId);
        for (Subtask sub : getAllSubtasks) {
            if (epicId == sub.getEpicId()) {
                duration = duration + sub.getDuration();
            }
        }
        durationEpic.setDuration(duration);
    }

    //Время начала/время окончания
    public void setStartAndEndTimeEpic(Task task) {
        ArrayList<Subtask> getAllSubtasks = getAllSubtasks();
        ArrayList<Subtask> subIdEpic = new ArrayList();
        Integer epicId = task.getId();
        Epic startTimeEpic = tableEpic.get(epicId);
        Epic endTimeEpic = tableEpic.get(epicId);
        for (Subtask allSub : getAllSubtasks) {
            if (epicId == allSub.getEpicId()) {
                subIdEpic.add(allSub);
            }
        }
        if (!subIdEpic.isEmpty()) {
            Subtask subtask = subIdEpic.get(0);
            LocalDateTime startTimeSub = subtask.getStartTime();
            LocalDateTime endTTimeSub = subtask.getEndTime();
            for (Subtask allSub : getAllSubtasks) {
                if (epicId == allSub.getEpicId()) {
                    if (startTimeSub.isAfter(allSub.getStartTime())) {
                        startTimeSub = allSub.getStartTime();
                    }
                    if (endTTimeSub.isBefore(allSub.getEndTime())) {
                        endTTimeSub = allSub.getEndTime();
                    }
                }
            }
            startTimeEpic.setStartTime(startTimeSub);
            endTimeEpic.setEndTime(endTTimeSub);
            Epic epic = tableEpic.get(epicId);
            prioritizedTasks.add(epic);
        }
    }

    //Выведите список задач в порядке приоритета
    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }
}


