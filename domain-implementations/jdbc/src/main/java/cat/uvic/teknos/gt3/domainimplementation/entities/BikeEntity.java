package cat.uvic.teknos.gt3.domainimplementation.entities;

import cat.uvic.teknos.gt3.domain.models.Bike;
import cat.uvic.teknos.gt3.domain.models.BikeData;
import cat.uvic.teknos.gt3.domain.models.Brand;
import cat.uvic.teknos.gt3.domain.models.User;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class BikeEntity implements Bike {
    private Long id;
    private String model;
    private int year;
    private BikeData bikeData;
    private Brand brand;
    private Set<User> users = new HashSet<>();

    // Getters i setters

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public BikeData getBikeData() {
        return bikeData;
    }

    @Override
    public void setBikeData(BikeData bikeData) {
        this.bikeData = bikeData;
    }

    @Override
    public Brand getBrand() {
        return brand;
    }

    @Override
    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    @Override
    public Set<User> getUsers() {
        return users;
    }

    @Override
    public void setUsers(Set<User> users) {
        this.users = users;
    }

    // Mètode per desar Bike a la base de dades
    public void save(Connection conn) throws SQLException {
        String sql = "INSERT INTO bikes (model, year, brand_id, bike_data_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, model);
            stmt.setInt(2, year);
            stmt.setLong(3, brand.getId());  // Assumim que 'brand' és un objecte Brand carregat prèviament
            stmt.setLong(4, bikeData.getId()); // Assumim que 'bikeData' és un objecte BikeData carregat prèviament
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    this.id = rs.getLong(1);
                }
            }
        }
    }

    // Mètode per carregar un Bike des de la base de dades
    public static BikeEntity load(Long id, Connection conn) throws SQLException {
        String sql = "SELECT * FROM bikes WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BikeEntity bike = new BikeEntity();
                    bike.setId(rs.getLong("id"));
                    bike.setModel(rs.getString("model"));
                    bike.setYear(rs.getInt("year"));
                    // Aquí hauràs de carregar les relacions Brand i BikeData també
                    return bike;
                } else {
                    return null;
                }
            }
        }
    }
}
