package cat.uvic.teknos.gt3.file.jdbc.test;

import cat.uvic.teknos.gt3.file.jbdc.models.Brand;
import cat.uvic.teknos.gt3.file.jbdc.models.Car;
import cat.uvic.teknos.gt3.file.jbdc.models.CarData;
import cat.uvic.teknos.gt3.file.jbdc.models.Driver;
import cat.uvic.teknos.gt3.file.jbdc.repositories.JdbcCarRepository;
import com.fcardara.dbtestutils.junit.CreateSchemaExtension;
import com.fcardara.dbtestutils.junit.DbAssertions;
import com.fcardara.dbtestutils.junit.GetConnectionExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({CreateSchemaExtension.class, GetConnectionExtension.class})
public class JdbcCarRepositoryTest {
    private final Connection connection;

    public JdbcCarRepositoryTest(Connection connection) {
        this.connection = connection;
    }

    @Test
    @DisplayName("Given a new Car (id = 0), when saved, then a new record is added to the CARS table")
    public void shouldInsertCar() throws SQLException {
        // Create a new brand
        Brand porscheBrand = new Brand();
        porscheBrand.setId(2); // Assuming ID 1 corresponds to Toyota, update if needed

        // Create drivers with IDs 1, 2
        Driver driver1 = createDriverWithId(1);
        Driver driver2 = createDriverWithId(2);

        // Create a new car
        Car car = new Car();
        car.setBrand(porscheBrand);
        car.setModelName("992 gt3 rs");


        // Create a new data for car
        var carData = new CarData();
        carData.setCar(car);
        carData.setHorsepower(660);
        carData.setWeight(1210);

        car.setCarData(carData);


        // Add drivers to the car
        var drivers = new HashSet<cat.uvic.teknos.gt3.domain.models.Driver>();
        drivers.add(driver1);
        drivers.add(driver2);
        car.setDrivers(drivers);

        // Save the car in the database
        JdbcCarRepository carRepository = new JdbcCarRepository(connection);
        carRepository.save(car);

        // Verify that an ID has been assigned to the car
        assertTrue(car.getId() > 0);

        DbAssertions.assertThat(connection)
                .table("CAR")
                .where("ID_CAR", car.getId())
                .hasOneLine();
    }

    private Driver createDriverWithId(int id) {
        Driver driver = new Driver();
        driver.setId(id);
        return driver;
    }
}
