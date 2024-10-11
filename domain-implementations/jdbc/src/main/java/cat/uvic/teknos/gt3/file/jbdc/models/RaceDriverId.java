package cat.uvic.teknos.gt3.file.jbdc.models;

public class RaceDriverId implements cat.uvic.teknos.gt3.domain.models.RaceDriverId {
    private int raceId;
    private int driverId;

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
