package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AppConfigDao {

    public Optional<String> find(String key) throws SQLException {
        String sql = "SELECT config_value FROM app_config WHERE config_key = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, key);
            try (ResultSet rs = st.executeQuery()) {
                return rs.next() ? Optional.ofNullable(rs.getString(1)) : Optional.empty();
            }
        }
    }

    public void save(String key, String value) throws SQLException {
        String sql = "INSERT INTO app_config (config_key, config_value) VALUES (?, ?) AS new "
                + "ON DUPLICATE KEY UPDATE config_value = new.config_value";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, key);
            st.setString(2, value);
            st.executeUpdate();
        }
    }
}
