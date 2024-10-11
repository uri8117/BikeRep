package cat.uvic.teknos.gt3.file.jpa.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "CIRCUIT")
public class Circuit implements cat.uvic.teknos.gt3.domain.models.Circuit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CIRCUIT")
    private int id;

    @Column(name = "CIRCUIT_NAME", nullable = false)
    private String circuitName;

    @Column(name = "COUNTRY", nullable = false)
    private String country;

    @Column(name = "LENGTH_KM", nullable = false)
    private double lengthKm;

    @OneToMany(mappedBy = "circuit", cascade = CascadeType.ALL, targetEntity = Race.class)
    private Set<cat.uvic.teknos.gt3.domain.models.Race> races = new HashSet<>();

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
    public String getCircuitName() {
        return circuitName;
    }

    @Override
    public void setCircuitName(String circuitName) {
        this.circuitName = circuitName;
    }

    @Override
    public String getCountry() {
        return country;
    }

    @Override
    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public double getLengthKm() {
        return lengthKm;
    }

    @Override
    public void setLengthKm(double lengthKm) {
        this.lengthKm = lengthKm;
    }

    @Override
    public Set<cat.uvic.teknos.gt3.domain.models.Race> getRaces() {
        return races;
    }

    @Override
    public void setRaces(cat.uvic.teknos.gt3.domain.models.Race race) {
        this.races.add(race);
    }

}
