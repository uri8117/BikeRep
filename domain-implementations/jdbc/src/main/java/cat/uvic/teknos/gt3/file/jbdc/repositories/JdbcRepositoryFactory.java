package cat.uvic.teknos.gt3.file.jbdc.repositories;

import cat.uvic.teknos.gt3.domain.repositories.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcRepositoryFactory implements RepositoryFactory {

    private final Connection connection;
    public JdbcRepositoryFactory(){
        try {
            var properties = new Properties();
            properties.load(this.getClass().getResourceAsStream("/datasource.properties"));
            connection = DriverManager.getConnection(String.format("%s:%s://%s/%s",
                    properties.getProperty("protocol"),
                    properties.getProperty("subprotocol"),
                    properties.getProperty("url"),
                    properties.getProperty("database")), properties.getProperty("user"), properties.getProperty("password"));
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BrandRepository getBrandRepository() {
        return new JdbcBrandRepository(connection);
    }

    @Override
    public BrandDataRepository getBrandDataRepository() {
        return null;
    }

    @Override
    public CarRepository getCarRepository() {
        return new JdbcCarRepository(connection);
    }

    @Override
    public CarDataRepository getCarDataRepository() {
        return null;
    }

    @Override
    public CircuitRepository getCircuitRepository() {
        return new JdbcCircuitRepository(connection);
    }

    @Override
    public DriverRepository getDriverRepository() {
        return new JdbcDriverRepository(connection);
    }

    @Override
    public RaceRepository getRaceRepository() {
        return new JdbcRaceRepository(connection);
    }
}
