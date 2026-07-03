package service;

import domain.Location;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import persistence.LocationDao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private LocationDao locationDao;

    @Test
    void addTrimsNameAndInserts() throws Exception {
        new LocationService(locationDao).add("  Curitiba  ", -25.43, -49.27);

        ArgumentCaptor<Location> captor = ArgumentCaptor.forClass(Location.class);
        verify(locationDao).insert(captor.capture());
        assertEquals("Curitiba", captor.getValue().name());
    }

    @Test
    void rejectsBlankName() {
        assertThrows(IllegalArgumentException.class,
                () -> new LocationService(locationDao).add("   ", 0, 0));
        verifyNoInteractions(locationDao);
    }

    @Test
    void rejectsOutOfRangeCoordinates() {
        LocationService service = new LocationService(locationDao);
        assertThrows(IllegalArgumentException.class, () -> service.add("X", 91, 0));
        assertThrows(IllegalArgumentException.class, () -> service.add("X", -91, 0));
        assertThrows(IllegalArgumentException.class, () -> service.add("X", 0, 181));
        assertThrows(IllegalArgumentException.class, () -> service.add("X", 0, -181));
        verifyNoInteractions(locationDao);
    }
}
