package api;

import api.dto.GeocodingResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

public class GeocodingClient {

    private static final String BASE_URL = "https://geocoding-api.open-meteo.com/v1/search";

    private static final Gson GSON = new Gson();
    private static final HttpClient HTTP = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public List<GeocodingResponse.ResultDto> search(String name) throws IOException, InterruptedException {
        String url = BASE_URL + "?name=" + URLEncoder.encode(name, StandardCharsets.UTF_8)
                + "&count=10&language=pt&format=json";
        HttpRequest request = HttpRequest.newBuilder(URI.create(url)).GET().build();
        HttpResponse<String> response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("Falha na busca de cidades: HTTP " + response.statusCode());
        }
        GeocodingResponse parsed = parse(response.body());
        return parsed.results() == null ? List.of() : parsed.results();
    }

    static GeocodingResponse parse(String json) {
        return GSON.fromJson(json, GeocodingResponse.class);
    }
}
