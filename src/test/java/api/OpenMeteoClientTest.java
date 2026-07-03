package api;

import domain.DailyForecast;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OpenMeteoClientTest {

    private static String fixture(String name) throws IOException {
        try (InputStream in = OpenMeteoClientTest.class.getResourceAsStream("/fixtures/" + name)) {
            return new String(in.readAllBytes());
        }
    }

    @Test
    void parsesForecastResponseIntoDomain() throws IOException {
        ForecastResult result = OpenMeteoClient.toDomain(OpenMeteoClient.parse(fixture("forecast.json")));

        assertEquals(LocalDateTime.parse("2026-07-03T13:45"), result.reading().readAt());
        assertEquals(20.5, result.reading().temperature());
        assertEquals(21.1, result.reading().feelsLike());
        assertEquals(0.2, result.reading().precipitation());

        assertEquals(2, result.dailyForecasts().size());
        DailyForecast first = result.dailyForecasts().get(0);
        assertEquals(LocalDate.parse("2026-07-03"), first.date());
        assertEquals(20.9, first.temperatureMax());
        assertEquals(11.4, first.temperatureMin());
        assertEquals(100.0, first.precipitationProbability());
    }
}
