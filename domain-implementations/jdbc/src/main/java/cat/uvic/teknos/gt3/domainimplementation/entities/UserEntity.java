package cat.uvic.teknos.gt3.domainimplementation.entities;

import cat.uvic.teknos.gt3.domain.models.User;
import cat.uvic.teknos.gt3.domain.models.Bike;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class UserEntity implements User {
    private Long id;
    private String name;
    private String email;
    private Set<Bike> bikes = new HashSet<>();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Set<Bike> getBikes() {
        return bikes;
    }

    @Override
    public void setBikes(Set<Bike> bikes) {
        this.bikes = bikes;
    }

    // Mètode per desar User a la base de dades
    public void save(Connection conn) throws SQLException {
        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    this.id = rs.getLong(1);
                }
            }
        }
    }

    // Mètode per carregar User des de la base de dades
    public static UserEntity load(Long id, Connection conn) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    UserEntity user = new UserEntity();
                    user.setId(rs.getLong("id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    return user;
                } else {
                    return null;
                }
            }
        }
    }

    public void addBike(BikeEntity bikeEntity) {
        bikes.add(bikeEntity);
    }
}
