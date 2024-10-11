package cat.uvic.teknos.gt3.file.jpa.repository;

import cat.uvic.teknos.gt3.domain.models.Car;
import cat.uvic.teknos.gt3.domain.repositories.CarRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JpaCarRepository implements CarRepository {
    private final EntityManagerFactory entityManagerFactory;

    public JpaCarRepository(EntityManagerFactory entityManagerFactory){
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void save(Car model) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try{
            entityManager.getTransaction().begin();
            if(model.getId() <= 0){
                entityManager.persist(model);
            } else if(!entityManager.contains(model)){
                entityManager.merge(model);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e){
            entityManager.getTransaction().rollback();
            e.printStackTrace(); // Handle exception appropriately
        }
    }

    @Override
    public void delete(Car model) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try{
            entityManager.getTransaction().begin();
            var car = entityManager.find(cat.uvic.teknos.gt3.domain.models.Car.class, model.getId());
            entityManager.remove(car);
            entityManager.getTransaction().commit();
        } catch (Exception e){
            entityManager.getTransaction().rollback();
            e.printStackTrace(); // Handle exception appropriately
        }
    }

    @Override
    public Car get(Integer id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager.find(cat.uvic.teknos.gt3.domain.models.Car.class, id);
    }

    @Override
    public Set<Car> getAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Car> carList = entityManager.createQuery("SELECT u FROM Car u", Car.class).getResultList();
        return new HashSet<>(carList);
    }
}
