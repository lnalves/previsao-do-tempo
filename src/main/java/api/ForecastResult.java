package api;

import domain.DailyForecast;
import domain.WeatherReading;

import java.util.List;

/** What one fetch produces: the observed reading plus the forecasts for the coming days. */
public record ForecastResult(WeatherReading reading, List<DailyForecast> dailyForecasts) {
}
