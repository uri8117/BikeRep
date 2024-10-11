package cat.uvic.teknos.gt3.file.jdbc.test;

import cat.uvic.teknos.gt3.file.jbdc.models.*;
import cat.uvic.teknos.gt3.file.jbdc.repositories.JdbcRaceRepository;

import com.fcardara.dbtestutils.junit.CreateSchemaExtension;
import com.fcardara.dbtestutils.junit.DbAssertions;
import com.fcardara.dbtestutils.junit.GetConnectionExtension;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.Connection;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({CreateSchemaExtension.class, GetConnectionExtension.class})
public class JdbcRaceRepositoryTest {
    private final Connection connection;

    public JdbcRaceRepositoryTest(Connection connection) {
        this.connection = connection;
    }

    @Test
    @DisplayName("Given a new Race (id = 0), when saved, then a new record is added to the RACES table")
    public void shouldInsertRace() {
        // Get the existing circuit (Spa-Francorchamps)
        Circuit spaCircuit = new Circuit();
        spaCircuit.setId(4); // Assuming ID 4 corresponds to Spa-Francorchamps, update if needed

        // Create drivers with IDs 1, 3, and 5
        Driver driver1 = createDriverWithId(1);
        Driver driver2 = createDriverWithId(3);
        Driver driver3 = createDriverWithId(5);

        var raceDriver1 = new RaceDriver();
        var raceDriver1Id = new RaceDriverId();
        raceDriver1Id.setDriverId(driver1.getId());
        raceDriver1.setId(raceDriver1Id);
        raceDriver1.setPosition(1);

        var raceDriver2 = new RaceDriver();
        var raceDriver2Id = new RaceDriverId();
        raceDriver2Id.setDriverId(driver2.getId());
        raceDriver2.setId(raceDriver2Id);
        raceDriver2.setPosition(2);

        var raceDriver3 = new RaceDriver();
        var raceDriver3Id = new RaceDriverId();
        raceDriver3Id.setDriverId(driver3.getId());
        raceDriver3.setId(raceDriver3Id);
        raceDriver3.setPosition(1);

        // Create a new race
        Race race = new Race();
        race.setCircuit(spaCircuit);
        race.setRaceName("Example Race");
        race.setRaceDate(Date.valueOf("2024-05-26"));

        // Add drivers to the race with positions
        var raceDrivers = new HashSet<cat.uvic.teknos.gt3.domain.models.RaceDriver>();
        raceDrivers.add(raceDriver1);
        raceDrivers.add(raceDriver2);
        raceDrivers.add(raceDriver3);

        race.setRaceDrivers(raceDrivers);

        // Save the race in the database
        JdbcRaceRepository raceRepository = new JdbcRaceRepository(connection);
        raceRepository.save(race);

        // Verify that an ID has been assigned to the race
        assertTrue(race.getId() > 0);

        DbAssertions.assertThat(connection)
                .table("RACE")
                .where("ID_RACE", race.getId())
                .hasOneLine();

    }

    private Driver createDriverWithId(int id) {
        Driver driver = new Driver();
        driver.setId(id);
        return driver;
    }
}
