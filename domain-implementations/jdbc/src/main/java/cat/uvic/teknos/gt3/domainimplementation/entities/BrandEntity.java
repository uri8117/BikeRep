package cat.uvic.teknos.gt3.domainimplementation.entities;

import cat.uvic.teknos.gt3.domain.models.Brand;
import cat.uvic.teknos.gt3.domain.models.Bike;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BrandEntity implements Brand {
    private Long id;
    private String name;
    private String country;

    private List<Bike> bikes = new ArrayList<>();

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
    public String getCountry() {
        return country;
    }

    @Override
    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public List<Bike> getBikes() {
        return bikes;
    }

    @Override
    public void setBikes(List<Bike> bikes) {
        this.bikes = bikes;
    }

    // Método para insertar Brand a la base de datos
    public void save(Connection conn) throws SQLException {
        String sql = "INSERT INTO brands (name, country) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.setString(2, country);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    this.id = rs.getLong(1);
                }
            }
        }
    }

    // Método para cargar Brand desde la base de datos
    public static BrandEntity load(Long id, Connection conn) throws SQLException {
        String sql = "SELECT * FROM brands WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BrandEntity brand = new BrandEntity();
                    brand.setId(rs.getLong("id"));
                    brand.setName(rs.getString("name"));
                    brand.setCountry(rs.getString("country"));
                    return brand;
                } else {
                    return null;
                }
            }
        }
    }
}