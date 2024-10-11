package cat.uvic.teknos.gt3.file.jpa.repository;

import cat.uvic.teknos.gt3.domain.models.Driver;
import cat.uvic.teknos.gt3.domain.repositories.DriverRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JpaDriverRepository implements DriverRepository {
    private final EntityManagerFactory entityManagerFactory;

    public JpaDriverRepository(EntityManagerFactory entityManagerFactory){
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void save(Driver model) {
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
    public void delete(Driver model) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try{
            entityManager.getTransaction().begin();
            var driver = entityManager.find(cat.uvic.teknos.gt3.domain.models.Driver.class, model.getId());
            entityManager.remove(driver);
            entityManager.getTransaction().commit();
        } catch (Exception e){
            entityManager.getTransaction().rollback();
            e.printStackTrace(); // Handle exception appropriately
        }
    }

    @Override
    public Driver get(Integer id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager.find(cat.uvic.teknos.gt3.domain.models.Driver.class, id);
    }

    @Override
    public Set<Driver> getAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Driver> driverList = entityManager.createQuery("SELECT u FROM Driver u", Driver.class).getResultList();
        return new HashSet<>(driverList);
    }
}
