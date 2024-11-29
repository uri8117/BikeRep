package cat.uvic.teknos.gt3.domainimplementation.repositories;

import cat.uvic.teknos.gt3.domain.models.Brand;
import cat.uvic.teknos.gt3.domain.repositories.BrandRepository;
import cat.uvic.teknos.gt3.domainimplementation.entities.BrandEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BrandRepositoryJDBC implements BrandRepository {

    private final Connection conn;

    public BrandRepositoryJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void save(Brand model) {
        String sql = "INSERT INTO brands (name, country) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, model.getName());
            stmt.setString(2, model.getCountry());
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long brandId = generatedKeys.getLong(1);
                        model.setId(brandId);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Brand model) {
        String sql = "DELETE FROM brands WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, model.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Brand get(Long id) {
        Brand brand = null;
        String sql = "SELECT * FROM brands WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    brand = new BrandEntity();
                    brand.setId(rs.getLong("id"));
                    brand.setName(rs.getString("name"));
                    brand.setCountry(rs.getString("country"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return brand;
    }

    @Override
    public List<Brand> getAll() {
        List<Brand> brands = new ArrayList<>();
        String sql = "SELECT * FROM brands";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Brand brand = new BrandEntity();
                brand.setId(rs.getLong("id"));
                brand.setName(rs.getString("name"));
                brand.setCountry(rs.getString("country"));
                brands.add(brand);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return brands;
    }
}
