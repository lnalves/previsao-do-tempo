package api.dto;

import com.google.gson.annotations.SerializedName;

/** Wire format of the Open-Meteo /v1/forecast endpoint. */
public record ForecastResponse(CurrentDto current, DailyDto daily) {

    public record CurrentDto(String time,
                             @SerializedName("temperature_2m") double temperature,
                             @SerializedName("apparent_temperature") double feelsLike,
                             double precipitation) {
    }

    public record DailyDto(String[] time,
                           @SerializedName("temperature_2m_max") double[] temperatureMax,
                           @SerializedName("temperature_2m_min") double[] temperatureMin,
                           @SerializedName("precipitation_probability_max") double[] precipitationProbabilityMax) {
    }
}
