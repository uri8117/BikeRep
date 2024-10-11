package cat.uvic.teknos.gt3.file.jpa.repository;

import cat.uvic.teknos.gt3.domain.exceptions.RepositoryException;
import cat.uvic.teknos.gt3.domain.repositories.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class JpaRepositoryFactory implements RepositoryFactory {

    private final EntityManagerFactory entityManagerFactory;

    public JpaRepositoryFactory() {
        var properties = new Properties();
        try {
            properties.load(JpaRepositoryFactory.class.getResourceAsStream("/jpa.properties"));
        } catch (IOException e) {
            throw new RepositoryException(e);
        }
        entityManagerFactory = Persistence.createEntityManagerFactory("gt3_rep_mysql", properties);
    }

    @Override
    public BrandRepository getBrandRepository() throws SQLException {
        return new JpaBrandRepository(entityManagerFactory);
    }

    @Override
    public BrandDataRepository getBrandDataRepository() throws SQLException {
        return new JpaBrandDataRepository(entityManagerFactory);
    }

    @Override
    public CarRepository getCarRepository() throws SQLException {
        return new JpaCarRepository(entityManagerFactory);
    }

    @Override
    public CarDataRepository getCarDataRepository() throws SQLException {
        return new JpaCarDataRepository(entityManagerFactory);
    }

    @Override
    public CircuitRepository getCircuitRepository() throws SQLException {
        return new JpaCircuitRepository(entityManagerFactory);
    }

    @Override
    public DriverRepository getDriverRepository() throws SQLException {
        return new JpaDriverRepository(entityManagerFactory);
    }

    @Override
    public RaceRepository getRaceRepository() throws SQLException {
        return new JpaRaceRepository(entityManagerFactory);
    }
}


