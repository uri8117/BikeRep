package cat.uvic.teknos.gt3.clients.console.dto;

import cat.uvic.teknos.gt3.domain.models.BrandData;
import cat.uvic.teknos.gt3.domain.models.Car;

import java.util.Set;

public class BrandDto implements cat.uvic.teknos.gt3.domain.models.Brand {
    private int id;
    private String name;
    private Set<Car> cars;
    private BrandData brandData;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getBrandName() {
        return name;
    }

    @Override
    public void setBrandName(String name) {
        this.name = name;
    }

    @Override
    public Set<Car> getCars() {
        return cars;
    }

    @Override
    public void setCars(Set<Car> car) {
        this.cars = car;
    }

    @Override
    public BrandData getBrandData() {
        return brandData;
    }

    @Override
    public void setBrandData(BrandData brandData) {
        this.brandData = brandData;
    }
}
