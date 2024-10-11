package cat.uvic.teknos.gt3.file.jpa.repository;

import cat.uvic.teknos.gt3.domain.models.Race;
import cat.uvic.teknos.gt3.domain.repositories.RaceRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JpaRaceRepository implements RaceRepository {
    private final EntityManagerFactory entityManagerFactory;

    public JpaRaceRepository(EntityManagerFactory entityManagerFactory){
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void save(Race model) {
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
    public void delete(Race model) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try{
            entityManager.getTransaction().begin();
            var race = entityManager.find(cat.uvic.teknos.gt3.domain.models.Race.class, model.getId());
            entityManager.remove(race);
            entityManager.getTransaction().commit();
        } catch (Exception e){
            entityManager.getTransaction().rollback();
            e.printStackTrace(); // Handle exception appropriately
        }
    }

    @Override
    public Race get(Integer id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager.find(cat.uvic.teknos.gt3.domain.models.Race.class, id);
    }

    @Override
    public Set<Race> getAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Race> raceList = entityManager.createQuery("SELECT u FROM Race u", Race.class).getResultList();
        return new HashSet<>(raceList);
    }
}
