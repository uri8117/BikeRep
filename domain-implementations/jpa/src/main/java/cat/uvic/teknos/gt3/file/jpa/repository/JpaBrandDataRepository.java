package cat.uvic.teknos.gt3.file.jpa.repository;

import cat.uvic.teknos.gt3.domain.models.BrandData;
import cat.uvic.teknos.gt3.domain.repositories.BrandDataRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JpaBrandDataRepository implements BrandDataRepository {
    private final EntityManagerFactory entityManagerFactory;

    public JpaBrandDataRepository(EntityManagerFactory entityManagerFactory){
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void save(BrandData model) {
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
    public void delete(BrandData model) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try{
            entityManager.getTransaction().begin();
            var brandData = entityManager.find(cat.uvic.teknos.gt3.domain.models.BrandData.class, model.getId());
            entityManager.remove(brandData);
            entityManager.getTransaction().commit();
        } catch (Exception e){
            entityManager.getTransaction().rollback();
            e.printStackTrace(); // Handle exception appropriately
        }
    }

    @Override
    public BrandData get(Integer id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager.find(cat.uvic.teknos.gt3.domain.models.BrandData.class, id);
    }

    @Override
    public Set<BrandData> getAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<BrandData> brandDataList = entityManager.createQuery("SELECT u FROM BrandData u", BrandData.class).getResultList();
        return new HashSet<>(brandDataList);
    }
}
