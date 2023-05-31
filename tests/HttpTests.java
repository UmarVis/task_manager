import com.google.gson.Gson;
import httptaskserver.HttpTaskServer;
import httptaskserver.KVServer;
import manager.HttpTaskManager;
import manager.Managers;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import util.Status;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTests {

    HttpClient client = HttpClient.newHttpClient();
    private Gson gson;

    KVServer kvServer;
    HttpTaskServer httpTaskServer;

    void start() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
    }

    @Test
    void saveEndpointsByHttpAndCheckRequests() throws IOException, InterruptedException {
        start();
        HttpTaskManager manager = (HttpTaskManager) Managers.getDefault();
        Task task = new Task(1, "task1", "Desc1", Status.NEW, 25,
                LocalDateTime.of(2022, 6, 9, 3, 0));
        Epic epic1 = new Epic(2, "epic1", "Desc1", Status.NEW, 25,
                LocalDateTime.of(2022, 6, 9, 3, 0));
        Subtask subtask1 = new Subtask(3, "subtask1", "Desc1", Status.NEW, 30,
                LocalDateTime.of(2022, 6, 9, 5, 0), 2);
        Subtask subtask2 = new Subtask(4, "subtask2", "Desc2", Status.NEW,
                45, LocalDateTime.of(2022, 6, 9, 9, 0), 2);


        gson = new Gson();
        URI epicUri = URI.create("http://localhost:8080/tasks/epic/");
        URI subtasksUri = URI.create("http://localhost:8080/tasks/subtask/");
        URI taskUri = URI.create("http://localhost:8080/tasks/task/");
        manager.createTask(task);
        manager.createEpic(epic1);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        String taskJson = gson.toJson(task);
        String epicJson1 = gson.toJson(epic1);
        String subtaskJson1 = gson.toJson(subtask1);
        String subtaskJson2 = gson.toJson(subtask2);

        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(taskJson);
        HttpRequest request = HttpRequest.newBuilder().uri(taskUri).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        final HttpRequest.BodyPublisher bodyEpic1 = HttpRequest.BodyPublishers.ofString(epicJson1);
        HttpRequest requestEpic1 = HttpRequest.newBuilder().uri(epicUri).POST(bodyEpic1).build();
        HttpResponse<String> responseEpic1 = client.send(requestEpic1, HttpResponse.BodyHandlers.ofString());

        final HttpRequest.BodyPublisher bodySubtask1 = HttpRequest.BodyPublishers.ofString(subtaskJson1);
        HttpRequest requestSubtask1 = HttpRequest.newBuilder().uri(subtasksUri).POST(bodySubtask1).build();
        HttpResponse<String> responseSubtask1 = client.send(requestSubtask1, HttpResponse.BodyHandlers.ofString());

        final HttpRequest.BodyPublisher bodySubtask2 = HttpRequest.BodyPublishers.ofString(subtaskJson2);
        HttpRequest requestSubtask2 = HttpRequest.newBuilder().uri(subtasksUri).POST(bodySubtask2).build();
        HttpResponse<String> responseSubtask2 = client.send(requestSubtask2, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(200, responseEpic1.statusCode());
        assertEquals(200, responseSubtask1.statusCode());
        assertEquals(200, responseSubtask2.statusCode());
    }
}
