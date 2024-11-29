package cat.uvic.teknos.gt3.domainimplementation.repositories;

import cat.uvic.teknos.gt3.domain.models.Bike;
import cat.uvic.teknos.gt3.domain.models.User;
import cat.uvic.teknos.gt3.domain.repositories.UserRepository;
import cat.uvic.teknos.gt3.domainimplementation.entities.BikeEntity;
import cat.uvic.teknos.gt3.domainimplementation.entities.UserEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UserRepositoryJDBC implements UserRepository {

    private final Connection conn;

    public UserRepositoryJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void save(User model) {
        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, model.getName());
            stmt.setString(2, model.getEmail());
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long userId = generatedKeys.getLong(1);
                        model.setId(userId);

                        // Vincular bicicleta inicial
                        if (model.getBikes() != null && !model.getBikes().isEmpty()) {
                            for (Bike bike : model.getBikes()) {
                                String sqlUserBike = "INSERT INTO user_bikes (user_id, bike_id) VALUES (?, ?)";
                                try (PreparedStatement stmtUserBike = conn.prepareStatement(sqlUserBike)) {
                                    stmtUserBike.setLong(1, userId);
                                    stmtUserBike.setLong(2, bike.getId());
                                    stmtUserBike.executeUpdate();
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void delete(User model) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, model.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User get(Long id) {
        User user = null;
        String sqlUser = "SELECT * FROM users WHERE id = ?";
        String sqlBikes = "SELECT b.* FROM bikes b INNER JOIN user_bikes ub ON b.id = ub.bike_id WHERE ub.user_id = ?";

        try (PreparedStatement stmtUser = conn.prepareStatement(sqlUser)) {
            stmtUser.setLong(1, id);
            try (ResultSet rsUser = stmtUser.executeQuery()) {
                if (rsUser.next()) {
                    user = new UserEntity();
                    user.setId(rsUser.getLong("id"));
                    user.setName(rsUser.getString("name"));
                    user.setEmail(rsUser.getString("email"));

                    // Obtenir les bicicletes associades
                    try (PreparedStatement stmtBikes = conn.prepareStatement(sqlBikes)) {
                        stmtBikes.setLong(1, id);
                        try (ResultSet rsBikes = stmtBikes.executeQuery()) {
                            List<Bike> bikes = new ArrayList<>();
                            while (rsBikes.next()) {
                                Bike bike = new BikeEntity();
                                bike.setId(rsBikes.getLong("id"));
                                bike.setModel(rsBikes.getString("model"));
                                bike.setYear(rsBikes.getInt("year"));

                                bikes.add(bike);
                            }
                            user.setBikes((Set<Bike>) bikes); // Assignar la llista de bicicletes
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }


    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                User user = new UserEntity();
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
