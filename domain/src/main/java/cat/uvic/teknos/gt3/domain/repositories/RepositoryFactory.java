package cat.uvic.teknos.gt3.domain.repositories;


import java.sql.SQLException;

public interface RepositoryFactory {

    BikeRepository getBikeRepository();

    BrandRepository getBrandRepository();

    UserRepository getUserRepository();

    UserBikeRepository getUserBikeRepository();

}
