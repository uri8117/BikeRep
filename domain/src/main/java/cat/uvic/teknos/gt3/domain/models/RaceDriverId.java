package cat.uvic.teknos.gt3.domain.models;

import java.io.Serializable;

public interface RaceDriverId extends Serializable {
    int getRaceId();
    void setRaceId(int raceId);

    int getDriverId();
    void setDriverId(int driverId);
}
