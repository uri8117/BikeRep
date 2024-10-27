package cat.uvic.teknos.gt3.file.jbdc.repositories;

import cat.uvic.teknos.gt3.domain.models.Circuit;
import cat.uvic.teknos.gt3.domain.repositories.CircuitRepository;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class JdbcCircuitRepository implements CircuitRepository {

    private static final String INSERT_CIRCUIT = "INSERT INTO CIRCUIT (CIRCUIT_NAME, COUNTRY, LENGTH_KM) VALUES (?, ?, ?)";
    private static final String UPDATE_CIRCUIT = "UPDATE CIRCUIT SET CIRCUIT_NAME = ?, COUNTRY = ?, LENGTH_KM = ? WHERE ID_CIRCUIT = ?";
    private static final String DELETE_CIRCUIT = "DELETE FROM CIRCUIT WHERE ID_CIRCUIT = ?";
    private static final String SELECT_CIRCUIT = "SELECT * FROM CIRCUIT WHERE ID_CIRCUIT = ?";
    private static final String SELECT_ALL_CIRCUITS = "SELECT * FROM CIRCUIT";

    private final Connection connection;

    public JdbcCircuitRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Circuit circuit) {
        if (circuit.getId() <= 0) {
            insert(circuit);
        } else {
            update(circuit);
        }
    }

    private void insert(Circuit circuit) {
        try (var preparedStatement = connection.prepareStatement(INSERT_CIRCUIT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, circuit.getCircuitName());
            preparedStatement.setString(2, circuit.getCountry());
            preparedStatement.setDouble(3, circuit.getLengthKm());
            preparedStatement.executeUpdate();

            var keys = preparedStatement.getGeneratedKeys();
            if (keys.next()) {
                circuit.setId(keys.getInt(1));
            } else {
                throw new SQLException("Creating circuit failed, no ID obtained.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void update(Circuit circuit) {
        try (var preparedStatement = connection.prepareStatement(UPDATE_CIRCUIT)) {
            preparedStatement.setString(1, circuit.getCircuitName());
            preparedStatement.setString(2, circuit.getCountry());
            preparedStatement.setDouble(3, circuit.getLengthKm());
            preparedStatement.setInt(4, circuit.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Circuit circuit) {
        try (var preparedStatement = connection.prepareStatement(DELETE_CIRCUIT)) {
            preparedStatement.setInt(1, circuit.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Circuit get(Integer id) {
        try (var preparedStatement = connection.prepareStatement(SELECT_CIRCUIT)) {
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Circuit circuit = new cat.uvic.teknos.gt3.file.jbdc.models.Circuit();
                circuit.setId(resultSet.getInt("ID_CIRCUIT"));
                circuit.setCircuitName(resultSet.getString("CIRCUIT_NAME"));
                circuit.setCountry(resultSet.getString("COUNTRY"));
                circuit.setLengthKm(resultSet.getFloat("LENGTH_KM"));
                return circuit;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Set<Circuit> getAll() {
        Set<Circuit> circuits = new HashSet<>();
        try (var preparedStatement = connection.prepareStatement(SELECT_ALL_CIRCUITS);
             var resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Circuit circuit = new cat.uvic.teknos.gt3.file.jbdc.models.Circuit();
                circuit.setId(resultSet.getInt("ID_CIRCUIT"));
                circuit.setCircuitName(resultSet.getString("CIRCUIT_NAME"));
                circuit.setCountry(resultSet.getString("COUNTRY"));
                circuit.setLengthKm(resultSet.getFloat("LENGTH_KM"));
                circuits.add(circuit);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return circuits;
    }
}

