package cat.uvic.teknos.gt3.file.jpa.test;

import cat.uvic.teknos.gt3.file.jpa.models.Brand;
import cat.uvic.teknos.gt3.file.jpa.models.Car;
import cat.uvic.teknos.gt3.file.jpa.models.CarData;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertTrue;

class JpaCarRepositoryTest {
    private static EntityManagerFactory entityManagerFactory;

    @BeforeAll
    static void setUp() throws IOException {
        var properties = new Properties();
        entityManagerFactory = Persistence.createEntityManagerFactory("gt3_rep_mysql", properties);
    }

    @AfterAll
    static void tearDown() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    @Test
    public void shouldInsertCar() throws SQLException {
        var entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();

            // Search an existent brand into the database
            var brand = entityManager.find(Brand.class, 1);
            if (brand == null) {
                throw new RuntimeException("Brand with ID 1 not found");
            }
            System.out.println(brand.getId());
            System.out.println(brand.getBrandName());

            // Create a new car
            Car car = new Car();
            car.setBrand(brand);
            car.setModelName("992 GT3 RS");

            // Create a new data for car
            CarData carData = new CarData();
            carData.setHorsepower(660);
            carData.setWeight(1210);

            // Set bidirectional relationship
            car.setCarData(carData);
            carData.setCar(car);

            // Persist the car (which will also persist carData due to cascading)
            entityManager.persist(car);

            // Ensure the car has been persisted and ID is generated
            assertTrue(car.getId() > 0);

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }
}

