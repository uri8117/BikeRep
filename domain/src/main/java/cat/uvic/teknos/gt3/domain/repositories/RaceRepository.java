package cat.uvic.teknos.gt3.domain.repositories;

import cat.uvic.teknos.gt3.domain.models.Car;
import cat.uvic.teknos.gt3.domain.models.Race;

import java.util.Set;

public interface RaceRepository extends Repository<Integer, Race>{
    @Override
    void save(Race model);

    @Override
    void delete(Race model);

    @Override
    Race get(Integer id);

    @Override
    Set<Race> getAll();
}
