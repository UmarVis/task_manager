package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import exception.ManagerSaveException;
import util.Status;
import util.TypeTask;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public FileBackedTasksManager() {
    }

    @Override
    public int nextId() {
        return super.nextId();
    }

    @Override
    public List<Task> getHistory() {
//        save();
        return super.getHistory();
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        save();
        return super.getAllTasks();
    }

    @Override
    public void clearTasks() {
        save();
        super.clearTasks();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public void durationEpic(Task task) {
        super.durationEpic(task);
        save();
    }

    @Override
    public void setStartAndEndTimeEpic(Task task) {
        super.setStartAndEndTimeEpic(task);
        save();
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        save();
        return super.getPrioritizedTasks();
    }

    @Override
    public Task createTask(Task task) {
        Task create = super.createTask(task);
        save();
        return create;
    }

    @Override
    public Task updateTask(Task taskUpdate) {
        save();
        return super.updateTask(taskUpdate);
    }

    @Override
    public Task removeTask(int id) {
        save();
        return super.removeTask(id);
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        save();
        return super.getAllEpics();
    }

    @Override
    public void clearEpics() {
        save();
        super.clearEpics();
    }

    @Override
    public Epic getEpic(int epicId) {
        save();
        return super.getEpic(epicId);
    }

    @Override
    public Epic createEpic(Epic newEpic) {
        Epic createEpic = super.createEpic(newEpic);
        save();
        return createEpic;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        save();
        return super.updateEpic(epic);
    }

    @Override
    public void removeEpic(int epicId) {
        save();
        super.removeEpic(epicId);
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        save();
        return super.getAllSubtasks();
    }

    @Override
    public Subtask removeSubtask(int subTaskId) {
        save();
        return super.removeSubtask(subTaskId);
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        Subtask creteSub = super.createSubtask(subtask);
        save();
        return creteSub;
    }

    @Override
    public void updateSubtask(Subtask updateSubtask) {
        save();
        super.updateSubtask(updateSubtask);
    }

    @Override
    public Subtask getSubtask(int subTaskId) {
        save();
        return super.getSubtask(subTaskId);
    }

    @Override
    public void clearAllSubtasks() {
        save();
        super.clearAllSubtasks();
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(int epicId) {
        save();
        return super.getEpicSubtasks(epicId);
    }

    @Override
    public void findEpicStatus(Epic epic) {
        super.findEpicStatus(epic);
        save();
    }

    // Метод сохранения задачи в строку
    public String toString(Task task) {
        return task.toString();
    }

    // Метод создания задачи из строки в файл
    public Task fromString(String value) {
        String[] asString = value.split(",");
        String dataTime = asString[6];
        String time = asString[7];
        Integer data = Integer.valueOf(dataTime.substring(0, 2));
        Integer month = Integer.valueOf(dataTime.substring(3, 5));
        Integer year = Integer.valueOf(dataTime.substring(6, 10));
        Integer hour = Integer.valueOf(time.substring(1, 3));
        Integer minute = Integer.valueOf(time.substring(4, 6));
        Task taskFromString = null;
        switch (asString[1]) {
            case "TASK":
                taskFromString = new Task(asString[2], asString[4], Status.valueOf(asString[3]), Integer.valueOf(asString[5]),
                        LocalDateTime.of(year, month, data, hour, minute));
                taskFromString.setId(Integer.valueOf(asString[0]));
                break;
            case "EPIC":
                taskFromString = new Epic(asString[2], asString[4], Status.valueOf(asString[3]), Integer.valueOf(asString[5]),
                        LocalDateTime.of(year, month, data, hour, minute));
                taskFromString.setId(Integer.valueOf(asString[0]));
                break;
            case "SUBTASK":
                taskFromString = new Subtask(asString[2], asString[4], Status.valueOf(asString[3]), Integer.valueOf(asString[5]),
                        LocalDateTime.of(year, month, data, hour, minute), Integer.valueOf(asString[5]));
                taskFromString.setId(Integer.valueOf(asString[0]));
                break;
        }
        return taskFromString;
    }

    //Сохранения и восстановления менеджера истории из CSV
    private static String historyToString(HistoryManager manager) {
        StringBuilder sb = new StringBuilder("");
        for (Task history : manager.getHistory()) {
            sb.append(history.getId()).append(",");
        }
        return sb.toString();
    }

    //Сохранения и восстановления менеджера истории из CSV
    private static List<Integer> historyFromString(String value) {
        final String[] id = value.split(",");
        List<Integer> history = new ArrayList<>();
        for (String v : id) {
            history.add(Integer.valueOf(v));
        }
        return history;
    }

    // Сохранять текущее состояние менеджера в указанный файл
    public void save() {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.append("id,type,name,status,description,epic, duration, startTime, endTime\n");

            for (Map.Entry<Integer, Task> TaskEntry : tableTask.entrySet()) {
                writer.append(toString(TaskEntry.getValue()));
                writer.newLine();
            }

            for (Map.Entry<Integer, Epic> epicEntry : tableEpic.entrySet()) {
                writer.append(toString(epicEntry.getValue()));
                writer.newLine();
            }

            for (Map.Entry<Integer, Subtask> subtaskEntry : tableSubtask.entrySet()) {
                writer.append(toString(subtaskEntry.getValue()));
                writer.newLine();
            }

            writer.newLine();
            writer.append(historyToString(historyManager));

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка в файле"); // свой Эксепшен
        }
    }

    private void load() {
        int maxId = 0;
        try (final BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line = reader.readLine();

            while (line != null) {
                line = reader.readLine();
                if (line == null || !line.contains(",")) {
                    break;
                }

                final Integer id = fromString(line).getId();

                if (fromString(line).getType() == TypeTask.TASK) {
                    tableTask.put(id, fromString(line));
                } else if (fromString(line).getType() == TypeTask.EPIC) {
                    tableEpic.put(id, (Epic) fromString(line));
                } else if (fromString(line).getType() == TypeTask.SUBTASK) {
                    tableSubtask.put(id, (Subtask) fromString(line));
                }

                if (maxId < id) {
                    maxId = id;
                }
            }
            line = reader.readLine();
            if (line != null) {
                historyFromString(line);
                List<Integer> history = historyFromString(line);
                for (Integer taskId : history) {
                    if (tableTask.containsKey(taskId)) {
                        getTask(taskId);
                    } else if (tableEpic.containsKey(taskId)) {
                        getEpic(taskId);
                    } else if (tableSubtask.containsKey(taskId)) {
                        getSubtask(taskId);
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка в файле"); // свой Эксепшен
        }
        newId = maxId;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        final FileBackedTasksManager manager = new FileBackedTasksManager(file);
        manager.load();
        return manager;
    }

    public static void main(String[] args) {
        File file = new File("file.csv");
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);

        Task newTask = new Task("Задача 1", "Описание задачи 1", Status.NEW, 25,
                LocalDateTime.of(2022, 9, 21, 13, 30));
        Task newTask2 = new Task("Задача 2", "Описание задачи 2", Status.NEW, 30,
                LocalDateTime.of(2022, 9, 22, 12, 0));

        fileBackedTasksManager.createTask(newTask);
        fileBackedTasksManager.createTask(newTask2);

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1", Status.NEW, 0,
                LocalDateTime.of(2000, 1, 1, 0, 0));
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2", Status.NEW, 111,
                LocalDateTime.of(2000, 1, 1, 0, 0));
        fileBackedTasksManager.createEpic(epic1);
        fileBackedTasksManager.createEpic(epic2);

        Subtask subtaskEpic1 = new Subtask("Подзадача 1 эпика 1", "Описание подзадачи 1",
                Status.DONE, 35, LocalDateTime.of(2022, 9, 22, 12, 0), 3);
        Subtask subtaskEpic2A = new Subtask("Подзадача 2 эпика 2", "Описание подзадачи 2", Status.NEW,
                20, LocalDateTime.of(2022, 9, 20, 10, 0), 4);
        Subtask subtaskEpic2B = new Subtask("Подзадача 3 эпика 2", "Описание подзадачи 3", Status.DONE,
                25, LocalDateTime.of(2022, 9, 18, 11, 0), 4);
        fileBackedTasksManager.createSubtask(subtaskEpic1);
        fileBackedTasksManager.createSubtask(subtaskEpic2A);
        fileBackedTasksManager.createSubtask(subtaskEpic2B);

        System.out.println("Получаем задачи");
        System.out.println(fileBackedTasksManager.getAllTasks());

        System.out.println("Получаем эпики");
        System.out.println(fileBackedTasksManager.getAllEpics());

        System.out.println("Получаем субтаски");
        System.out.println(fileBackedTasksManager.getAllSubtasks());

        System.out.println("Получаем историю");
        fileBackedTasksManager.getTask(1);
        fileBackedTasksManager.getEpic(3);
        fileBackedTasksManager.getEpic(4);
        fileBackedTasksManager.getSubtask(5);
        System.out.println(fileBackedTasksManager.getHistory());

        FileBackedTasksManager loadManager = FileBackedTasksManager.loadFromFile(file);
        System.out.println("Проверяем загрузку:");
        System.out.println("Получаем историю");

        System.out.println(loadManager.getHistory());
        System.out.println(loadManager.getTask(2));
        System.out.println(loadManager.getSubtask(6));
        System.out.println(loadManager.getEpic(4));
//
//
//        Task newTaskManager = new Task("Задача 1", "Описание задачи 1", Status.NEW);
//        Task newTask2Manager = new Task("Задача 2", "Описание задачи 2", Status.NEW);
//        Subtask subtaskEpic1Manager = new Subtask("Субтаск 1", "Описание субтаска 1",
//                Status.NEW, 3);
//        Subtask subtaskEpic2AManager = new Subtask("Субтаск 2", "Описание субтаска 2", Status.NEW,
//                4);
//        Epic epic1Manager = new Epic("Эпик 1", "Описание эпика 1", Status.NEW);
//        Epic epic2Manager = new Epic("Эпик 2", "Описание эпика 2", Status.NEW);
//
//        TaskManager taskManager = Managers.getDefault();
//
//        taskManager.createTask(newTaskManager);
//        taskManager.createTask(newTask2Manager);
//        taskManager.getAllTasks();
//
//        taskManager.createEpic(epic1Manager);
//        taskManager.createEpic(epic2Manager);
//        taskManager.getAllEpics();
//
//        taskManager.createSubtask(subtaskEpic1Manager);
//        taskManager.createSubtask(subtaskEpic2AManager);
//        taskManager.getAllSubtasks();
//
//        taskManager.getTask(2);
//        taskManager.getEpic(4);
//        taskManager.getHistory();
//
//        System.out.println("Тесты:");
//        System.out.println(fileBackedTasksManager.getTask(1).equals(taskManager.getTask(1)));
//        System.out.println(fileBackedTasksManager.getTask(2).equals(taskManager.getTask(2)));
//        System.out.println(fileBackedTasksManager.getEpic(3).equals(taskManager.getEpic(3)));
//        System.out.println(fileBackedTasksManager.getEpic(4).equals(taskManager.getEpic(4)));
//        System.out.println(fileBackedTasksManager.getSubtask(5).equals(taskManager.getSubtask(5)));
//        System.out.println(fileBackedTasksManager.getSubtask(6).equals(taskManager.getSubtask(6)));
    }
}
