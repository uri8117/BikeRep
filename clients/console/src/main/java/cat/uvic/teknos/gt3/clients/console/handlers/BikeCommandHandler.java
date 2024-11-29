package cat.uvic.teknos.gt3.clients.console.handlers;

import cat.uvic.teknos.gt3.clients.console.utils.RestClientImpl;
import cat.uvic.teknos.gt3.clients.console.utils.Mappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import cat.uvic.teknos.gt3.clients.console.exceptions.RequestException;
import cat.uvic.teknos.gt3.clients.console.dto.BikeDto;
import cat.uvic.teknos.gt3.clients.console.dto.BikeDataDto;
import cat.uvic.teknos.gt3.clients.console.dto.BrandDto;
import cat.uvic.teknos.gt3.clients.console.dto.UserDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class BikeCommandHandler {
    private final RestClientImpl restClient;
    private final BufferedReader in;

    public BikeCommandHandler(RestClientImpl restClient, BufferedReader in) {
        this.restClient = restClient;
        this.in = in;
    }

    public void manageBike() {
        String command;
        do {
            showMenu();
            command = readLine(in);
            try {
                switch (command) {
                    case "1" -> listBikes();
                    case "2" -> getBike();
                    case "3" -> createBike();
                    case "4" -> updateBike();
                    case "5" -> deleteBike();
                    case "exit" -> System.out.println("Exiting Bike Management...");
                    default -> System.out.println("Invalid command.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        } while (!command.equalsIgnoreCase("exit"));
    }

    private void showMenu() {
        System.out.println("\n*** Bike Manager ***");
        System.out.println("1. List all bikes");
        System.out.println("2. Get bike details");
        System.out.println("3. Add new bike");
        System.out.println("4. Update bike");
        System.out.println("5. Delete bike");
        System.out.println("Type 'exit' to quit");
        System.out.print("Enter command: ");
    }

    private void listBikes() throws RequestException, JsonProcessingException {
        BikeDto[] bikes = restClient.getAll("bikes", BikeDto[].class);
        if (bikes == null || bikes.length == 0) {
            System.out.println("No bikes found.");
            return;
        }

        System.out.println("\n*** List of Bikes ***");
        System.out.println(AsciiTable.getTable(Stream.of(bikes).toList(), List.of(
                new Column().header("Id").with(b -> String.valueOf(b.getId())),
                new Column().header("Model").with(BikeDto::getModel),
                new Column().header("Year").with(b -> String.valueOf(b.getYear())),
                new Column().header("Brand").with(b -> b.getBrand().getName())
        )));
    }

    private void getBike() throws RequestException, JsonProcessingException {
        System.out.print("Enter the ID of the bike: ");
        String bikeId = readLine(in);

        try {
            BikeDto bike = restClient.get("bikes/" + bikeId, BikeDto.class);
            if (bike == null) {
                System.out.println("Bike not found.");
                return;
            }

            System.out.println("\n*** Bike Details ***");
            System.out.println(AsciiTable.getTable(List.of(bike), List.of(
                    new Column().header("Id").with(b -> String.valueOf(b.getId())),
                    new Column().header("Model").with(BikeDto::getModel),
                    new Column().header("Year").with(b -> String.valueOf(b.getYear())),
                    new Column().header("Brand").with(b -> b.getBrand().getName()),
                    new Column().header("Engine Capacity").with(b -> String.valueOf(b.getBikeData().getEngineCapacity())),
                    new Column().header("Weight").with(b -> String.valueOf(b.getBikeData().getWeight()))
            )));
        } catch (Exception e) {
            System.out.println("Error: Could not retrieve bike details. Please check the ID.");
        }
    }

    public void createBike() {
        try {
            System.out.print("Enter bike model: ");
            String model = readLine(in);

            System.out.print("Enter bike year: ");
            int year = Integer.parseInt(readLine(in));

            System.out.print("Enter brand ID: ");
            long brandId = Long.parseLong(readLine(in));

            System.out.print("Enter engine capacity: ");
            int engineCapacity = Integer.parseInt(readLine(in));

            System.out.print("Enter weight: ");
            int weight = Integer.parseInt(readLine(in));

            BrandDto brand = restClient.get("brands/" + brandId, BrandDto.class);
            if (brand == null) {
                System.out.println("Brand not found.");
                return;
            }

            BikeDataDto bikeData = new BikeDataDto();
            bikeData.setEngineCapacity(engineCapacity);
            bikeData.setWeight(weight);

            BikeDto bike = new BikeDto();
            bike.setModel(model);
            bike.setYear(year);
            bike.setBrand(brand);
            bike.setBikeData(bikeData);

            String bikeJson = Mappers.get().writeValueAsString(bike);
            restClient.post("bikes", bikeJson);

            System.out.println("Bike successfully created!");
        } catch (Exception e) {
            System.out.println("Error creating bike: " + e.getMessage());
        }
    }


    public void updateBike() {
        try {
            System.out.print("Enter the ID of the bike to update: ");
            long id = Long.parseLong(readLine(in));

            System.out.print("Enter new bike model (leave blank to keep unchanged): ");
            String model = readLine(in);

            System.out.print("Enter new bike year (leave blank to keep unchanged): ");
            String yearInput = readLine(in);

            System.out.print("Enter new brand ID (leave blank to keep unchanged): ");
            String brandIdInput = readLine(in);

            System.out.print("Enter new engine capacity (leave blank to keep unchanged): ");
            String engineCapacityInput = readLine(in);

            System.out.print("Enter new weight (leave blank to keep unchanged): ");
            String weightInput = readLine(in);

            BikeDto bike = restClient.get("bikes/" + id, BikeDto.class);

            if (bike == null) {
                System.out.println("Bike not found.");
                return;
            }

            if (!model.isBlank()) {
                bike.setModel(model);
            }
            if (!yearInput.isBlank()) {
                bike.setYear(Integer.parseInt(yearInput));
            }
            if (!brandIdInput.isBlank()) {
                long brandId = Long.parseLong(brandIdInput);
                BrandDto brand = restClient.get("brands/" + brandId, BrandDto.class);
                if (brand != null) {
                    bike.setBrand(brand);
                } else {
                    System.out.println("Brand not found. Keeping current brand.");
                }
            }
            if (!engineCapacityInput.isBlank()) {
                bike.getBikeData().setEngineCapacity(Integer.parseInt(engineCapacityInput));
            }
            if (!weightInput.isBlank()) {
                bike.getBikeData().setWeight(Integer.parseInt(weightInput));
            }

            String bikeJson = Mappers.get().writeValueAsString(bike);
            restClient.put("bikes/" + id, bikeJson);

            System.out.println("Bike successfully updated!");
        } catch (Exception e) {
            System.out.println("Error updating bike: " + e.getMessage());
        }
    }


    public void deleteBike() {
        try {
            System.out.print("Enter the ID of the bike to delete: ");
            long id = Long.parseLong(readLine(in));
            restClient.delete("bikes/" + id, null);
            System.out.println("Bike successfully deleted!");
        } catch (Exception e) {
            System.out.println("Error deleting bike: " + e.getMessage());
        }
    }

    private String readLine(BufferedReader in) {
        try {
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Error reading input", e);
        }
    }
}
