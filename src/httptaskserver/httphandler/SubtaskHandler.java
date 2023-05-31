package httptaskserver.httphandler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import tasks.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class SubtaskHandler implements HttpHandler {

    private final TaskManager taskManager;
    private final Gson gson;
    private static final Charset UTF = StandardCharsets.UTF_8;


    public SubtaskHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (method.equals("GET")) {
            requestGet(exchange);
        } else if (method.equals("POST")) {
            requestPost(exchange);
        } else if (method.equals("DELETE")) {
            requestDelete(exchange);
        }
    }

    private void requestDelete(HttpExchange exchange) throws IOException {
        URI uri = exchange.getRequestURI();
        String path = exchange.getRequestURI().getPath();
        if (path.equals("/tasks/subtask/") && uri.getQuery() == null) {
            taskManager.clearAllSubtasks();
            String response = "Все подзадачи удалены";
            exchange.sendResponseHeaders(200, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes(UTF));
            }
        } else if (uri.getQuery() != null) {
            taskManager.removeSubtask(getIdUri(uri));
            String response = "Подзадача с ИД " + getIdUri(uri) + " удалена";
            exchange.sendResponseHeaders(200, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes(UTF));
            }
        }
    }

    private void requestPost(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), UTF);
        Subtask subtask = gson.fromJson(body, Subtask.class);
        if (subtask.getId() != 0) {
            taskManager.updateSubtask(subtask);
            exchange.sendResponseHeaders(200, 0);
            String response = "Подзадача обновлена";
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes(UTF));
            }
        } else {
            taskManager.createSubtask(subtask);
            String response = "Подзадача добавлена";
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes(UTF));
            }
        }
    }

    private void requestGet(HttpExchange exchange) throws IOException {
        URI uri = exchange.getRequestURI();
        String path = exchange.getRequestURI().getPath();
        if (path.equals("/tasks/subtask/") && uri.getQuery() == null) {
            String response = gson.toJson(taskManager.getAllSubtasks());
            exchange.sendResponseHeaders(200, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes(UTF));
            }
        } else if (uri.getQuery() != null) {
            String response = gson.toJson(taskManager.getSubtask(getIdUri(uri)));
            if (response.equals(null)) {
                exchange.sendResponseHeaders(400, 0);
            }
            exchange.sendResponseHeaders(200, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes(UTF));
            }
        }
    }

    private int getIdUri(URI uri) {
        String idUri = uri.getQuery();
        return Integer.parseInt(idUri.split("=")[1]);
    }
}
