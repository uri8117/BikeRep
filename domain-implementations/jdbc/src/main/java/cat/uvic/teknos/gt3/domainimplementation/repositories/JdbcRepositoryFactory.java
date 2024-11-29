package cat.uvic.teknos.gt3.domainimplementation.repositories;

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
    public BikeRepository getBikeRepository() {
        return new BikeRepositoryJDBC(connection);
    }

    @Override
    public BrandRepository getBrandRepository() {
        return new BrandRepositoryJDBC(connection);
    }

    @Override
    public UserRepository getUserRepository() {
        return new UserRepositoryJDBC(connection);
    }

    @Override
    public UserBikeRepository getUserBikeRepository() {
        return new UserBikeRepositoryJDBC(connection);
    }
}
