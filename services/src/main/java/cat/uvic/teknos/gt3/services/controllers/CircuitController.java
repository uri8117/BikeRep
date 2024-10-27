package cat.uvic.teknos.gt3.services.controllers;

import cat.uvic.teknos.gt3.domain.models.Circuit;
import cat.uvic.teknos.gt3.domain.models.ModelFactory;
import cat.uvic.teknos.gt3.domain.repositories.RepositoryFactory;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CircuitController implements Controller {

    private final RepositoryFactory repositoryFactory;
    private final ModelFactory modelFactory;
    private final ObjectMapper mapper;

    public CircuitController(RepositoryFactory repositoryFactory, ModelFactory modelFactory) {
        this.repositoryFactory = repositoryFactory;
        this.modelFactory = modelFactory;
        this.mapper = new ObjectMapper();

        this.mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Override
    public String get(int id) {
        var circuit = repositoryFactory.getCircuitRepository().get(id);
        if (circuit == null) {
            throw new RuntimeException("Circuit not found");
        }
        try {
            return mapper.writeValueAsString(circuit);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting circuit to JSON", e);
        }
    }

    @Override
    public String get() {
        var circuits = repositoryFactory.getCircuitRepository().getAll();
        try {
            return mapper.writeValueAsString(circuits);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting circuits list to JSON", e);
        }
    }

    @Override
    public void post(String value) {
        try {
            JsonNode rootNode = mapper.readTree(value);
            Circuit circuit = new cat.uvic.teknos.gt3.file.jbdc.models.Circuit();
            circuit.setCircuitName(rootNode.get("name").asText());
            circuit.setCountry(rootNode.get("location").asText());
            circuit.setLengthKm(rootNode.get("length").asDouble());

            repositoryFactory.getCircuitRepository().save(circuit);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to process JSON", e);
        }
    }

    @Override
    public void put(int id, String value) {
        try {
            var existingCircuit = repositoryFactory.getCircuitRepository().get(id);
            if (existingCircuit == null) {
                throw new RuntimeException("Circuit not found");
            }

            JsonNode rootNode = mapper.readTree(value);
            existingCircuit.setCircuitName(rootNode.get("name").asText());
            existingCircuit.setCountry(rootNode.get("location").asText());
            existingCircuit.setLengthKm(rootNode.get("length").asDouble());

            repositoryFactory.getCircuitRepository().save(existingCircuit);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to process JSON", e);
        }
    }

    @Override
    public void delete(int id) {
        var existingCircuit = repositoryFactory.getCircuitRepository().get(id);

        if (existingCircuit != null) {
            repositoryFactory.getCircuitRepository().delete(existingCircuit);
        } else {
            throw new RuntimeException("Circuit not found");
        }
    }
}
