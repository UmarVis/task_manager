package httptaskserver.httphandler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import tasks.Epic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class EpicHandler implements HttpHandler {

    private final TaskManager taskManager;
    private final Gson gson;
    private static final Charset UTF = StandardCharsets.UTF_8;


    public EpicHandler(TaskManager taskManager, Gson gson) {
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
        if (path.equals("/tasks/epic/") && uri.getQuery() == null) {
            taskManager.clearEpics();
            String response = "Все эпики удалены";
            exchange.sendResponseHeaders(200, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes(UTF));
            }
        } else if (uri.getQuery() != null) {
            taskManager.removeEpic(getIdUri(uri));
            String response = "Эпик с ИД " + getIdUri(uri) + " удален";
            exchange.sendResponseHeaders(200, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes(UTF));
            }
        }
    }

    private void requestPost(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), UTF);
        Epic epic = gson.fromJson(body, Epic.class);
        if (epic.getId() != 0) {
            taskManager.updateEpic(epic);
            exchange.sendResponseHeaders(200, 0);
            String response = "Эпик обновлен";
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes(UTF));
            }
        } else {
            taskManager.createEpic(epic);
            String response = "Эпик добавлен";
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes(UTF));
            }
        }
    }

    private void requestGet(HttpExchange exchange) throws IOException {
        URI uri = exchange.getRequestURI();
        String path = exchange.getRequestURI().getPath();
        if (path.equals("/tasks/epic/") && uri.getQuery() == null) {
            String response = gson.toJson(taskManager.getAllEpics());
            exchange.sendResponseHeaders(200, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes(UTF));
            }
        } else if (uri.getQuery() != null) {
            String response = gson.toJson(taskManager.getEpic(getIdUri(uri)));
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
