package persistence;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Opens JDBC connections. Resolution order per setting: environment variable,
 * then config.properties on the classpath, then defaults matching docker-compose.yml.
 */
public final class ConnectionFactory {

    private static final Properties FILE_PROPS = loadFile();

    private ConnectionFactory() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                resolve("DB_URL", "db.url", "jdbc:mysql://localhost:3306/weatherdb"),
                resolve("DB_USER", "db.user", "weather"),
                resolve("DB_PASSWORD", "db.password", "weather"));
    }

    private static String resolve(String envVar, String propKey, String defaultValue) {
        String env = System.getenv(envVar);
        if (env != null && !env.isBlank()) {
            return env;
        }
        return FILE_PROPS.getProperty(propKey, defaultValue);
    }

    private static Properties loadFile() {
        Properties props = new Properties();
        try (InputStream in = ConnectionFactory.class.getResourceAsStream("/config.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Falha ao ler config.properties", e);
        }
        return props;
    }
}
