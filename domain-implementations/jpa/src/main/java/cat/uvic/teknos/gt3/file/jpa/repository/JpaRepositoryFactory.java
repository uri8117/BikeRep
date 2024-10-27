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
    public BrandRepository getBrandRepository() {
        return new JpaBrandRepository(entityManagerFactory);
    }

    @Override
    public BrandDataRepository getBrandDataRepository() {
        return new JpaBrandDataRepository(entityManagerFactory);
    }

    @Override
    public CarRepository getCarRepository() {
        return new JpaCarRepository(entityManagerFactory);
    }

    @Override
    public CarDataRepository getCarDataRepository() {
        return new JpaCarDataRepository(entityManagerFactory);
    }

    @Override
    public CircuitRepository getCircuitRepository() {
        return new JpaCircuitRepository(entityManagerFactory);
    }

    @Override
    public DriverRepository getDriverRepository() {
        return new JpaDriverRepository(entityManagerFactory);
    }

    @Override
    public RaceRepository getRaceRepository() {
        return new JpaRaceRepository(entityManagerFactory);
    }
}


