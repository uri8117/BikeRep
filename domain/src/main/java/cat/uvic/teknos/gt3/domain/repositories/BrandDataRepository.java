package cat.uvic.teknos.gt3.domain.repositories;

import cat.uvic.teknos.gt3.domain.models.BrandData;

import java.util.Set;

public interface BrandDataRepository extends Repository<Integer, BrandData>{
    @Override
    void save(BrandData model);

    @Override
    void delete(BrandData model);

    @Override
    BrandData get(Integer id);

    @Override
    Set<BrandData> getAll();
}
