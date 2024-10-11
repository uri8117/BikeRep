package cat.uvic.teknos.gt3.file.jpa.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "CAR")
public class Car implements cat.uvic.teknos.gt3.domain.models.Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "MODEL_NAME", nullable = false)
    private String modelName;

    @ManyToOne
    @JoinColumn(name = "ID_BRAND", nullable = false)
    private Brand brand;

    @OneToOne(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    private CarData carData;

    @ManyToMany(mappedBy = "cars", targetEntity = Driver.class)
    private Set<cat.uvic.teknos.gt3.domain.models.Driver> drivers = new HashSet<>();

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
    public cat.uvic.teknos.gt3.domain.models.Brand getBrand() {
        return brand;
    }

    @Override
    public void setBrand(cat.uvic.teknos.gt3.domain.models.Brand brand) {
        this.brand = (Brand) brand;
    }

    @Override
    public String getModelName() {
        return modelName;
    }

    @Override
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    @Override
    public cat.uvic.teknos.gt3.domain.models.CarData getCarData() {
        return carData;
    }

    @Override
    public void setCarData(cat.uvic.teknos.gt3.domain.models.CarData carData) {
        this.carData = (CarData) carData;
    }

    @Override
    public Set<cat.uvic.teknos.gt3.domain.models.Driver> getDrivers() {
        return drivers;
    }

    @Override
    public void setDrivers(Set<cat.uvic.teknos.gt3.domain.models.Driver> drivers) {
        this.drivers = drivers;
    }
}

