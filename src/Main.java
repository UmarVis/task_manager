import httptaskserver.HttpTaskServer;
import httptaskserver.KVServer;
import manager.HttpTaskManager;
import tasks.Task;
import util.Status;

import java.io.IOException;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws IOException {

        KVServer kvServer = new KVServer();
        kvServer.start();
        HttpTaskServer server = new HttpTaskServer();
        server.start();

        Task task = new Task(1, "Задача 1", "Описание задачи 1", Status.NEW, 25,
                LocalDateTime.of(2022, 10, 1, 12, 0));
        HttpTaskManager manager = new HttpTaskManager("http://localhost:8078/");
        manager.createTask(task);
        System.out.println(manager.getTask(task.getId()));
        System.out.println("History: ");
        System.out.println(manager.getHistory());
        System.out.println("Loading:...");
        HttpTaskManager manager1 = new HttpTaskManager("http://localhost:8078/");
        manager1.load();
        System.out.println(manager1.getAllTasks());
        System.out.println("History: ");
        System.out.println(manager1.getHistory());
        server.stop();
        kvServer.stop();
    }
}
