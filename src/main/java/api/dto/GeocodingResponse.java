package api.dto;

import java.util.List;

/** Wire format of the Open-Meteo geocoding search endpoint. */
public record GeocodingResponse(List<ResultDto> results) {

    public record ResultDto(String name, double latitude, double longitude, String country, String admin1) {
    }
}
