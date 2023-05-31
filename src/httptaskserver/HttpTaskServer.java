package httptaskserver;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import httptaskserver.httphandler.EpicHandler;
import httptaskserver.httphandler.HistoryHandler;
import httptaskserver.httphandler.SubtaskHandler;
import httptaskserver.httphandler.TaskHandler;
import manager.Managers;
import manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private HttpServer httpServer;
    private Gson gson;
    private TaskManager taskManager;

    public HttpTaskServer() throws IOException {
        taskManager = Managers.getDefault();
        gson = new Gson();
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks/task/", new TaskHandler(taskManager, gson));
        httpServer.createContext("/tasks/subtask/", new SubtaskHandler(taskManager, gson));
        httpServer.createContext("/tasks/epic/", new EpicHandler(taskManager, gson));
        httpServer.createContext("/tasks/history/", new HistoryHandler(taskManager, gson));
    }

    public void start() {
        System.out.println("Сервер запустился на порту " + PORT + " по ссылке http://localhost:" + PORT);
        httpServer.start();
    }

    public void stop() {
        System.out.println("Сервер завершил свою работу");
        httpServer.stop(1);
    }
}
