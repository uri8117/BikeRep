package cat.uvic.teknos.gt3.domain.repositories;

import java.util.List;

public interface Repository<K, V> {
    void save(V model);
    void delete(V model);
    V get(K id);
    List<V> getAll();
}
