//package cat.uvic.teknos.gt3.file.jpa.repository;
//
//import cat.uvic.teknos.gt3.domain.models.RaceDriverId;
//import cat.uvic.teknos.gt3.domain.repositories.RaceDriverIdRepository;
//import jakarta.persistence.EntityManager;
//
//import java.util.Set;
//
//public class JpaRaceDriverIdRepository implements RaceDriverIdRepository {
//    private final EntityManager entityManager;
//
//    public JpaRaceDriverIdRepository(EntityManager entityManager){
//        this.entityManager = entityManager;
//    }
//
//    @Override
//    public void save(RaceDriverId model) {
//        try{
//            entityManager.getTransaction().begin();
//            entityManager.persist(model);
//            entityManager.getTransaction().commit();
//        } catch (Exception e){
//            entityManager.getTransaction().rollback();
//            e.printStackTrace(); // Handle exception appropriately
//        }
//    }
//
//    @Override
//    public void delete(RaceDriverId model) {
//        try{
//            entityManager.getTransaction().begin();
//            entityManager.remove(model);
//            entityManager.getTransaction().commit();
//        } catch (Exception e){
//            entityManager.getTransaction().rollback();
//            e.printStackTrace(); // Handle exception appropriately
//        }
//    }
//
//    @Override
//    public RaceDriverId get(RaceDriverId id) {
//        return entityManager.find(RaceDriverId.class, id);
//    }
//
//    @Override
//    public void save(RaceDriverIdRepository model) {
//
//    }
//
//    @Override
//    public void delete(RaceDriverIdRepository model) {
//
//    }
//
//    @Override
//    public RaceDriverIdRepository get(Integer id) {
//        return null;
//    }
//
//    @Override
//    public Set<RaceDriverIdRepository> getAll() {
//        return Set.of();
//    }
//}
