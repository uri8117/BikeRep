package cat.uvic.teknos.gt3.file.jpa.test;

import cat.uvic.teknos.gt3.file.jpa.models.Circuit;
import cat.uvic.teknos.gt3.file.jpa.models.Race;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class JpaRaceRepositoryTest {
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
    public void shouldInsertRace() throws SQLException {
        var entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();

            // Create a new circuit
            Circuit circuit = new Circuit();
            circuit.setLengthKm(6.752f);
            circuit.setCircuitName("Test");
            circuit.setCountry("Belgium");

            // Persist the circuit
            entityManager.persist(circuit);

            // Create a new race
            Race race = new Race();
            race.setRaceDate(Date.valueOf("2023-07-19"));
            race.setRaceName("Belgium Grand Prix");
            race.setCircuit(circuit);

            // Persist the race
            entityManager.persist(race);

            // Ensure the race has been persisted and ID is generated
            assertNotNull(race.getId());

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }
}
