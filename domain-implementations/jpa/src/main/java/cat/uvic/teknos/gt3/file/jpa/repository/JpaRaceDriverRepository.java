//package cat.uvic.teknos.gt3.file.jpa.repository;
//
//import cat.uvic.teknos.gt3.domain.models.RaceDriver;
//import cat.uvic.teknos.gt3.domain.repositories.RaceDriverRepository;
//import cat.uvic.teknos.gt3.file.jpa.models.RaceDriverImpl;
//import jakarta.persistence.EntityManager;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//public class JpaRaceDriverRepository implements RaceDriverRepository {
//    private final EntityManager entityManager;
//
//    public JpaRaceDriverRepository(EntityManager entityManager){
//        this.entityManager = entityManager;
//    }
//
//    @Override
//    public void save(RaceDriver model) {
//        try{
//            entityManager.getTransaction().begin();
//            if(model.getId() <= 0){
//                entityManager.persist(model);
//            } else if(!entityManager.contains(model)){
//                entityManager.merge(model);
//            }
//            entityManager.getTransaction().commit();
//        } catch (Exception e){
//            entityManager.getTransaction().rollback();
//            e.printStackTrace(); // Handle exception appropriately
//        }
//    }
//
//    @Override
//    public void delete(RaceDriver model) {
//        try{
//            entityManager.getTransaction().begin();
//            var raceDriver = entityManager.find(cat.uvic.teknos.gt3.domain.models.RaceDriver.class, model.getId());
//            entityManager.remove(raceDriver);
//            entityManager.getTransaction().commit();
//        } catch (Exception e){
//            entityManager.getTransaction().rollback();
//            e.printStackTrace(); // Handle exception appropriately
//        }
//    }
//
//    @Override
//    public RaceDriver get(Integer id) {
//        return entityManager.find(cat.uvic.teknos.gt3.domain.models.RaceDriver.class, id);
//    }
//
//    @Override
//    public Set<RaceDriver> getAll() {
//        List<RaceDriver> raceDriverList = entityManager.createQuery("SELECT u FROM RaceDriverImpl u", RaceDriver.class).getResultList();
//        return new HashSet<>(raceDriverList);
//    }
//}
