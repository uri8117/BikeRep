package cat.uvic.teknos.gt3.domain.repositories;


import java.sql.SQLException;

public interface RepositoryFactory {

    BrandRepository getBrandRepository();

    BrandDataRepository getBrandDataRepository();

    CarRepository getCarRepository();

    CarDataRepository getCarDataRepository();

    CircuitRepository getCircuitRepository();

    DriverRepository getDriverRepository();

    RaceRepository getRaceRepository();

}
