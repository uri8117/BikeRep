package cat.uvic.teknos.gt3.services.controllers;

import cat.uvic.teknos.gt3.domain.models.Brand;
import cat.uvic.teknos.gt3.domain.models.ModelFactory;
import cat.uvic.teknos.gt3.domain.repositories.RepositoryFactory;
import cat.uvic.teknos.gt3.domainimplementation.entities.BrandEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class BrandController implements Controller<Integer, Brand> {

    private final RepositoryFactory repositoryFactory;
    private final ObjectMapper objectMapper;

    public BrandController(RepositoryFactory repositoryFactory, ModelFactory modelFactory) {
        this.repositoryFactory = repositoryFactory;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String get(int id) {
        Brand brand = repositoryFactory.getBrandRepository().get((long) id);
        if (brand == null) {
            return "{\"message\": \"Brand not found.\"}";
        }
        try {
            return objectMapper.writeValueAsString(brand);
        } catch (IOException e) {
            e.printStackTrace();
            return "{\"message\": \"Error converting Brand to JSON.\"}";
        }
    }

    @Override
    public String get() {
        try {
            return objectMapper.writeValueAsString(repositoryFactory.getBrandRepository().getAll());
        } catch (IOException e) {
            e.printStackTrace();
            return "{\"message\": \"Error converting Brand list to JSON.\"}";
        }
    }

    @Override
    public void post(String json) {
        try {
            JsonNode node = objectMapper.readTree(json);

            BrandEntity brandEntity = new BrandEntity();
            brandEntity.setName(node.get("name").asText());

            // Guardar la nueva BrandEntity
            repositoryFactory.getBrandRepository().save(brandEntity);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void put(int id, String json) {
        try {
            JsonNode node = objectMapper.readTree(json);
            BrandEntity brandEntity = (BrandEntity) repositoryFactory.getBrandRepository().get((long) id);

            if (brandEntity != null) {
                brandEntity.setName(node.get("name").asText());

                // Actualizar la BrandEntity
                repositoryFactory.getBrandRepository().save(brandEntity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        BrandEntity brandEntity = (BrandEntity) repositoryFactory.getBrandRepository().get((long) id);
        if (brandEntity != null) {
            repositoryFactory.getBrandRepository().delete(brandEntity);
        }
    }
}
