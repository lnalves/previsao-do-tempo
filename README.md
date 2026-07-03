# Weather Forecast

[![CI](https://github.com/lnalves/previsao-do-tempo/actions/workflows/ci.yml/badge.svg)](https://github.com/lnalves/previsao-do-tempo/actions/workflows/ci.yml)

Java Swing desktop app that tracks current weather and 7-day forecasts for user-saved locations, powered by the [Open-Meteo API](https://open-meteo.com). Every refresh stores an observed **weather reading** and upserts the **daily forecasts**, building a queryable history over time.

## Features

- Current conditions (temperature, feels-like, precipitation) and 7-day forecast per city
- Add cities by name via the Open-Meteo geocoding API — no manual coordinates needed
- Reading history with date filters, joined with the forecast that existed for each day
- All data persisted in MySQL

## Architecture

```
ui ──► service ──► api          (Open-Meteo HTTP clients + DTOs)
              └──► persistence  (DAOs, one short-lived connection per operation)
                        │
                     domain     (records: Location, WeatherReading, DailyForecast)
```

- **Java 21**, Swing (background work on `SwingWorker`, never on the EDT)
- **MySQL 8** with unique constraints backing idempotent upserts
- **Gson** for JSON, `java.net.http.HttpClient` for HTTP
- See [CONTEXT.md](CONTEXT.md) for the domain glossary and [docs/adr](docs/adr) for decisions

## Quickstart

Requires JDK 21+, Maven and Docker.

```bash
docker compose up -d     # MySQL with schema and seed data
mvn package              # build + unit tests
java -jar target/weather-forecast.jar
```

Database credentials default to the ones in `docker-compose.yml`; override via environment variables (`DB_URL`, `DB_USER`, `DB_PASSWORD`) or a `config.properties` (see `src/main/resources/config.properties.example`).

## Tests

```bash
mvn verify
```

Unit tests cover JSON parsing and service rules; integration tests spin up a real MySQL via Testcontainers (Docker required) and exercise the DAOs against the same `init.sql` the app uses.

## Known limitations

- Timezone is fixed to `America/Sao_Paulo`: readings for foreign cities are shown and stored in São Paulo time.

## Roadmap

- Temperature charts in the history view
- Weather-condition icons (`weather_code`)
- Auto-refresh timer
- `timezone=auto` for correct local times worldwide
