package cat.uvic.teknos.gt3.services.controllers;

import cat.uvic.teknos.gt3.domain.models.Car;
import cat.uvic.teknos.gt3.domain.models.CarData;
import cat.uvic.teknos.gt3.domain.models.Driver;
import cat.uvic.teknos.gt3.domain.models.ModelFactory;
import cat.uvic.teknos.gt3.domain.repositories.RepositoryFactory;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Set;

public class CarController implements Controller {

    private final RepositoryFactory repositoryFactory;
    private final ModelFactory modelFactory;
    private final ObjectMapper mapper;

    public CarController(RepositoryFactory repositoryFactory, ModelFactory modelFactory) {
        this.repositoryFactory = repositoryFactory;
        this.modelFactory = modelFactory;
        this.mapper = new ObjectMapper();

        this.mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Override
    public String get(int id) {
        Car car = repositoryFactory.getCarRepository().get(id);
        if (car == null) {
            throw new RuntimeException("Car not found");
        }
        try {
            return mapper.writeValueAsString(car);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting car to JSON", e);
        }
    }

    @Override
    public String get() {
        Set<Car> cars = repositoryFactory.getCarRepository().getAll();
        try {
            return mapper.writeValueAsString(cars);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting cars list to JSON", e);
        }
    }

    @Override
    public void post(String value) {
        try {
            JsonNode rootNode = mapper.readTree(value);
            Car car = new cat.uvic.teknos.gt3.file.jbdc.models.Car();
            car.setModelName(rootNode.get("modelName").asText());

            // Assuming brand ID is sent in the request
            int brandId = rootNode.get("brandId").asInt();
            car.setBrand(repositoryFactory.getBrandRepository().get(brandId));

            // Setting CarData
            CarData carData = new cat.uvic.teknos.gt3.file.jbdc.models.CarData();
            carData.setHorsepower(rootNode.get("carData").get("horsepower").asInt());
            carData.setWeight(rootNode.get("carData").get("weight").asInt());
            car.setCarData(carData);

            // Assuming driver IDs are sent as an array
            if (rootNode.has("drivers")) {
                for (JsonNode driverNode : rootNode.get("drivers")) {
                    Driver driver = new cat.uvic.teknos.gt3.file.jbdc.models.Driver();
                    driver.setId(driverNode.get("id").asInt());
                    car.addDriver(driver); // Assuming there's a method to add drivers
                }
            }

            repositoryFactory.getCarRepository().save(car);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to process JSON", e);
        }
    }

    @Override
    public void put(int id, String value) {
        try {
            Car existingCar = repositoryFactory.getCarRepository().get(id);
            if (existingCar == null) {
                throw new RuntimeException("Car not found");
            }

            JsonNode rootNode = mapper.readTree(value);
            existingCar.setModelName(rootNode.get("modelName").asText());

            // Update CarData
            if (rootNode.has("carData")) {
                CarData carData = existingCar.getCarData();
                carData.setHorsepower(rootNode.get("carData").get("horsepower").asInt());
                carData.setWeight(rootNode.get("carData").get("weight").asInt());
            }

            // Update drivers
            existingCar.clearDrivers(); // Assuming there's a method to clear drivers
            if (rootNode.has("drivers")) {
                for (JsonNode driverNode : rootNode.get("drivers")) {
                    Driver driver = new cat.uvic.teknos.gt3.file.jbdc.models.Driver();
                    driver.setId(driverNode.get("id").asInt());
                    existingCar.addDriver(driver); // Assuming there's a method to add drivers
                }
            }

            repositoryFactory.getCarRepository().save(existingCar);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to process JSON", e);
        }
    }

    @Override
    public void delete(int id) {
        Car existingCar = repositoryFactory.getCarRepository().get(id);
        if (existingCar != null) {
            repositoryFactory.getCarRepository().delete(existingCar);
        } else {
            throw new RuntimeException("Car not found");
        }
    }
}
