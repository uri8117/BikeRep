package cat.uvic.teknos.gt3.services.controllers;

import cat.uvic.teknos.gt3.domain.models.Bike;
import cat.uvic.teknos.gt3.domain.models.ModelFactory;
import cat.uvic.teknos.gt3.domain.repositories.RepositoryFactory;
import cat.uvic.teknos.gt3.domainimplementation.entities.BikeEntity;
import cat.uvic.teknos.gt3.domainimplementation.entities.BikeDataEntity;
import cat.uvic.teknos.gt3.domainimplementation.entities.UserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.List;

public class BikeController implements Controller<Integer, Bike> {

    private final RepositoryFactory repositoryFactory;
    private final ObjectMapper objectMapper;

    public BikeController(RepositoryFactory repositoryFactory, ModelFactory modelFactory) {
        this.repositoryFactory = repositoryFactory;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String get(int id) {
        Bike bike = repositoryFactory.getBikeRepository().get((long) id);
        if (bike == null) {
            return "{\"message\": \"Bike not found.\"}";
        }
        try {
            return objectMapper.writeValueAsString(bike);
        } catch (IOException e) {
            e.printStackTrace();
            return "{\"message\": \"Error converting Bike to JSON.\"}";
        }
    }

    @Override
    public String get() {
        try {
            return objectMapper.writeValueAsString(repositoryFactory.getBikeRepository().getAll());
        } catch (IOException e) {
            e.printStackTrace();
            return "{\"message\": \"Error converting bikes list to JSON.\"}";
        }
    }

    @Override
    public void post(String json) {
        try {
            JsonNode node = objectMapper.readTree(json);

            // Crear BikeEntity
            BikeEntity bikeEntity = new BikeEntity();
            bikeEntity.setModel(node.get("model").asText());
            bikeEntity.setYear(node.get("year").asInt());

            // Crear BikeDataEntity (relacionado con la bicicleta)
            BikeDataEntity bikeDataEntity = new BikeDataEntity();
            bikeDataEntity.setEngineCapacity(node.get("engineCapacity").asDouble());
            bikeDataEntity.setWeight(node.get("weight").asDouble());

            // Asociar BikeDataEntity a la BikeEntity
            bikeEntity.setBikeData(bikeDataEntity);

            // Guardar la nueva BikeEntity en la base de datos
            repositoryFactory.getBikeRepository().save(bikeEntity);

            // Enlazar la bicicleta con un usuario si se pasa un id de usuario
            if (node.has("userId")) {
                Long userId = node.get("userId").asLong();
                UserEntity userEntity = (UserEntity) repositoryFactory.getUserRepository().get(userId);
                if (userEntity != null) {
                    // Asociar la bicicleta al usuario
                    repositoryFactory.getUserBikeRepository().addBikeToUser(userEntity, bikeEntity);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void put(int id, String json) {
        try {
            JsonNode node = objectMapper.readTree(json);
            BikeEntity bikeEntity = (BikeEntity) repositoryFactory.getBikeRepository().get((long) id);

            if (bikeEntity != null) {
                // Actualizar los datos de la bicicleta
                bikeEntity.setModel(node.get("model").asText());
                bikeEntity.setYear(node.get("year").asInt());

                // Actualizar BikeDataEntity (datos técnicos)
                BikeDataEntity bikeDataEntity = (BikeDataEntity) bikeEntity.getBikeData();  // CAST a BikeDataEntity

                bikeDataEntity.setEngineCapacity(node.get("engineCapacity").asDouble());
                bikeDataEntity.setWeight(node.get("weight").asDouble());

                // Guardar los cambios en la base de datos
                repositoryFactory.getBikeRepository().save(bikeEntity);

                // Enlazar la bicicleta con un usuario si se pasa un id de usuario
                if (node.has("userId")) {
                    Long userId = node.get("userId").asLong();
                    UserEntity userEntity = (UserEntity) repositoryFactory.getUserRepository().get(userId);
                    if (userEntity != null) {
                        // Asociar la bicicleta al usuario
                        repositoryFactory.getUserBikeRepository().addBikeToUser(userEntity, bikeEntity);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        BikeEntity bikeEntity = (BikeEntity) repositoryFactory.getBikeRepository().get((long) id);
        if (bikeEntity != null) {
            // Eliminar la bicicleta de la base de datos
            repositoryFactory.getBikeRepository().delete(bikeEntity);
            // No necesitas eliminar las relaciones en `user_bikes`, ya que lo hará la base de datos
        }
    }
}
