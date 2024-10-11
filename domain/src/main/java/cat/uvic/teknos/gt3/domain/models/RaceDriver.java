package cat.uvic.teknos.gt3.domain.models;

public interface RaceDriver {
    RaceDriverId getId();
    void setId(RaceDriverId id);

    int getPosition();
    void setPosition(int position);

    Race getRace();
    void setRace(Race race);

    Driver getDriver();
    void setDriver(Driver driver);
}
