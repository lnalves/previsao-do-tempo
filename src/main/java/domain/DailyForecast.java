package domain;

import java.time.LocalDate;

/** A prediction for a date — superseded by newer fetches for the same date. */
public record DailyForecast(LocalDate date, double temperatureMax, double temperatureMin,
                            double precipitationProbability) {
}
