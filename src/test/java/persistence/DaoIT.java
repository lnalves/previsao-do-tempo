package persistence;

import domain.DailyForecast;
import domain.Location;
import domain.ReadingHistoryEntry;
import domain.WeatherReading;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Integration tests against a real MySQL running the same init.sql the app uses. */
@Testcontainers
class DaoIT {

    @Container
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8")
            .withDatabaseName("weatherdb")
            .withCopyFileToContainer(MountableFile.forHostPath("db/init.sql"),
                    "/docker-entrypoint-initdb.d/init.sql");

    @BeforeAll
    static void pointConnectionFactoryAtContainer() {
        System.setProperty("db.url", MYSQL.getJdbcUrl());
        System.setProperty("db.user", MYSQL.getUsername());
        System.setProperty("db.password", MYSQL.getPassword());
    }

    private static Location findByName(String name) throws Exception {
        var all = new LocationDao().findAll();
        return all.stream()
                .filter(l -> l.name().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Nomes no banco: " + all));
    }

    @Test
    void insertsAndListsLocations() throws Exception {
        LocationDao dao = new LocationDao();
        int before = dao.findAll().size();

        dao.insert(new Location(0, "Teste City", -10.0, -50.0));

        List<Location> after = dao.findAll();
        assertEquals(before + 1, after.size());
        assertTrue(after.stream().anyMatch(l -> l.name().equals("Teste City")));
    }

    @Test
    void forecastUpsertKeepsOneRowPerDateAndUpdatesValues() throws Exception {
        Location location = findByName("Curitiba");
        LocalDate date = LocalDate.of(2026, 7, 3);
        LocalDateTime readAt = date.atTime(10, 0);

        new WeatherReadingDao().insert(location.id(), new WeatherReading(readAt, 20.0, 21.0, 0.0));
        DailyForecastDao forecastDao = new DailyForecastDao();
        forecastDao.upsertAll(location.id(), List.of(new DailyForecast(date, 25.0, 12.0, 40.0)));
        forecastDao.upsertAll(location.id(), List.of(new DailyForecast(date, 27.0, 13.0, 60.0)));

        List<ReadingHistoryEntry> history = new WeatherReadingDao().findHistory(location.id(), date, date);
        assertEquals(1, history.size());
        assertEquals(27.0, history.get(0).dayMax());
    }

    @Test
    void readingInsertIsIdempotentForSameTimestamp() throws Exception {
        Location location = findByName("São Paulo");
        LocalDateTime readAt = LocalDateTime.of(2026, 7, 4, 15, 30);

        WeatherReadingDao dao = new WeatherReadingDao();
        dao.insert(location.id(), new WeatherReading(readAt, 18.0, 17.5, 0.0));
        dao.insert(location.id(), new WeatherReading(readAt, 18.2, 17.8, 0.1));

        List<ReadingHistoryEntry> history = dao.findHistory(location.id(),
                readAt.toLocalDate(), readAt.toLocalDate());
        assertEquals(1, history.size());
        assertEquals(18.2, history.get(0).temperature());
    }

    @Test
    void historyDateFiltersExcludeOutOfRangeReadings() throws Exception {
        Location location = findByName("Rio de Janeiro");
        WeatherReadingDao dao = new WeatherReadingDao();
        dao.insert(location.id(), new WeatherReading(LocalDateTime.of(2026, 1, 1, 12, 0), 30.0, 33.0, 0.0));
        dao.insert(location.id(), new WeatherReading(LocalDateTime.of(2026, 2, 1, 12, 0), 31.0, 34.0, 0.0));

        List<ReadingHistoryEntry> january = dao.findHistory(location.id(),
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31));
        assertEquals(1, january.size());
        assertEquals(30.0, january.get(0).temperature());

        assertEquals(2, dao.findHistory(location.id(), null, null).size());
    }

    @Test
    void appConfigSaveAndFindRoundTrip() throws Exception {
        AppConfigDao dao = new AppConfigDao();
        assertEquals("https://api.open-meteo.com/v1/forecast", dao.find("API_BASE_URL").orElseThrow());

        dao.save("API_BASE_URL", "http://localhost:9999");
        assertEquals("http://localhost:9999", dao.find("API_BASE_URL").orElseThrow());
        assertTrue(dao.find("NAO_EXISTE").isEmpty());
    }
}
