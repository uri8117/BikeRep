package cat.uvic.teknos.gt3.services.controllers;

import cat.uvic.teknos.gt3.domain.models.Driver;
import cat.uvic.teknos.gt3.domain.models.ModelFactory;
import cat.uvic.teknos.gt3.domain.repositories.RepositoryFactory;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Set;

public class DriverController implements Controller {

    private final RepositoryFactory repositoryFactory;
    private final ModelFactory modelFactory;
    private final ObjectMapper mapper;

    public DriverController(RepositoryFactory repositoryFactory, ModelFactory modelFactory) {
        this.repositoryFactory = repositoryFactory;
        this.modelFactory = modelFactory;
        this.mapper = new ObjectMapper();

        this.mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Override
    public String get(int id) {
        Driver driver = repositoryFactory.getDriverRepository().get(id);
        if (driver == null) {
            throw new RuntimeException("Driver not found");
        }
        try {
            return mapper.writeValueAsString(driver);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting driver to JSON", e);
        }
    }

    @Override
    public String get() {
        Set<Driver> drivers = repositoryFactory.getDriverRepository().getAll();
        try {
            return mapper.writeValueAsString(drivers);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting drivers list to JSON", e);
        }
    }

    @Override
    public void post(String value) {
        try {
            JsonNode rootNode = mapper.readTree(value);
            Driver driver = new cat.uvic.teknos.gt3.file.jbdc.models.Driver();
            driver.setFirstName(rootNode.get("firstName").asText());
            driver.setLastName(rootNode.get("lastName").asText());
            driver.setNationality(rootNode.get("nationality").asText());
            driver.setBirthdate(java.sql.Date.valueOf(rootNode.get("birthdate").asText())); // Assuming birthdate is in 'YYYY-MM-DD' format

            repositoryFactory.getDriverRepository().save(driver);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to process JSON", e);
        }
    }

    @Override
    public void put(int id, String value) {
        try {
            Driver existingDriver = repositoryFactory.getDriverRepository().get(id);
            if (existingDriver == null) {
                throw new RuntimeException("Driver not found");
            }

            JsonNode rootNode = mapper.readTree(value);
            existingDriver.setFirstName(rootNode.get("firstName").asText());
            existingDriver.setLastName(rootNode.get("lastName").asText());
            existingDriver.setNationality(rootNode.get("nationality").asText());
            existingDriver.setBirthdate(java.sql.Date.valueOf(rootNode.get("birthdate").asText())); // Assuming birthdate is in 'YYYY-MM-DD' format

            repositoryFactory.getDriverRepository().save(existingDriver);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to process JSON", e);
        }
    }

    @Override
    public void delete(int id) {
        Driver existingDriver = repositoryFactory.getDriverRepository().get(id);
        if (existingDriver != null) {
            repositoryFactory.getDriverRepository().delete(existingDriver);
        } else {
            throw new RuntimeException("Driver not found");
        }
    }
}
