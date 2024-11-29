package cat.uvic.teknos.gt3.domainimplementation.entities;

import cat.uvic.teknos.gt3.domain.models.Bike;
import cat.uvic.teknos.gt3.domain.models.BikeData;

import java.sql.*;

public class BikeDataEntity implements BikeData {
    private Long id;
    private double engineCapacity;
    private double weight;
    private Bike bike;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public double getEngineCapacity() {
        return engineCapacity;
    }

    @Override
    public void setEngineCapacity(double engineCapacity) {
        this.engineCapacity = engineCapacity;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public Bike getBike() {
        return bike;
    }

    @Override
    public void setBike(Bike bike) {
        this.bike = bike;
    }

    // Mètode per desar BikeData a la base de dades
    public void save(Connection conn) throws SQLException {
        String sql = "INSERT INTO bike_data (engine_capacity, weight, bike_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDouble(1, engineCapacity);
            stmt.setDouble(2, weight);
            stmt.setLong(3, bike.getId());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    this.id = rs.getLong(1);
                }
            }
        }
    }

    // Mètode per carregar BikeData des de la base de dades
    public static BikeDataEntity load(Long id, Connection conn) throws SQLException {
        String sql = "SELECT * FROM bike_data WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BikeDataEntity bikeData = new BikeDataEntity();
                    bikeData.setId(rs.getLong("id"));
                    bikeData.setEngineCapacity(rs.getDouble("engine_capacity"));
                    bikeData.setWeight(rs.getDouble("weight"));
                    // Carregar la relació Bike
                    return bikeData;
                } else {
                    return null;
                }
            }
        }
    }
}
