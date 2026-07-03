package domain;

/** A user-saved place with a name and geographic coordinates that weather data is fetched for. */
public record Location(int id, String name, double latitude, double longitude) {

    @Override
    public String toString() {
        return String.format("%s (%.4f, %.4f)", name, latitude, longitude);
    }
}
