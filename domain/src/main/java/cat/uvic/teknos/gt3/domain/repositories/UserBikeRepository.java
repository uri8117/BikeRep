package cat.uvic.teknos.gt3.domain.repositories;

import cat.uvic.teknos.gt3.domain.models.Bike;
import cat.uvic.teknos.gt3.domain.models.User;

public interface UserBikeRepository {

    void addBikeToUser(User user, Bike bike);

    void removeAllBikesFromUser(User user);
}
