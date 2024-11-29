package cat.uvic.teknos.gt3.domainimplementation.repositories;

import cat.uvic.teknos.gt3.domain.models.Bike;
import cat.uvic.teknos.gt3.domain.models.BikeData;
import cat.uvic.teknos.gt3.domain.repositories.BikeRepository;
import cat.uvic.teknos.gt3.domainimplementation.entities.BikeDataEntity;
import cat.uvic.teknos.gt3.domainimplementation.entities.BikeEntity;
import cat.uvic.teknos.gt3.domainimplementation.entities.BrandEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BikeRepositoryJDBC implements BikeRepository {

    private final Connection conn;

    public BikeRepositoryJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void save(Bike model) {
        String sql = "INSERT INTO bikes (model, year, brand_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, model.getModel());
            stmt.setInt(2, model.getYear());
            stmt.setLong(3, model.getBrand().getId());
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long bikeId = generatedKeys.getLong(1);
                        model.setId(bikeId);

                        // Desar BikeData si está presente
                        if (model.getBikeData() != null) {
                            saveBikeData(model.getBikeData(), bikeId);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveBikeData(BikeData bikeData, Long bikeId) {
        String sql = "INSERT INTO bike_data (engine_capacity, weight, bike_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, bikeData.getEngineCapacity());
            stmt.setDouble(2, bikeData.getWeight());
            stmt.setLong(3, bikeId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Bike model) {
        if (model.getId() == null) {
            throw new IllegalArgumentException("Bike ID cannot be null when deleting a bike");
        }

        String sql = "DELETE FROM bikes WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, model.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Bike model) {
        if (model.getId() == null) {
            throw new IllegalArgumentException("Bike ID cannot be null when updating a bike");
        }

        String sql = "UPDATE bikes SET model = ?, year = ?, brand_id = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, model.getModel());
            stmt.setInt(2, model.getYear());
            stmt.setLong(3, model.getBrand().getId());
            stmt.setLong(4, model.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Bike get(Long id) {
        Bike bike = null;
        String sql = "SELECT * FROM bikes WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    bike = new BikeEntity();
                    bike.setId(rs.getLong("id"));
                    bike.setModel(rs.getString("model"));
                    bike.setYear(rs.getInt("year"));

                    // Carregar el Brand
                    BrandEntity brand = new BrandEntity();
                    brand.setId(rs.getLong("brand_id"));
                    bike.setBrand(brand);

                    // Carregar BikeData
                    BikeDataEntity bikeData = BikeDataEntity.load(rs.getLong("id"), conn);
                    bike.setBikeData(bikeData);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bike;
    }

    @Override
    public List<Bike> getAll() {
        List<Bike> bikes = new ArrayList<>();
        String sql = "SELECT * FROM bikes";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Bike bike = new BikeEntity();
                bike.setId(rs.getLong("id"));
                bike.setModel(rs.getString("model"));
                bike.setYear(rs.getInt("year"));

                // Carregar el Brand
                BrandEntity brand = new BrandEntity();
                brand.setId(rs.getLong("brand_id"));
                bike.setBrand(brand);

                // Carregar BikeData
                BikeDataEntity bikeData = BikeDataEntity.load(rs.getLong("id"), conn);
                bike.setBikeData(bikeData);

                bikes.add(bike);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bikes;
    }
}
