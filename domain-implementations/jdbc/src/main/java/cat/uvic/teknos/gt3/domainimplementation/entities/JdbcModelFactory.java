package cat.uvic.teknos.gt3.domainimplementation.entities;

import cat.uvic.teknos.gt3.domain.models.*;

public class JdbcModelFactory implements ModelFactory {
    @Override
    public Bike createBike() {
        return new BikeEntity();
    }

    @Override
    public BikeData createBikeData() {
        return new BikeDataEntity();
    }

    @Override
    public Brand createBrand() {
        return new BrandEntity();
    }

    @Override
    public User createUser() {
        return new UserEntity();
    }
}
