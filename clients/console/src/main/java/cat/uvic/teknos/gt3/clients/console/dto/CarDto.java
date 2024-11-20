package cat.uvic.teknos.gt3.clients.console.dto;

import cat.uvic.teknos.gt3.domain.models.Brand;
import cat.uvic.teknos.gt3.domain.models.CarData;
import cat.uvic.teknos.gt3.domain.models.Driver;

import java.util.HashSet;
import java.util.Set;

public class CarDto implements cat.uvic.teknos.gt3.domain.models.Car {
    private int id;
    private int brandId;
    private String modelName;
    private Brand brand;
    private CarData carData;
    private Set<Driver> drivers;

    public CarDto() {
        this.drivers = new HashSet<>(); // Inicializa el conjunto de drivers
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
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
    public Brand getBrand() {
        return brand;
    }

    @Override
    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    @Override
    public CarData getCarData() {
        return carData;
    }

    @Override
    public void setCarData(CarData carData) {
        this.carData = carData;
    }

    @Override
    public Set<Driver> getDrivers() {
        return drivers;
    }

    @Override
    public void setDrivers(Set<Driver> driver) {
        this.drivers = driver;
    }

    @Override
    public void addDriver(Driver driver) {
        this.drivers.add(driver); // Agrega un conductor al conjunto
    }

    @Override
    public void clearDrivers() {
        this.drivers.clear(); // Limpia el conjunto de conductores
    }
}
