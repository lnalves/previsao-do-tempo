package api;

import api.dto.GeocodingResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class GeocodingClientTest {

    @Test
    void parsesGeocodingResults() throws IOException {
        String json;
        try (InputStream in = getClass().getResourceAsStream("/fixtures/geocoding.json")) {
            json = new String(in.readAllBytes());
        }

        GeocodingResponse response = GeocodingClient.parse(json);

        assertEquals(1, response.results().size());
        GeocodingResponse.ResultDto result = response.results().get(0);
        assertEquals("Curitiba", result.name());
        assertEquals(-25.42778, result.latitude());
        assertEquals(-49.27306, result.longitude());
        assertEquals("Paraná", result.admin1());
    }

    @Test
    void emptyResponseParsesToNullResults() {
        assertNull(GeocodingClient.parse("{}").results());
    }
}
