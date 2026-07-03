package service;

import domain.Location;
import persistence.LocationDao;

import java.sql.SQLException;
import java.util.List;

public class LocationService {

    private final LocationDao locationDao;

    public LocationService() {
        this(new LocationDao());
    }

    public LocationService(LocationDao locationDao) {
        this.locationDao = locationDao;
    }

    public List<Location> listAll() throws SQLException {
        return locationDao.findAll();
    }

    public void add(String name, double latitude, double longitude) throws SQLException {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("O nome da cidade é obrigatório.");
        }
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("Latitude deve estar entre -90 e 90.");
        }
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Longitude deve estar entre -180 e 180.");
        }
        locationDao.insert(new Location(0, name.trim(), latitude, longitude));
    }
}
