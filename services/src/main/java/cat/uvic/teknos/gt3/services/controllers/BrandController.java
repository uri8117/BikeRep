package cat.uvic.teknos.gt3.services.controllers;

import cat.uvic.teknos.gt3.domain.models.Brand;
import cat.uvic.teknos.gt3.domain.models.BrandData;
import cat.uvic.teknos.gt3.domain.models.ModelFactory;
import cat.uvic.teknos.gt3.domain.repositories.RepositoryFactory;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BrandController implements Controller {

    private final RepositoryFactory repositoryFactory;
    private final ModelFactory modelFactory;
    private final ObjectMapper mapper;

    public BrandController(RepositoryFactory repositoryFactory, ModelFactory modelFactory) {
        this.repositoryFactory = repositoryFactory;
        this.modelFactory = modelFactory;
        this.mapper = new ObjectMapper();

        this.mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Override
    public String get(int id) {
        var brand = repositoryFactory.getBrandRepository().get(id);
        if (brand == null) {
            throw new RuntimeException("Brand not found");
        }
        try {
            return mapper.writeValueAsString(brand);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting brand to JSON", e);
        }
    }

    @Override
    public String get() {
        var brands = repositoryFactory.getBrandRepository().getAll();
        try {
            return mapper.writeValueAsString(brands);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting brands list to JSON", e);
        }
    }

    @Override
    public void post(String value) {
        try {
            JsonNode rootNode = mapper.readTree(value);
            Brand brand = new cat.uvic.teknos.gt3.file.jbdc.models.Brand();
            BrandData brandData = new cat.uvic.teknos.gt3.file.jbdc.models.BrandData();
            brand.setBrandData(brandData);
            brand.setBrandName(rootNode.get("brandName").asText());

            if (rootNode.has("brandData")) {
                JsonNode brandDataNode = rootNode.get("brandData");
                brand.getBrandData().setCountryOfOrigin(brandDataNode.get("countryOfOrigin").asText());
                brand.getBrandData().setContactInfo(brandDataNode.get("contactInfo").asText());
            }

            repositoryFactory.getBrandRepository().save(brand);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to process JSON", e);
        }
    }

    @Override
    public void put(int id, String value) {
        try {
            var existingBrand = repositoryFactory.getBrandRepository().get(id);
            if (existingBrand == null) {
                throw new RuntimeException("Brand not found");
            }

            JsonNode rootNode = mapper.readTree(value);
            existingBrand.setBrandName(rootNode.get("brandName").asText());

            if (rootNode.has("brandData")) {
                JsonNode brandDataNode = rootNode.get("brandData");
                existingBrand.getBrandData().setCountryOfOrigin(brandDataNode.get("countryOfOrigin").asText());
                existingBrand.getBrandData().setContactInfo(brandDataNode.get("contactInfo").asText());
            }

            repositoryFactory.getBrandRepository().save(existingBrand);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to process JSON", e);
        }
    }

    @Override
    public void delete(int id) {
        var existingBrand = repositoryFactory.getBrandRepository().get(id);

        if (existingBrand != null) {
            repositoryFactory.getBrandRepository().delete(existingBrand);
        } else {
            throw new RuntimeException("Brand not found");
        }
    }
}
