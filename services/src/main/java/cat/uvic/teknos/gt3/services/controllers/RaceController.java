package cat.uvic.teknos.gt3.services.controllers;

import cat.uvic.teknos.gt3.domain.models.ModelFactory;
import cat.uvic.teknos.gt3.domain.models.RaceDriver;
import cat.uvic.teknos.gt3.domain.repositories.RaceRepository;
import cat.uvic.teknos.gt3.domain.repositories.RepositoryFactory;  // Asegúrate de usar la interfaz
import cat.uvic.teknos.gt3.file.jbdc.models.Driver;
import cat.uvic.teknos.gt3.file.jbdc.models.Race;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

public class RaceController implements Controller {

    private final RepositoryFactory repositoryFactory;
    private final ModelFactory modelFactory;
    private final ObjectMapper mapper;

    public RaceController(RepositoryFactory repositoryFactory, ModelFactory modelFactory) {  // Inyección de la interfaz RaceRepository
        this.repositoryFactory = repositoryFactory;
        this.modelFactory = modelFactory;
        this.mapper = new ObjectMapper();

        this.mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Override
    public String get(int id) {
        var race = repositoryFactory.getRaceRepository().get(id);  // Utiliza la interfaz en lugar de la implementación
        if (race == null) {
            throw new RuntimeException("Race not found");
        }
        try {
            return mapper.writeValueAsString(race);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting race to JSON", e);
        }
    }

    @Override
    public String get() {
        var races = repositoryFactory.getRaceRepository().getAll();  // Utiliza la interfaz en lugar de la implementación
        try {
            return mapper.writeValueAsString(races);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting races list to JSON", e);
        }
    }

    @Override
    public void post(String value) {
        try {
            JsonNode rootNode = mapper.readTree(value);
            var race = new cat.uvic.teknos.gt3.file.jbdc.models.Race();
            var newRace = createOrUpdateRaceFromJson(race, rootNode);

            repositoryFactory.getRaceRepository().save(newRace);  // Guarda la nueva carrera en la base de datos usando la interfaz

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to process JSON", e);
        }
    }

    @Override
    public void put(int id, String value) {
        try {
            var existingRace = repositoryFactory.getRaceRepository().get(id);  // Utiliza la interfaz
            if (existingRace == null) {
                throw new RuntimeException("Race not found");
            }

            JsonNode rootNode = mapper.readTree(value);
            var newRace = createOrUpdateRaceFromJson(existingRace, rootNode);

            repositoryFactory.getRaceRepository().save(newRace);;  // Guarda los cambios en la base de datos usando la interfaz

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to process JSON", e);
        }
    }

    @Override
    public void delete(int id) {
        cat.uvic.teknos.gt3.domain.models.Race existingRace = repositoryFactory.getRaceRepository().get(id);

        if (existingRace != null) {
            repositoryFactory.getRaceRepository().delete(existingRace);
        } else {
            throw new RuntimeException("Client not found");
        }
    }

    // Método auxiliar para crear o actualizar un objeto Race desde JSON
    private cat.uvic.teknos.gt3.domain.models.Race createOrUpdateRaceFromJson(cat.uvic.teknos.gt3.domain.models.Race race, JsonNode rootNode) {
        if (rootNode.has("circuitId")) {
            cat.uvic.teknos.gt3.domain.models.Circuit circuit = new cat.uvic.teknos.gt3.file.jbdc.models.Circuit();
            circuit.setId(rootNode.get("circuitId").asInt());
            race.setCircuit(circuit);
        }

        if (rootNode.has("raceName")) {
            race.setRaceName(rootNode.get("raceName").asText());
        }

        if (rootNode.has("raceDate")) {
            race.setRaceDate(Date.valueOf(rootNode.get("raceDate").asText()));
        }

        if (rootNode.has("drivers")) {
            Set<RaceDriver> raceDrivers = new HashSet<>();
            for (JsonNode driverNode : rootNode.get("drivers")) {
                int driverId = driverNode.get("driverId").asInt();
                int position = driverNode.get("position").asInt();

                Driver driver = createDriverWithId(driverId);
                RaceDriver raceDriver = new cat.uvic.teknos.gt3.file.jbdc.models.RaceDriver();
                raceDriver.setRace(race);
                raceDriver.setDriver(driver);
                raceDriver.setPosition(position);

                raceDrivers.add(raceDriver);
            }
            race.setRaceDrivers(raceDrivers);
        }

        return race;
    }

    private Driver createDriverWithId(int id) {
        Driver driver = new Driver();
        driver.setId(id);
        return driver;
    }
}