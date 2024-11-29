package cat.uvic.teknos.gt3.file.jdbc.test;

import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTest {
    private static Connection connection;

    @BeforeAll
    public static void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bike_rep", "admin", "password");
    }

    @AfterAll
    public static void tearDown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void testCreateUser() throws SQLException {
        String insertSQL = "INSERT INTO users (name, email) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertSQL)) {
            statement.setString(1, "TestUser");
            statement.setString(2, "testuser@example.com");
            int rowsAffected = statement.executeUpdate();
            assertEquals(1, rowsAffected);
        }
    }

    @Test
    public void testUpdateUser() throws SQLException {
        String updateSQL = "UPDATE users SET email = ? WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(updateSQL)) {
            statement.setString(1, "newemail@example.com");
            statement.setString(2, "TestUser");
            int rowsAffected = statement.executeUpdate();
            assertEquals(1, rowsAffected);
        }

        String selectSQL = "SELECT * FROM users WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(selectSQL)) {
            statement.setString(1, "TestUser");
            ResultSet resultSet = statement.executeQuery();
            assertTrue(resultSet.next());
            assertEquals("newemail@example.com", resultSet.getString("email"));
        }
    }

    @Test
    public void testDeleteUser() throws SQLException {
        String deleteSQL = "DELETE FROM users WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(deleteSQL)) {
            statement.setString(1, "TestUser");
            int rowsAffected = statement.executeUpdate();
            assertEquals(1, rowsAffected);
        }

        String selectSQL = "SELECT * FROM users WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(selectSQL)) {
            statement.setString(1, "TestUser");
            ResultSet resultSet = statement.executeQuery();
            assertFalse(resultSet.next());
        }
    }

    @Test
    public void testGetUserById() throws SQLException {
        // Inserir un nuevo usuario para hacer el test
        String insertSQL = "INSERT INTO users (name, email) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, "TestGetUser");
            statement.setString(2, "testgetuser@example.com");
            int rowsAffected = statement.executeUpdate();
            assertEquals(1, rowsAffected);

            // Obtener el ID generado
            ResultSet generatedKeys = statement.getGeneratedKeys();
            long userId = -1;
            if (generatedKeys.next()) {
                userId = generatedKeys.getLong(1);
            }

            // Test de obtener el usuario por ID
            String selectSQL = "SELECT * FROM users WHERE id = ?";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectSQL)) {
                selectStatement.setLong(1, userId);
                ResultSet resultSet = selectStatement.executeQuery();
                assertTrue(resultSet.next());
                assertEquals("TestGetUser", resultSet.getString("name"));
                assertEquals("testgetuser@example.com", resultSet.getString("email"));
            }
        }
    }

    @Test
    public void testGetAllUsers() throws SQLException {
        // Obtener todos los usuarios
        String selectSQL = "SELECT * FROM users";
        try (PreparedStatement statement = connection.prepareStatement(selectSQL)) {
            ResultSet resultSet = statement.executeQuery();
            List<String> userNames = new ArrayList<>();
            while (resultSet.next()) {
                userNames.add(resultSet.getString("name"));
            }

            // Asegurarse que la lista de usuarios no está vacía
            assertTrue(userNames.size() > 0);
            // Puedes agregar más verificaciones dependiendo de tus datos insertados
            assertTrue(userNames.contains("TestUser"));
        }
    }
}
