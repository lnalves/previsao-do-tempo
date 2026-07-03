package api;

import api.dto.ForecastResponse;
import com.google.gson.Gson;
import domain.DailyForecast;
import domain.WeatherReading;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OpenMeteoClient {

    public static final String DEFAULT_BASE_URL = "https://api.open-meteo.com/v1/forecast";

    private static final Gson GSON = new Gson();
    private static final HttpClient HTTP = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private final String baseUrl;

    public OpenMeteoClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public ForecastResult fetch(double latitude, double longitude) throws IOException, InterruptedException {
        String url = String.format(Locale.US,
                "%s?latitude=%.4f&longitude=%.4f"
                        + "&daily=temperature_2m_max,temperature_2m_min,precipitation_probability_max"
                        + "&current=temperature_2m,precipitation,apparent_temperature"
                        + "&timezone=America/Sao_Paulo",
                baseUrl, latitude, longitude);

        HttpRequest request = HttpRequest.newBuilder(URI.create(url)).GET().build();
        HttpResponse<String> response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("Falha na requisição à Open-Meteo: HTTP " + response.statusCode()
                    + " — " + response.body());
        }
        return toDomain(parse(response.body()));
    }

    static ForecastResponse parse(String json) {
        return GSON.fromJson(json, ForecastResponse.class);
    }

    static ForecastResult toDomain(ForecastResponse dto) {
        WeatherReading reading = new WeatherReading(
                LocalDateTime.parse(dto.current().time()),
                dto.current().temperature(),
                dto.current().feelsLike(),
                dto.current().precipitation());

        List<DailyForecast> forecasts = new ArrayList<>();
        ForecastResponse.DailyDto daily = dto.daily();
        for (int i = 0; i < daily.time().length; i++) {
            forecasts.add(new DailyForecast(
                    LocalDate.parse(daily.time()[i]),
                    daily.temperatureMax()[i],
                    daily.temperatureMin()[i],
                    daily.precipitationProbabilityMax()[i]));
        }
        return new ForecastResult(reading, forecasts);
    }
}
