package service;

import api.ForecastResult;
import api.OpenMeteoClient;
import domain.Location;
import domain.ReadingHistoryEntry;
import persistence.AppConfigDao;
import persistence.DailyForecastDao;
import persistence.WeatherReadingDao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/** Orchestrates fetching weather data and persisting it. */
public class WeatherService {

    public static final String API_BASE_URL_KEY = "API_BASE_URL";

    private final AppConfigDao appConfigDao;
    private final WeatherReadingDao readingDao;
    private final DailyForecastDao forecastDao;

    public WeatherService() {
        this(new AppConfigDao(), new WeatherReadingDao(), new DailyForecastDao());
    }

    public WeatherService(AppConfigDao appConfigDao, WeatherReadingDao readingDao, DailyForecastDao forecastDao) {
        this.appConfigDao = appConfigDao;
        this.readingDao = readingDao;
        this.forecastDao = forecastDao;
    }

    /** Fetches current weather and forecasts, stores both, and returns the result for display. */
    public ForecastResult fetchAndStore(Location location) throws Exception {
        ForecastResult result = newClient().fetch(location.latitude(), location.longitude());
        readingDao.insert(location.id(), result.reading());
        forecastDao.upsertAll(location.id(), result.dailyForecasts());
        return result;
    }

    public List<ReadingHistoryEntry> getHistory(Location location, LocalDate from, LocalDate to) throws SQLException {
        return readingDao.findHistory(location.id(), from, to);
    }

    public String getApiBaseUrl() throws SQLException {
        return appConfigDao.find(API_BASE_URL_KEY).orElse(OpenMeteoClient.DEFAULT_BASE_URL);
    }

    public void saveApiBaseUrl(String url) throws SQLException {
        appConfigDao.save(API_BASE_URL_KEY, url);
    }

    protected OpenMeteoClient newClient() throws SQLException {
        return new OpenMeteoClient(getApiBaseUrl());
    }
}
