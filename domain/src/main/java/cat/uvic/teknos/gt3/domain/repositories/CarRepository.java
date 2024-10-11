package cat.uvic.teknos.gt3.domain.repositories;

import cat.uvic.teknos.gt3.domain.models.Car;
import cat.uvic.teknos.gt3.domain.models.Race;

import java.util.Set;

public interface CarRepository extends Repository<Integer, Car>{
    @Override
    void save(Car model);

    @Override
    void delete(Car model);

    @Override
    Car get(Integer id);

    @Override
    Set<Car> getAll();
}
