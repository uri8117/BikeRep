package cat.uvic.teknos.gt3.services.controllers;

import cat.uvic.teknos.gt3.domain.models.ModelFactory;
import cat.uvic.teknos.gt3.domain.models.User;
import cat.uvic.teknos.gt3.domain.repositories.RepositoryFactory;
import cat.uvic.teknos.gt3.domainimplementation.entities.BikeEntity;
import cat.uvic.teknos.gt3.domainimplementation.entities.UserEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class UserController implements Controller<Integer, User> {

    private final RepositoryFactory repositoryFactory;
    private final ObjectMapper objectMapper;

    public UserController(RepositoryFactory repositoryFactory, ModelFactory modelFactory) {
        this.repositoryFactory = repositoryFactory;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String get(int id) {
        UserEntity user = (UserEntity) repositoryFactory.getUserRepository().get((long) id);
        if (user == null) {
            return "{\"message\": \"User not found.\"}";
        }
        try {
            return objectMapper.writeValueAsString(user);
        } catch (IOException e) {
            return "{\"message\": \"Error converting User to JSON.\"}";
        }
    }

    @Override
    public String get() {
        try {
            return objectMapper.writeValueAsString(repositoryFactory.getUserRepository().getAll());
        } catch (IOException e) {
            return "{\"message\": \"Error converting User list to JSON.\"}";
        }
    }

    @Override
    public void post(String json) {
        try {
            JsonNode node = objectMapper.readTree(json);

            UserEntity user = new UserEntity();
            user.setName(node.get("name").asText());
            user.setEmail(node.get("email").asText());

            if (node.has("bikeId")) {
                Long bikeId = node.get("bikeId").asLong();
                BikeEntity bike = (BikeEntity) repositoryFactory.getBikeRepository().get(bikeId);
                if (bike != null) {
                    repositoryFactory.getUserBikeRepository().addBikeToUser(user, bike);
                }
            }

            repositoryFactory.getUserRepository().save(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void put(int id, String json) {
        try {
            JsonNode node = objectMapper.readTree(json);

            UserEntity user = (UserEntity) repositoryFactory.getUserRepository().get((long) id);
            if (user != null) {
                user.setName(node.get("name").asText());
                user.setEmail(node.get("email").asText());

                if (node.has("bikeIds")) {
                    repositoryFactory.getUserBikeRepository().removeAllBikesFromUser(user);
                    for (JsonNode bikeIdNode : node.get("bikeIds")) {
                        Long bikeId = bikeIdNode.asLong();
                        BikeEntity bike = (BikeEntity) repositoryFactory.getBikeRepository().get(bikeId);
                        if (bike != null) {
                            repositoryFactory.getUserBikeRepository().addBikeToUser(user, bike);
                        }
                    }
                }

                repositoryFactory.getUserRepository().save(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        UserEntity user = (UserEntity) repositoryFactory.getUserRepository().get((long) id);
        if (user != null) {
            repositoryFactory.getUserBikeRepository().removeAllBikesFromUser(user);
            repositoryFactory.getUserRepository().delete(user);
        }
    }
}
