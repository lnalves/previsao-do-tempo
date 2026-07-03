package domain;

import java.time.LocalDateTime;

/** A Weather Reading enriched with the Daily Forecast that existed for that date (may be absent). */
public record ReadingHistoryEntry(LocalDateTime readAt, double temperature, double feelsLike,
                                  Double dayMax, Double dayMin) {
}
