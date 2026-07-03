package domain;

import java.time.LocalDateTime;

/** An observed weather condition captured when the user refreshes. A fact — never changes once recorded. */
public record WeatherReading(LocalDateTime readAt, double temperature, double feelsLike, double precipitation) {
}
