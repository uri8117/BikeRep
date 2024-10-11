package cat.uvic.teknos.gt3.file.jbdc.repositories;

import cat.uvic.teknos.gt3.domain.repositories.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcRepositoryFactory implements RepositoryFactory {

    private Connection connection;
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
    public BrandRepository getBrandRepository() throws SQLException {
        return null;
    }

    @Override
    public BrandDataRepository getBrandDataRepository() throws SQLException {
        return null;
    }

    @Override
    public CarRepository getCarRepository() throws SQLException {
        return new JdbcCarRepository(connection);
    }

    @Override
    public CarDataRepository getCarDataRepository() throws SQLException {
        return null;
    }

    @Override
    public CircuitRepository getCircuitRepository() throws SQLException {
        return null;
    }

    @Override
    public DriverRepository getDriverRepository() throws SQLException {
        return null;
    }

    @Override
    public RaceRepository getRaceRepository() throws SQLException {
        return new JdbcRaceRepository(connection);
    }
}
