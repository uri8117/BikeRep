package cat.uvic.teknos.gt3.domain.repositories;

import java.util.Set;

public interface Repository<K, V> {
    void save(V model);
    void delete(V model);
    V get(K id);
    Set<V> getAll();
}
