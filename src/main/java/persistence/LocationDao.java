package persistence;

import domain.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LocationDao {

    public List<Location> findAll() throws SQLException {
        String sql = "SELECT id, name, latitude, longitude FROM location ORDER BY name";
        List<Location> locations = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                locations.add(new Location(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude")));
            }
        }
        return locations;
    }

    public void insert(Location location) throws SQLException {
        String sql = "INSERT INTO location (name, latitude, longitude) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, location.name());
            st.setDouble(2, location.latitude());
            st.setDouble(3, location.longitude());
            st.executeUpdate();
        }
    }
}
