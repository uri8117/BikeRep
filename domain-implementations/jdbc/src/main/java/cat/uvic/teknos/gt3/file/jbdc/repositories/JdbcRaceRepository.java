package cat.uvic.teknos.gt3.file.jbdc.repositories;

import cat.uvic.teknos.gt3.domain.models.Race;
import cat.uvic.teknos.gt3.domain.models.Driver;
import cat.uvic.teknos.gt3.domain.repositories.RaceRepository;
import cat.uvic.teknos.gt3.domain.models.RaceDriver;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class JdbcRaceRepository implements RaceRepository {
    private static final String INSERT_RACE = "INSERT INTO RACE (ID_CIRCUIT, RACE_NAME, RACE_DATE) VALUES (?,?,?)";
    private static final String INSERT_RACE_DRIVER = "INSERT INTO RACE_DRIVER (ID_RACE, ID_DRIVER, POSITION) VALUES (?,?,?)";
    private static final String UPDATE_RACE = "UPDATE RACE SET RACE_NAME = ?, RACE_DATE = ? WHERE ID_RACE = ?";
    private static final String DELETE_RACE_DRIVERS = "DELETE FROM RACE_DRIVER WHERE ID_RACE = ?";
    private static final String SELECT_RACE = "SELECT * FROM RACE WHERE ID_RACE = ?";
    private static final String SELECT_ALL_RACES = "SELECT * FROM RACE";
    private static final String SELECT_RACE_DRIVERS = "SELECT * FROM RACE_DRIVER WHERE ID_RACE = ?";

    private final Connection connection;

    public JdbcRaceRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Race model) {
        if (model.getId() <= 0) {
            insert(model);
        } else {
            update(model);
        }
    }

    private void insert(Race model) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_RACE, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);

            preparedStatement.setInt(1, model.getCircuit().getId());
            preparedStatement.setString(2, model.getRaceName());
            preparedStatement.setDate(3, model.getRaceDate());
            preparedStatement.executeUpdate();

            ResultSet keys = preparedStatement.getGeneratedKeys();
            if (keys.next()) {
                model.setId(keys.getInt(1));
                if (model.getRaceDrivers() != null) {
                    for (RaceDriver raceDriver : model.getRaceDrivers()) {
                        raceDriver.getId().setRaceId(model.getId());
                        insertRaceDriver(raceDriver);
                    }
                }
            } else {
                throw new SQLException("Creating race failed, no ID obtained.");
            }

            connection.commit();
        } catch (SQLException e) {
            rollback();
            throw new RuntimeException(e);
        } finally {
            setAutocommitTrue();
        }
    }

    private void insertRaceDriver(RaceDriver model) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_RACE_DRIVER)) {
            preparedStatement.setInt(1, model.getId().getRaceId());
            preparedStatement.setInt(2, model.getId().getDriverId());
            preparedStatement.setInt(3, model.getPosition());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void update(Race model) {
        try (PreparedStatement raceStatement = connection.prepareStatement(UPDATE_RACE)) {
            connection.setAutoCommit(false);

            raceStatement.setString(1, model.getRaceName());
            raceStatement.setDate(2, model.getRaceDate());
            raceStatement.setInt(3, model.getId());
            raceStatement.executeUpdate();

            deleteRaceDrivers(model.getId());

            if (model.getRaceDrivers() != null) {
                for (RaceDriver raceDriver : model.getRaceDrivers()) {
                    raceDriver.getId().setRaceId(model.getId());
                    insertRaceDriver(raceDriver);
                }
            }

            connection.commit();
        } catch (SQLException e) {
            rollback();
            throw new RuntimeException(e);
        } finally {
            setAutocommitTrue();
        }
    }

    private void deleteRaceDrivers(int raceId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_RACE_DRIVERS)) {
            preparedStatement.setInt(1, raceId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Race model) {
        try (PreparedStatement raceStatement = connection.prepareStatement("DELETE FROM RACE WHERE ID_RACE = ?")) {
            connection.setAutoCommit(false);

            raceStatement.setInt(1, model.getId());
            raceStatement.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            rollback();
            throw new RuntimeException(e);
        } finally {
            setAutocommitTrue();
        }
    }

    @Override
    public Race get(Integer id) {
        try (PreparedStatement raceStatement = connection.prepareStatement(SELECT_RACE);
             PreparedStatement raceDriversStatement = connection.prepareStatement(SELECT_RACE_DRIVERS)) {

            raceStatement.setInt(1, id);
            ResultSet raceResultSet = raceStatement.executeQuery();

            if (raceResultSet.next()) {
                Race race = new cat.uvic.teknos.gt3.file.jbdc.models.Race();
                race.setId(raceResultSet.getInt("ID_RACE"));
                race.setRaceName(raceResultSet.getString("RACE_NAME"));
                race.setRaceDate(raceResultSet.getDate("RACE_DATE"));

                // Retrieve circuit information
                var circuit = new cat.uvic.teknos.gt3.file.jbdc.models.Circuit();
                circuit.setId(raceResultSet.getInt("ID_CIRCUIT"));
                race.setCircuit(circuit);

                // Retrieve race drivers
                raceDriversStatement.setInt(1, race.getId());
                ResultSet raceDriversResultSet = raceDriversStatement.executeQuery();
                Set<RaceDriver> raceDrivers = new HashSet<>();
                while (raceDriversResultSet.next()) {
                    var raceDriver = new cat.uvic.teknos.gt3.file.jbdc.models.RaceDriver();
                    var raceDriverId = new cat.uvic.teknos.gt3.file.jbdc.models.RaceDriverId();
                    raceDriverId.setRaceId(raceDriversResultSet.getInt("ID_RACE"));
                    raceDriverId.setDriverId(raceDriversResultSet.getInt("ID_DRIVER"));
                    raceDriver.setId(raceDriverId);
                    raceDriver.setPosition(raceDriversResultSet.getInt("POSITION"));
                    raceDrivers.add(raceDriver);
                }
                race.setRaceDrivers(raceDrivers);

                return race;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Set<Race> getAll() {
        Set<Race> races = new HashSet<>();
        try (Statement statement = connection.createStatement();
             PreparedStatement raceDriversStatement = connection.prepareStatement(SELECT_RACE_DRIVERS)) {

            ResultSet raceResultSet = statement.executeQuery(SELECT_ALL_RACES);
            while (raceResultSet.next()) {
                var race = new cat.uvic.teknos.gt3.file.jbdc.models.Race();
                race.setId(raceResultSet.getInt("ID_RACE"));
                race.setRaceName(raceResultSet.getString("RACE_NAME"));
                race.setRaceDate(raceResultSet.getDate("RACE_DATE"));

                // Retrieve circuit information
                var circuit = new cat.uvic.teknos.gt3.file.jbdc.models.Circuit();
                circuit.setId(raceResultSet.getInt("ID_CIRCUIT"));
                race.setCircuit(circuit);

                // Retrieve race drivers
                raceDriversStatement.setInt(1, race.getId());
                ResultSet raceDriversResultSet = raceDriversStatement.executeQuery();
                Set<RaceDriver> raceDrivers = new HashSet<>();
                while (raceDriversResultSet.next()) {
                    var raceDriver = new cat.uvic.teknos.gt3.file.jbdc.models.RaceDriver();
                    var raceDriverId = new cat.uvic.teknos.gt3.file.jbdc.models.RaceDriverId();
                    raceDriverId.setRaceId(raceDriversResultSet.getInt("ID_RACE"));
                    raceDriverId.setDriverId(raceDriversResultSet.getInt("ID_DRIVER"));
                    raceDriver.setId(raceDriverId);
                    raceDriver.setPosition(raceDriversResultSet.getInt("POSITION"));
                    raceDrivers.add(raceDriver);
                }
                race.setRaceDrivers(raceDrivers);

                races.add(race);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return races;
    }

    private void setAutocommitTrue() {
        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void rollback() {
        try {
            connection.rollback();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
