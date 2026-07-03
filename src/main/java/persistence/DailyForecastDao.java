package persistence;

import domain.DailyForecast;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DailyForecastDao {

    /** A forecast is superseded by newer fetches for the same date, hence the upsert. */
    public void upsertAll(int locationId, List<DailyForecast> forecasts) throws SQLException {
        String sql = "INSERT INTO daily_forecast (location_id, forecast_date, temperature_max, temperature_min, precipitation_probability) "
                + "VALUES (?, ?, ?, ?, ?) AS new "
                + "ON DUPLICATE KEY UPDATE temperature_max = new.temperature_max, "
                + "temperature_min = new.temperature_min, precipitation_probability = new.precipitation_probability";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            for (DailyForecast forecast : forecasts) {
                st.setInt(1, locationId);
                st.setDate(2, Date.valueOf(forecast.date()));
                st.setDouble(3, forecast.temperatureMax());
                st.setDouble(4, forecast.temperatureMin());
                st.setDouble(5, forecast.precipitationProbability());
                st.addBatch();
            }
            st.executeBatch();
        }
    }
}
