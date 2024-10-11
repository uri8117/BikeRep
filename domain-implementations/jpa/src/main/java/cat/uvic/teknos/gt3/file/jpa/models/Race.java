package cat.uvic.teknos.gt3.file.jpa.models;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.Set;

@Entity
@Table(name = "RACE")
public class Race implements cat.uvic.teknos.gt3.domain.models.Race {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RACE")
    private int id;

    @Column(name = "RACE_NAME", nullable = false)
    private String raceName;

    @Column(name = "RACE_DATE", nullable = false)
    private Date raceDate;

    @ManyToOne
    @JoinColumn(name = "ID_CIRCUIT", nullable = false)
    private Circuit circuit;

    @OneToMany(mappedBy = "race", cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = RaceDriver.class)
    private Set<cat.uvic.teknos.gt3.domain.models.RaceDriver> raceDrivers;
    // Getters and setters

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getRaceName() {
        return raceName;
    }

    @Override
    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }

    @Override
    public Date getRaceDate() {
        return raceDate;
    }

    @Override
    public void setRaceDate(Date raceDate) {
        this.raceDate = raceDate;
    }

    @Override
    public cat.uvic.teknos.gt3.domain.models.Circuit getCircuit() {
        return circuit;
    }

    @Override
    public void setCircuit(cat.uvic.teknos.gt3.domain.models.Circuit circuit) {
        this.circuit = (Circuit) circuit;
    }

    @Override
    public Set<cat.uvic.teknos.gt3.domain.models.RaceDriver> getRaceDrivers() {
        return raceDrivers;
    }

    @Override
    public void setRaceDrivers(Set<cat.uvic.teknos.gt3.domain.models.RaceDriver> raceDrivers) {
        this.raceDrivers = raceDrivers;
    }
}
