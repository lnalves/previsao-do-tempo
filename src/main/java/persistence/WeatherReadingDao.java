package persistence;

import domain.ReadingHistoryEntry;
import domain.WeatherReading;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WeatherReadingDao {

    /** Refreshing twice within the same API time slot must not duplicate readings. */
    public void insert(int locationId, WeatherReading reading) throws SQLException {
        String sql = "INSERT INTO weather_reading (location_id, read_at, temperature, feels_like, precipitation) "
                + "VALUES (?, ?, ?, ?, ?) AS new "
                + "ON DUPLICATE KEY UPDATE temperature = new.temperature, "
                + "feels_like = new.feels_like, precipitation = new.precipitation";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, locationId);
            st.setTimestamp(2, Timestamp.valueOf(reading.readAt()));
            st.setDouble(3, reading.temperature());
            st.setDouble(4, reading.feelsLike());
            st.setDouble(5, reading.precipitation());
            st.executeUpdate();
        }
    }

    /** Readings enriched with the forecast that existed for each date; date bounds are optional. */
    public List<ReadingHistoryEntry> findHistory(int locationId, LocalDate from, LocalDate to) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT r.read_at, r.temperature, r.feels_like, f.temperature_max, f.temperature_min "
                        + "FROM weather_reading r "
                        + "LEFT JOIN daily_forecast f ON f.location_id = r.location_id AND f.forecast_date = DATE(r.read_at) "
                        + "WHERE r.location_id = ?");
        if (from != null) {
            sql.append(" AND r.read_at >= ?");
        }
        if (to != null) {
            sql.append(" AND r.read_at < ?");
        }
        sql.append(" ORDER BY r.read_at DESC");

        List<ReadingHistoryEntry> entries = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql.toString())) {
            int i = 1;
            st.setInt(i++, locationId);
            if (from != null) {
                st.setTimestamp(i++, Timestamp.valueOf(from.atStartOfDay()));
            }
            if (to != null) {
                st.setTimestamp(i++, Timestamp.valueOf(to.plusDays(1).atStartOfDay()));
            }
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    entries.add(new ReadingHistoryEntry(
                            rs.getTimestamp("read_at").toLocalDateTime(),
                            rs.getDouble("temperature"),
                            rs.getDouble("feels_like"),
                            (Double) rs.getObject("temperature_max"),
                            (Double) rs.getObject("temperature_min")));
                }
            }
        }
        return entries;
    }
}
