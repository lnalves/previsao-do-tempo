package service;

import api.ForecastResult;
import api.OpenMeteoClient;
import domain.DailyForecast;
import domain.Location;
import domain.WeatherReading;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import persistence.AppConfigDao;
import persistence.DailyForecastDao;
import persistence.WeatherReadingDao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private AppConfigDao appConfigDao;
    @Mock
    private WeatherReadingDao readingDao;
    @Mock
    private DailyForecastDao forecastDao;
    @Mock
    private OpenMeteoClient client;

    private WeatherService serviceWithMockedClient() {
        return new WeatherService(appConfigDao, readingDao, forecastDao) {
            @Override
            protected OpenMeteoClient newClient() {
                return client;
            }
        };
    }

    @Test
    void fetchAndStorePersistsReadingAndForecasts() throws Exception {
        Location location = new Location(7, "Curitiba", -25.43, -49.27);
        ForecastResult result = new ForecastResult(
                new WeatherReading(LocalDateTime.now(), 20.0, 21.0, 0.0),
                List.of(new DailyForecast(LocalDate.now(), 25.0, 12.0, 40.0)));
        when(client.fetch(location.latitude(), location.longitude())).thenReturn(result);

        ForecastResult returned = serviceWithMockedClient().fetchAndStore(location);

        assertSame(result, returned);
        verify(readingDao).insert(7, result.reading());
        verify(forecastDao).upsertAll(7, result.dailyForecasts());
    }

    @Test
    void apiBaseUrlFallsBackToDefaultWhenUnconfigured() throws Exception {
        when(appConfigDao.find(WeatherService.API_BASE_URL_KEY)).thenReturn(Optional.empty());

        assertEquals(OpenMeteoClient.DEFAULT_BASE_URL,
                new WeatherService(appConfigDao, readingDao, forecastDao).getApiBaseUrl());
    }
}
