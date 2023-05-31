package httptaskserver;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TaskClient {
    private HttpClient httpClient;
    private String url;
    private String token;
    private URI uri;

    public TaskClient(String url) {
        this.url = url;
        httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "register"))
                .build();
        HttpResponse.BodyHandler<String> stringBodyHandler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = httpClient.send(request, stringBodyHandler);
            if (response.statusCode() == 200) {
                this.token = response.body();
            } else {
                throw new RuntimeException("Ошибка");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String load(String key) throws IOException, InterruptedException {
        if (key == null) {
            return "null";
        }
        uri = URI.create(url + "load/" + key + "?API_TOKEN=" + token);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(request, handler);
        String jsonStr = "";
        if (response.statusCode() == 200) {
            JsonElement jsonElement = JsonParser.parseString(response.body());

            if (jsonElement.isJsonArray()) {
                JsonArray jsonArray = jsonElement.getAsJsonArray();
                jsonStr = jsonArray.toString();
            }
            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                jsonStr = jsonObject.toString();
            }
        } else {
            throw new RuntimeException("Ошибка");
        }
        return jsonStr;
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        uri = URI.create(url + "save/" + key + "?API_TOKEN=" + token);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(request, handler);
        if (response.statusCode() == 200) {
            System.out.println("Код состояния " + response.statusCode());
        } else {
            throw new RuntimeException("Ошибка");
        }
    }
}

