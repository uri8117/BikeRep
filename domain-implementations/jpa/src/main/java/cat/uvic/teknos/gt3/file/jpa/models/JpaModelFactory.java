package cat.uvic.teknos.gt3.file.jpa.models;


import cat.uvic.teknos.gt3.domain.models.*;

public class JpaModelFactory implements ModelFactory {
    @Override
    public Brand createBrand() {
        return new Brand();
    }

    @Override
    public BrandData createBrandData() {
        return new BrandData();
    }

    @Override
    public Car createCar() {
        return new Car();
    }

    public CarData createCarData() {
        return new CarData();
    }
    @Override
    public Circuit createCircuit() {
        return new Circuit();
    }

    @Override
    public Driver createDriver() {
        return new Driver();
    }

    @Override
    public Race createRace() {
        return new Race();
    }
}
