-- Schema for the Weather Forecast app.
-- Runs automatically on first startup via docker-entrypoint-initdb.d.

-- The entrypoint's mysql client defaults to latin1; without this, accented
-- seed data (e.g. "São Paulo") is stored as mojibake.
SET NAMES utf8mb4;

CREATE TABLE location (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  latitude DECIMAL(10,6) NOT NULL,
  longitude DECIMAL(10,6) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- A Weather Reading is an observed condition captured when the user refreshes.
-- Refreshing twice within the same API time slot must not duplicate rows.
CREATE TABLE weather_reading (
  id INT NOT NULL AUTO_INCREMENT,
  location_id INT NOT NULL,
  read_at DATETIME NOT NULL,
  temperature DOUBLE DEFAULT NULL,
  feels_like DOUBLE DEFAULT NULL,
  precipitation DOUBLE DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_reading_location_time (location_id, read_at),
  CONSTRAINT fk_reading_location FOREIGN KEY (location_id) REFERENCES location (id)
) ENGINE=InnoDB;

-- A Daily Forecast is a prediction, superseded by newer fetches for the same date.
CREATE TABLE daily_forecast (
  id INT NOT NULL AUTO_INCREMENT,
  location_id INT NOT NULL,
  forecast_date DATE NOT NULL,
  temperature_max DOUBLE DEFAULT NULL,
  temperature_min DOUBLE DEFAULT NULL,
  precipitation_probability DOUBLE DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_forecast_location_date (location_id, forecast_date),
  CONSTRAINT fk_forecast_location FOREIGN KEY (location_id) REFERENCES location (id)
) ENGINE=InnoDB;

CREATE TABLE app_config (
  id INT NOT NULL AUTO_INCREMENT,
  config_key VARCHAR(100) NOT NULL,
  config_value VARCHAR(500) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_config_key (config_key)
) ENGINE=InnoDB;

INSERT INTO app_config (config_key, config_value)
VALUES ('API_BASE_URL', 'https://api.open-meteo.com/v1/forecast');

INSERT INTO location (name, latitude, longitude) VALUES
  ('São Paulo', -23.5475, -46.6361),
  ('Curitiba', -25.4284, -49.2733),
  ('Rio de Janeiro', -22.9064, -43.1822);
