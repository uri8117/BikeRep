package cat.uvic.teknos.gt3.file.jpa.models;

import jakarta.persistence.*;

@Entity
@Table(name = "RACE_DRIVER")
public class RaceDriver implements cat.uvic.teknos.gt3.domain.models.RaceDriver {
    @EmbeddedId
    private RaceDriverId id;

    @Column(name = "POSITION", nullable = false)
    private int position;

    @ManyToOne(targetEntity = Race.class)
    @MapsId("raceId")
    @JoinColumn(name = "ID_RACE")
    private cat.uvic.teknos.gt3.domain.models.Race race;

    @ManyToOne(targetEntity = Driver.class)
    @MapsId("driverId")
    @JoinColumn(name = "ID_DRIVER")
    private cat.uvic.teknos.gt3.domain.models.Driver driver;

    // Getters and setters

    @Override
    public cat.uvic.teknos.gt3.domain.models.RaceDriverId getId() {
        return id;
    }

    @Override
    public void setId(cat.uvic.teknos.gt3.domain.models.RaceDriverId id) {
        this.id = (RaceDriverId) id;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public cat.uvic.teknos.gt3.domain.models.Race getRace() {
        return race;
    }

    @Override
    public void setRace(cat.uvic.teknos.gt3.domain.models.Race race) {
        this.race = race;
    }

    @Override
    public cat.uvic.teknos.gt3.domain.models.Driver getDriver() {
        return driver;
    }

    @Override
    public void setDriver(cat.uvic.teknos.gt3.domain.models.Driver driver) {
        this.driver = driver;
    }
}
