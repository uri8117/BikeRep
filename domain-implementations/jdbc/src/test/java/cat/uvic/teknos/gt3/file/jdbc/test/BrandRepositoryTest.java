package cat.uvic.teknos.gt3.file.jdbc.test;

import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class BrandRepositoryTest {
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
    public void testCreateBrand() throws SQLException {
        String insertSQL = "INSERT INTO brands (name, country) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertSQL)) {
            statement.setString(1, "TestBrand");
            statement.setString(2, "Country");
            int rowsAffected = statement.executeUpdate();
            assertEquals(1, rowsAffected);
        }
    }

    @Test
    public void testUpdateBrand() throws SQLException {
        String updateSQL = "UPDATE brands SET country = ? WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(updateSQL)) {
            statement.setString(1, "NewCountry");
            statement.setString(2, "TestBrand");
            int rowsAffected = statement.executeUpdate();
            assertEquals(1, rowsAffected);
        }

        String selectSQL = "SELECT * FROM brands WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(selectSQL)) {
            statement.setString(1, "TestBrand");
            ResultSet resultSet = statement.executeQuery();
            assertTrue(resultSet.next());
            assertEquals("NewCountry", resultSet.getString("country"));
        }
    }

    @Test
    public void testDeleteBrand() throws SQLException {
        String deleteSQL = "DELETE FROM brands WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(deleteSQL)) {
            statement.setString(1, "Yamaha");
            int rowsAffected = statement.executeUpdate();
            assertEquals(1, rowsAffected);
        }

        String selectSQL = "SELECT * FROM brands WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(selectSQL)) {
            statement.setString(1, "Yamaha");
            ResultSet resultSet = statement.executeQuery();
            assertFalse(resultSet.next());
        }
    }

    @Test
    public void testGetBrandById() throws SQLException {
        String insertSQL = "INSERT INTO brands (name, country) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, "TestBrand");
            statement.setString(2, "Country");
            int rowsAffected = statement.executeUpdate();
            assertEquals(1, rowsAffected);

            // Obtener el ID generado
            ResultSet generatedKeys = statement.getGeneratedKeys();
            long brandId = -1;
            if (generatedKeys.next()) {
                brandId = generatedKeys.getLong(1);
            }

            // Obtener la marca por ID
            String selectSQL = "SELECT * FROM brands WHERE id = ?";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectSQL)) {
                selectStatement.setLong(1, brandId);
                ResultSet resultSet = selectStatement.executeQuery();
                assertTrue(resultSet.next());
                assertEquals("TestBrand", resultSet.getString("name"));
                assertEquals("Country", resultSet.getString("country"));
            }
        }
    }

    @Test
    public void testGetAllBrands() throws SQLException {
        String selectSQL = "SELECT * FROM brands";
        try (PreparedStatement statement = connection.prepareStatement(selectSQL)) {
            ResultSet resultSet = statement.executeQuery();
            List<String> brandNames = new ArrayList<>();
            while (resultSet.next()) {
                brandNames.add(resultSet.getString("name"));
            }

            // Asegurarse que la lista no esté vacía
            assertTrue(brandNames.size() > 0);
            // Puedes verificar más marcas si has insertado previamente datos
            assertTrue(brandNames.contains("TestBrand"));
        }
    }
}
