package cat.uvic.teknos.gt3.file.jdbc.test;

import cat.uvic.teknos.gt3.domain.models.Bike;
import cat.uvic.teknos.gt3.domain.models.BikeData;
import cat.uvic.teknos.gt3.domain.models.Brand;
import cat.uvic.teknos.gt3.domainimplementation.entities.BikeEntity;
import cat.uvic.teknos.gt3.domainimplementation.entities.BikeDataEntity;
import cat.uvic.teknos.gt3.domainimplementation.entities.BrandEntity;
import cat.uvic.teknos.gt3.domainimplementation.repositories.BikeRepositoryJDBC;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class BikeRepositoryTest {

    private static Connection connection;
    private static BikeRepositoryJDBC bikeRepository;

    @BeforeAll
    public static void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bike_rep", "admin", "password");
        bikeRepository = new BikeRepositoryJDBC(connection);
    }

    @AfterAll
    public static void tearDown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void testSaveBike() throws SQLException {
        BikeData bikeData = new BikeDataEntity();
        bikeData.setEngineCapacity(150.0);
        bikeData.setWeight(20.5);

        Brand brand = new BrandEntity();
        brand.setId(2L); // Asumimos que existe una marca con ID 2

        Bike bike = new BikeEntity();
        bike.setModel("TestModel");
        bike.setYear(2022);
        bike.setBrand(brand);
        bike.setBikeData(bikeData);

        bikeRepository.save(bike);

        assertNotNull(bike.getId());  // El ID debe ser asignado
        assertTrue(bike.getId() > 0); // El ID debe ser mayor que 0

        String checkBikeDataSQL = "SELECT * FROM bike_data WHERE bike_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(checkBikeDataSQL)) {
            stmt.setLong(1, bike.getId());
            ResultSet resultSet = stmt.executeQuery();
            assertTrue(resultSet.next());
            assertEquals(bikeData.getEngineCapacity(), resultSet.getDouble("engine_capacity"));
            assertEquals(bikeData.getWeight(), resultSet.getDouble("weight"));
        }
    }

    @Test
    public void testDeleteBike() throws SQLException {
        // Crear una nueva bicicleta y guardarla
        Bike bike = createTestBike();

        // Verificar que la bicicleta tiene un ID antes de intentar eliminarla
        assertNotNull(bike.getId(), "La bicicleta debe tener un ID asignado.");

        // Eliminar la bicicleta y su BikeData
        bikeRepository.delete(bike);

        // Verificar que la bicicleta fue eliminada
        String checkBikeSQL = "SELECT * FROM bikes WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(checkBikeSQL)) {
            stmt.setLong(1, bike.getId());
            ResultSet resultSet = stmt.executeQuery();
            assertFalse(resultSet.next(), "La bicicleta debería haber sido eliminada.");
        }

        // Verificar que BikeData también fue eliminada
        String checkBikeDataSQL = "SELECT * FROM bike_data WHERE bike_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(checkBikeDataSQL)) {
            stmt.setLong(1, bike.getId());
            ResultSet resultSet = stmt.executeQuery();
            assertFalse(resultSet.next(), "Los datos de la bicicleta deberían haber sido eliminados.");
        }
    }

    @Test
    public void testUpdateBikeAndData() throws SQLException {
        // Crear una bicicleta de prueba y guardarla
        Bike bike = createTestBike();

        // Actualizar los datos de la bicicleta
        String updateBikeSQL = "UPDATE bikes SET model = ?, year = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(updateBikeSQL)) {
            stmt.setString(1, "UpdatedModel");
            stmt.setInt(2, 2023);
            stmt.setLong(3, bike.getId());
            int rowsAffected = stmt.executeUpdate();
            assertEquals(1, rowsAffected);
        }

        // Actualizar los datos de la bicicleta (BikeData)
        String updateBikeDataSQL = "UPDATE bike_data SET engine_capacity = ?, weight = ? WHERE bike_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(updateBikeDataSQL)) {
            stmt.setDouble(1, 200.0);  // Actualizar engine_capacity
            stmt.setDouble(2, 25.5);   // Actualizar weight
            stmt.setLong(3, bike.getId());
            int rowsAffected = stmt.executeUpdate();
            assertEquals(1, rowsAffected);
        }

        // Verificar que la bicicleta fue actualizada correctamente
        String selectBikeSQL = "SELECT * FROM bikes WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(selectBikeSQL)) {
            stmt.setLong(1, bike.getId());
            ResultSet resultSet = stmt.executeQuery();
            assertTrue(resultSet.next());
            assertEquals("UpdatedModel", resultSet.getString("model"));
            assertEquals(2023, resultSet.getInt("year"));
        }

        // Verificar que los datos de la bicicleta fueron actualizados correctamente
        String selectBikeDataSQL = "SELECT * FROM bike_data WHERE bike_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(selectBikeDataSQL)) {
            stmt.setLong(1, bike.getId());
            ResultSet resultSet = stmt.executeQuery();
            assertTrue(resultSet.next());
            assertEquals(200.0, resultSet.getDouble("engine_capacity"));
            assertEquals(25.5, resultSet.getDouble("weight"));
        }
    }

    @Test
    public void testGetBikeById() throws SQLException {
        Bike bike = createTestBike();

        Bike fetchedBike = bikeRepository.get(bike.getId());

        assertNotNull(fetchedBike);
        assertEquals(bike.getId(), fetchedBike.getId());
        assertEquals(bike.getModel(), fetchedBike.getModel());
        assertEquals(bike.getYear(), fetchedBike.getYear());
        assertEquals(bike.getBrand().getId(), fetchedBike.getBrand().getId());

        assertNotNull(fetchedBike.getBikeData());
        assertEquals(bike.getBikeData().getEngineCapacity(), fetchedBike.getBikeData().getEngineCapacity());
        assertEquals(bike.getBikeData().getWeight(), fetchedBike.getBikeData().getWeight());
    }

    @Test
    public void testGetAllBikes() throws SQLException {
        Bike bike1 = createTestBike();
        Bike bike2 = createTestBike();

        List<Bike> bikes = bikeRepository.getAll();

        assertTrue(bikes.size() > 0);
        assertTrue(bikes.stream().anyMatch(b -> b.getId().equals(bike1.getId())));
        assertTrue(bikes.stream().anyMatch(b -> b.getId().equals(bike2.getId())));
    }

    private Bike createTestBike() throws SQLException {
        BikeData bikeData = new BikeDataEntity();
        bikeData.setEngineCapacity(150.0);
        bikeData.setWeight(20.5);

        Brand brand = new BrandEntity();
        brand.setId(1L);

        Bike bike = new BikeEntity();
        bike.setModel("TestModel");
        bike.setYear(2022);
        bike.setBrand(brand);
        bike.setBikeData(bikeData);

        bikeRepository.save(bike);

        return bike;
    }
}
