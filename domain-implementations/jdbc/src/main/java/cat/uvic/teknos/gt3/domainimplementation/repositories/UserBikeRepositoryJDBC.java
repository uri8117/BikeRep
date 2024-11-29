package cat.uvic.teknos.gt3.domainimplementation.repositories;

import cat.uvic.teknos.gt3.domain.models.Bike;
import cat.uvic.teknos.gt3.domain.models.User;
import cat.uvic.teknos.gt3.domain.repositories.UserBikeRepository;
import cat.uvic.teknos.gt3.domainimplementation.entities.UserEntity;
import cat.uvic.teknos.gt3.domainimplementation.entities.BikeEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserBikeRepositoryJDBC implements UserBikeRepository {

    private final Connection connection;

    public UserBikeRepositoryJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addBikeToUser(User user, Bike bike) {
        String sql = "INSERT INTO user_bikes (user_id, bike_id) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, user.getId());  // userEntity tiene un método getId()
            stmt.setLong(2, bike.getId());  // bikeEntity tiene un método getId()
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeAllBikesFromUser(User user) {
        String sql = "DELETE FROM user_bikes WHERE user_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, user.getId());  // userEntity tiene un método getId()
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
