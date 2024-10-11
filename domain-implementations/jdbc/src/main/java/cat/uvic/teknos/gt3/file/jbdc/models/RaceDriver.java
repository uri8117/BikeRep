package cat.uvic.teknos.gt3.file.jbdc.models;

import cat.uvic.teknos.gt3.domain.models.Driver;
import cat.uvic.teknos.gt3.domain.models.Race;
import cat.uvic.teknos.gt3.domain.models.RaceDriverId;

public class RaceDriver implements cat.uvic.teknos.gt3.domain.models.RaceDriver {
    private RaceDriverId id;
    private int position;
    private Driver driver;
    private Race race;

    @Override
    public RaceDriverId getId() {
        return id;
    }

    @Override
    public void setId(RaceDriverId id) {
        this.id = id;
    }

    @Override
    public Driver getDriver() {
        return driver;
    }

    @Override
    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    @Override
    public Race getRace() {
        return race;
    }

    @Override
    public void setRace(Race race) {
        this.race = race;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public void setPosition(int position) {
        this.position = position;
    }
}
