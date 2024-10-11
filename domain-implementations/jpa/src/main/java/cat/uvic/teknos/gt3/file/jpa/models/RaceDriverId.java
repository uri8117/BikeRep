package cat.uvic.teknos.gt3.file.jpa.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class RaceDriverId implements cat.uvic.teknos.gt3.domain.models.RaceDriverId {
    @Column(name = "ID_RACE")
    private int raceId;

    @Column(name = "ID_DRIVER")
    private int driverId;

    // Getters and setters

    @Override
    public int getRaceId() {
        return raceId;
    }

    @Override
    public void setRaceId(int raceId) {
        this.raceId = raceId;
    }

    @Override
    public int getDriverId() {
        return driverId;
    }

    @Override
    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }
}
