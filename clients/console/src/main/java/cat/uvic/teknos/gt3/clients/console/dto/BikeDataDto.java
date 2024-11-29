package cat.uvic.teknos.gt3.clients.console.dto;

import cat.uvic.teknos.gt3.domain.models.Bike;
import cat.uvic.teknos.gt3.domain.models.BikeData;

import java.sql.*;

public class BikeDataDto implements BikeData {
    private Long id;
    private double engineCapacity;
    private double weight;
    private Bike bike;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public double getEngineCapacity() {
        return engineCapacity;
    }

    @Override
    public void setEngineCapacity(double engineCapacity) {
        this.engineCapacity = engineCapacity;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public Bike getBike() {
        return bike;
    }

    @Override
    public void setBike(Bike bike) {
        this.bike = bike;
    }
}
