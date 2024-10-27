package cat.uvic.teknos.gt3.file.jbdc.repositories;

import cat.uvic.teknos.gt3.domain.models.Brand;
import cat.uvic.teknos.gt3.domain.models.BrandData;
import cat.uvic.teknos.gt3.domain.models.Driver;
import cat.uvic.teknos.gt3.domain.repositories.BrandRepository;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class JdbcBrandRepository implements BrandRepository {

    private static final String INSERT_BRAND = "INSERT INTO BRAND (BRAND_NAME) VALUES (?)";
    private static final String INSERT_BRAND_DATA = "INSERT INTO BRAND_DATA (ID_BRAND, COUNTRY_OF_ORIGIN, CONTACT_INFO) VALUES (?, ?, ?)";
    private static final String UPDATE_BRAND = "UPDATE BRAND SET BRAND_NAME = ? WHERE ID_BRAND = ?";
    private static final String UPDATE_BRAND_DATA = "UPDATE BRAND_DATA SET COUNTRY_OF_ORIGIN = ?, CONTACT_INFO = ? WHERE ID_BRAND = ?";
    private static final String DELETE_BRAND = "DELETE FROM BRAND WHERE ID_BRAND = ?";
    private static final String SELECT_BRAND = "SELECT * FROM BRAND WHERE ID_BRAND = ?";
    private static final String SELECT_BRAND_DATA = "SELECT * FROM BRAND_DATA WHERE ID_BRAND = ?";
    private static final String SELECT_ALL_BRANDS = "SELECT * FROM BRAND";

    private final Connection connection;

    public JdbcBrandRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Brand brand) {
        if (brand.getId() <= 0) {
            insert(brand);
        } else {
            update(brand);
        }
    }

    private void insert(Brand brand) {
        try (var preparedStatement = connection.prepareStatement(INSERT_BRAND, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            preparedStatement.setString(1, brand.getBrandName());
            preparedStatement.executeUpdate();

            var keys = preparedStatement.getGeneratedKeys();
            if (keys.next()) {
                brand.setId(keys.getInt(1));
                insertBrandData(brand.getId(), brand.getBrandData());
            } else {
                throw new SQLException("Creating brand failed, no ID obtained.");
            }
            connection.commit();
        } catch (SQLException e) {
            rollback();
            throw new RuntimeException(e);
        } finally {
            setAutocommitTrue();
        }
    }

    private void insertBrandData(int brandId, BrandData brandData) {
        try (var preparedStatement = connection.prepareStatement(INSERT_BRAND_DATA)) {
            preparedStatement.setInt(1, brandId);
            preparedStatement.setString(2, brandData.getCountryOfOrigin());
            preparedStatement.setString(3, brandData.getContactInfo());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void update(Brand brand) {
        try (var preparedStatement = connection.prepareStatement(UPDATE_BRAND)) {
            connection.setAutoCommit(false);
            preparedStatement.setString(1, brand.getBrandName());
            preparedStatement.setInt(2, brand.getId());
            preparedStatement.executeUpdate();

            updateBrandData(brand.getId(), brand.getBrandData());
            connection.commit();
        } catch (SQLException e) {
            rollback();
            throw new RuntimeException(e);
        } finally {
            setAutocommitTrue();
        }
    }

    private void updateBrandData(int brandId, BrandData brandData) {
        try (var preparedStatement = connection.prepareStatement(UPDATE_BRAND_DATA)) {
            preparedStatement.setString(1, brandData.getCountryOfOrigin());
            preparedStatement.setString(2, brandData.getContactInfo());
            preparedStatement.setInt(3, brandId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Brand brand) {
        try (var preparedStatement = connection.prepareStatement(DELETE_BRAND)) {
            connection.setAutoCommit(false);
            preparedStatement.setInt(1, brand.getId());
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            rollback();
            throw new RuntimeException(e);
        } finally {
            setAutocommitTrue();
        }
    }

    @Override
    public Brand get(Integer id) {
        try (var preparedStatement = connection.prepareStatement(SELECT_BRAND);
             var brandDataStatement = connection.prepareStatement(SELECT_BRAND_DATA)) {

            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Brand brand = new cat.uvic.teknos.gt3.file.jbdc.models.Brand();
                brand.setId(resultSet.getInt("ID_BRAND"));
                brand.setBrandName(resultSet.getString("BRAND_NAME"));

                // Obtiene los datos adicionales de BrandData
                brandDataStatement.setInt(1, brand.getId());
                var brandDataResultSet = brandDataStatement.executeQuery();

                if (brandDataResultSet.next()) {
                    BrandData brandData = new cat.uvic.teknos.gt3.file.jbdc.models.BrandData();
                    brandData.setCountryOfOrigin(brandDataResultSet.getString("COUNTRY_OF_ORIGIN"));
                    brandData.setContactInfo(brandDataResultSet.getString("CONTACT_INFO"));
                    brand.setBrandData(brandData);
                }
                return brand;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Set<Brand> getAll() {
        Set<Brand> brands = new HashSet<>();

        // Consulta adicional para contar los coches asociados a cada marca
        String SELECT_CAR_COUNT_FOR_BRAND = "SELECT COUNT(*) AS car_count FROM CAR WHERE ID_BRAND = ?";

        try (var brandStatement = connection.prepareStatement(SELECT_ALL_BRANDS);
             var brandDataStatement = connection.prepareStatement(SELECT_BRAND_DATA)) {

            var brandResultSet = brandStatement.executeQuery();

            while (brandResultSet.next()) {
                Brand brand = new cat.uvic.teknos.gt3.file.jbdc.models.Brand();
                brand.setId(brandResultSet.getInt("ID_BRAND"));
                brand.setBrandName(brandResultSet.getString("BRAND_NAME"));

                // Obtener datos adicionales para cada brand
                brandDataStatement.setInt(1, brand.getId());
                var brandDataResultSet = brandDataStatement.executeQuery();

                if (brandDataResultSet.next()) {
                    BrandData brandData = new cat.uvic.teknos.gt3.file.jbdc.models.BrandData();
                    brandData.setCountryOfOrigin(brandDataResultSet.getString("COUNTRY_OF_ORIGIN"));
                    brandData.setContactInfo(brandDataResultSet.getString("CONTACT_INFO"));
                    brand.setBrandData(brandData);
                }
                brands.add(brand);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return brands;
    }

    private void setAutocommitTrue() {
        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void rollback() {
        try {
            connection.rollback();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
