package cat.uvic.teknos.gt3.domain.repositories;


import java.sql.SQLException;

public interface RepositoryFactory {

    BrandRepository getBrandRepository() throws SQLException;

    BrandDataRepository getBrandDataRepository() throws SQLException;

    CarRepository getCarRepository() throws SQLException;

    CarDataRepository getCarDataRepository() throws SQLException;

    CircuitRepository getCircuitRepository() throws SQLException;

    DriverRepository getDriverRepository() throws SQLException;

    RaceRepository getRaceRepository() throws SQLException;

}
