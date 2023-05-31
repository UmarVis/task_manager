package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import httptaskserver.TaskClient;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {

    private String url;
    private TaskClient client;
    private Gson gson = getGson();

    public HttpTaskManager(String url) {
        this.client = new TaskClient(url);
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }

    public void load() {
        try {
            String jsonTask = client.load("TASK");
            final HashMap<Integer, Task> restoredTasks = gson.fromJson(jsonTask,
                    new TypeToken<HashMap<Integer, Task>>() {
                    }.getType());
            for (Map.Entry<Integer, Task> taskEntry : restoredTasks.entrySet()) {
                tableTask.put(taskEntry.getKey(), taskEntry.getValue());
            }
            String jsonEpic = client.load("EPIC");
            final HashMap<Integer, Epic> restoredEpics = gson.fromJson(jsonEpic,
                    new TypeToken<HashMap<Integer, Epic>>() {
                    }.getType());
            for (Map.Entry<Integer, Epic> taskEntry : restoredEpics.entrySet()) {
                tableEpic.put(taskEntry.getKey(), taskEntry.getValue());
            }
            String jsonSub = client.load("SUBTASK");
            final HashMap<Integer, Subtask> restoredSubs = gson.fromJson(jsonSub,
                    new TypeToken<HashMap<Integer, Subtask>>() {
                    }.getType());
            for (Map.Entry<Integer, Subtask> taskEntry : restoredSubs.entrySet()) {
                tableSubtask.put(taskEntry.getKey(), taskEntry.getValue());
            }
            String jsonHistory = client.load("HISTORY");
            final List<Integer> historyIds = gson.fromJson(jsonHistory, new TypeToken<List<Integer>>() {
            }.getType());
            for (int id : historyIds) {
                List<Task> allTasks = getAllTasks();
                List<Subtask> allSubtasks = getAllSubtasks();
                List<Epic> allEpics = getAllEpics();
                for (Task task : allTasks) {
                    if (id == task.getId()) {
                        historyManager.add(task);
                    }
                }
                for (Subtask subtask : allSubtasks) {
                    if (id == subtask.getId()) {
                        historyManager.add(subtask);
                    }
                }
                for (Epic epic : allEpics) {
                    if (id == epic.getId()) {
                        historyManager.add(epic);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Внимание ошибка.");
        } catch (InterruptedException e) {
            System.out.println("Внимание ошибка.");
        }
    }

    @Override
    public void save() {
        try {
            String jsonTask = gson.toJson(tableTask);
            client.put("TASK", jsonTask);
            String jsonEpic = gson.toJson(tableEpic);
            client.put("EPIC", jsonEpic);
            String jsonSub = gson.toJson(tableSubtask);
            client.put("SUBTASK", jsonSub);
            List<Task> history = getHistory();
            String jsonHistory = gson.toJson(history.stream().map(Task::getId).collect(Collectors.toList()));
            client.put("HISTORY", jsonHistory);
        } catch (IOException e) {
            System.out.println("Внимание ошибка.");
        } catch (InterruptedException e) {
            System.out.println("Внимание ошибка.");
        }
    }
}



