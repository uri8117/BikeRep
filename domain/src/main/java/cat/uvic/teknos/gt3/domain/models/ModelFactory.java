package cat.uvic.teknos.gt3.domain.models;

public interface ModelFactory {

    Bike createBike();

    BikeData createBikeData();

    Brand createBrand();

    User createUser();
}
