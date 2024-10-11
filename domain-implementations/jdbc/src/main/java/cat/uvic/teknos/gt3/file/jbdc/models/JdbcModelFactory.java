package cat.uvic.teknos.gt3.file.jbdc.models;

import cat.uvic.teknos.gt3.domain.models.*;
import cat.uvic.teknos.gt3.domain.models.Brand;
import cat.uvic.teknos.gt3.domain.models.BrandData;
import cat.uvic.teknos.gt3.domain.models.Car;
import cat.uvic.teknos.gt3.domain.models.CarData;
import cat.uvic.teknos.gt3.domain.models.Circuit;
import cat.uvic.teknos.gt3.domain.models.Driver;
import cat.uvic.teknos.gt3.domain.models.Race;

public class JdbcModelFactory implements ModelFactory {
    @Override
    public Brand createBrand() {
        return new cat.uvic.teknos.gt3.file.jbdc.models.Brand();
    }

    @Override
    public Car createCar() {
        return new cat.uvic.teknos.gt3.file.jbdc.models.Car();
    }

    @Override
    public Circuit createCircuit() {
        return new cat.uvic.teknos.gt3.file.jbdc.models.Circuit();
    }

    @Override
    public Driver createDriver() {
        return new cat.uvic.teknos.gt3.file.jbdc.models.Driver();
    }

    @Override
    public Race createRace() {
        return new cat.uvic.teknos.gt3.file.jbdc.models.Race();
    }

    @Override
    public BrandData createBrandData() {
        return new cat.uvic.teknos.gt3.file.jbdc.models.BrandData();
    }

    @Override
    public CarData createCarData() {
        return new cat.uvic.teknos.gt3.file.jbdc.models.CarData();
    }
}
