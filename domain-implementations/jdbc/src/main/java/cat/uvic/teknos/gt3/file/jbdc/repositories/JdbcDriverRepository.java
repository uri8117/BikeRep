package cat.uvic.teknos.gt3.file.jbdc.repositories;

import cat.uvic.teknos.gt3.domain.models.Driver;
import cat.uvic.teknos.gt3.domain.repositories.DriverRepository;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class JdbcDriverRepository implements DriverRepository {
    private static final String INSERT_DRIVER = "INSERT INTO DRIVER (FIRST_NAME, LAST_NAME, NATIONALITY, BIRTHDATE) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_DRIVER = "UPDATE DRIVER SET FIRST_NAME = ?, LAST_NAME = ?, NATIONALITY = ?, BIRTHDATE = ? WHERE ID_DRIVER = ?";
    private static final String DELETE_DRIVER = "DELETE FROM DRIVER WHERE ID_DRIVER = ?";
    private static final String GET_DRIVER = "SELECT * FROM DRIVER WHERE ID_DRIVER = ?";
    private static final String GET_ALL_DRIVERS = "SELECT * FROM DRIVER";

    private final Connection connection;

    public JdbcDriverRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Driver driver) {
        if (driver.getId() <= 0) {
            insert(driver);
        } else {
            update(driver);
        }
    }

    private void insert(Driver driver) {
        try (var preparedStatement = connection.prepareStatement(INSERT_DRIVER, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, driver.getFirstName());
            preparedStatement.setString(2, driver.getLastName());
            preparedStatement.setString(3, driver.getNationality());
            preparedStatement.setDate(4, driver.getBirthdate());
            preparedStatement.executeUpdate();

            var keys = preparedStatement.getGeneratedKeys();
            if (keys.next()) {
                driver.setId(keys.getInt(1));
            } else {
                throw new SQLException("Creating driver failed, no ID obtained.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void update(Driver driver) {
        try (var preparedStatement = connection.prepareStatement(UPDATE_DRIVER)) {
            preparedStatement.setString(1, driver.getFirstName());
            preparedStatement.setString(2, driver.getLastName());
            preparedStatement.setString(3, driver.getNationality());
            preparedStatement.setDate(4, driver.getBirthdate());
            preparedStatement.setInt(5, driver.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Driver driver) {
        try (var preparedStatement = connection.prepareStatement(DELETE_DRIVER)) {
            preparedStatement.setInt(1, driver.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Driver get(Integer id) {
        try (var preparedStatement = connection.prepareStatement(GET_DRIVER)) {
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Driver driver = new cat.uvic.teknos.gt3.file.jbdc.models.Driver();
                driver.setId(resultSet.getInt("ID_DRIVER"));
                driver.setFirstName(resultSet.getString("FIRST_NAME"));
                driver.setLastName(resultSet.getString("LAST_NAME"));
                driver.setNationality(resultSet.getString("NATIONALITY"));
                driver.setBirthdate(resultSet.getDate("BIRTHDATE"));
                return driver;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Set<Driver> getAll() {
        Set<Driver> drivers = new HashSet<>();
        try (var preparedStatement = connection.prepareStatement(GET_ALL_DRIVERS);
             var resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Driver driver = new cat.uvic.teknos.gt3.file.jbdc.models.Driver();
                driver.setId(resultSet.getInt("ID_DRIVER"));
                driver.setFirstName(resultSet.getString("FIRST_NAME"));
                driver.setLastName(resultSet.getString("LAST_NAME"));
                driver.setNationality(resultSet.getString("NATIONALITY"));
                driver.setBirthdate(resultSet.getDate("BIRTHDATE"));
                drivers.add(driver);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return drivers;
    }
}
