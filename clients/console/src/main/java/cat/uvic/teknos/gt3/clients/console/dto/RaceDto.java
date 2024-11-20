package cat.uvic.teknos.gt3.clients.console.dto;

import cat.uvic.teknos.gt3.domain.models.Circuit;
import cat.uvic.teknos.gt3.domain.models.Race;
import cat.uvic.teknos.gt3.domain.models.RaceDriver;

import java.sql.Date;
import java.util.Set;

public class RaceDto implements Race {
    private int id;
    private String raceName;
    private Date raceDate;
    private Set<cat.uvic.teknos.gt3.domain.models.RaceDriver> raceDrivers;
    private Circuit circuit;

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
    public Set<cat.uvic.teknos.gt3.domain.models.RaceDriver> getRaceDrivers() {
        return raceDrivers;
    }

    @Override
    public void setRaceDrivers(Set<cat.uvic.teknos.gt3.domain.models.RaceDriver> raceDrivers) {
        this.raceDrivers = raceDrivers;
    }

    @Override
    public Circuit getCircuit() {
        return circuit;
    }

    @Override
    public void setCircuit(Circuit circuit) {
        this.circuit = circuit;
    }
}
