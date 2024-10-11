package cat.uvic.teknos.gt3.domain.models;

public interface ModelFactory {

    Brand createBrand();

    BrandData createBrandData();

    Car createCar();

    CarData createCarData();

    Circuit createCircuit();

    Driver createDriver();

    Race createRace();
}
