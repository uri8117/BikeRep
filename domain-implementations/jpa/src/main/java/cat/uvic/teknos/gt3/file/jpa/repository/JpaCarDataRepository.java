package cat.uvic.teknos.gt3.file.jpa.repository;

import cat.uvic.teknos.gt3.domain.models.CarData;
import cat.uvic.teknos.gt3.domain.repositories.CarDataRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JpaCarDataRepository implements CarDataRepository {
    private final EntityManagerFactory entityManagerFactory;

    public JpaCarDataRepository(EntityManagerFactory entityManagerFactory){
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void save(CarData model) {
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
    public void delete(CarData model) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try{
            entityManager.getTransaction().begin();
            var carData = entityManager.find(cat.uvic.teknos.gt3.domain.models.CarData.class, model.getId());
            entityManager.remove(carData);
            entityManager.getTransaction().commit();
        } catch (Exception e){
            entityManager.getTransaction().rollback();
            e.printStackTrace(); // Handle exception appropriately
        }
    }

    @Override
    public CarData get(Integer id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager.find(cat.uvic.teknos.gt3.domain.models.CarData.class, id);
    }

    @Override
    public Set<CarData> getAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<CarData> carDataList = entityManager.createQuery("SELECT u FROM CarData u", CarData.class).getResultList();
        return new HashSet<>(carDataList);
    }
}
