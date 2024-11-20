package cat.uvic.teknos.gt3.clients.console.handlers;

import cat.uvic.teknos.gt3.clients.console.dto.CarDto;
import cat.uvic.teknos.gt3.clients.console.utils.RestClientImpl;
import cat.uvic.teknos.gt3.clients.console.utils.Mappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import cat.uvic.teknos.gt3.clients.console.exceptions.RequestException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class CarCommandHandler {
    private final RestClientImpl restClient;
    private final BufferedReader in;
    private static final Scanner scanner = new Scanner(System.in);

    public CarCommandHandler(RestClientImpl restClient, BufferedReader in) {
        this.restClient = restClient;
        this.in = in;
    }

    public void manageCar() throws RequestException, JsonProcessingException {
        String command;
        do {
            showMenu();
            command = readLine(in);
            switch (command) {
                case "1" -> listCars();
                case "2" -> getCar();
                case "3" -> createCar();
                case "4" -> updateCar();
                case "5" -> deleteCar();
                case "exit" -> System.out.println("Exiting Car Management...");
                default -> System.out.println("Invalid command.");
            }
        } while (!command.equalsIgnoreCase("exit"));
    }

    private void showMenu() {
        System.out.println("\n*** Car Manager ***");
        System.out.println("1. List all cars");
        System.out.println("2. Get car details");
        System.out.println("3. Add new car");
        System.out.println("4. Update car");
        System.out.println("5. Delete car");
        System.out.println("Type 'exit' to quit");
        System.out.print("Enter command: ");
    }

    private void listCars() throws RequestException, JsonProcessingException {
        var cars = restClient.getAll("cars", CarDto[].class);
        System.out.println("\n*** List of Cars ***");
        if (cars.length == 0) {
            System.out.println("No cars found.");
            return;
        }
        System.out.println(AsciiTable.getTable(Stream.of(cars).toList(), List.of(
                new Column().header("Id").with(c -> String.valueOf(c.getId())),
                new Column().header("Model").with(CarDto::getModelName)
        )));
    }

    private void getCar() throws RequestException, JsonProcessingException {
        System.out.print("Enter the ID of the car: ");
        var carId = readLine(in);
        var car = restClient.get("cars/" + carId, CarDto.class);

        if (car == null) {
            System.out.println("Car not found.");
            return;
        }

        System.out.println("\n*** Car Details ***");
        System.out.println(AsciiTable.getTable(List.of(car), List.of(
                new Column().header("Id").with(c -> String.valueOf(c.getId())),
                new Column().header("Model").with(CarDto::getModelName)
        )));
    }

    public void createCar() {
        System.out.print("Enter car model name: ");
        String modelName = scanner.nextLine();

        System.out.print("Enter brand ID: ");
        String brandIdStr = scanner.nextLine();

        try {
            if (modelName.isEmpty() || brandIdStr.isEmpty()) {
                System.out.println("Model name and brand ID are required!");
                return;
            }

            int brandId = Integer.parseInt(brandIdStr);

            var car = new CarDto();
            car.setModelName(modelName);
            //car.setBrandId(brandId);

            String json = Mappers.get().writeValueAsString(car);
            restClient.post("cars", json);

            System.out.println("Car successfully created!");
        } catch (Exception e) {
            System.out.println("Error creating car: " + e.getMessage());
        }
    }

    public void updateCar() {
        System.out.print("Enter the ID of the car to update: ");
        String idStr = scanner.nextLine();

        try {
            int id = Integer.parseInt(idStr);

            // Get current details
            String detailsPath = String.format("cars/%d", id);
            CarDto car = restClient.get(detailsPath, CarDto.class);

            System.out.printf("Current Model: %s%n", car.getModelName());

            System.out.print("Enter new model name (leave blank to keep current): ");
            String newModel = scanner.nextLine();

            String updatedModel = newModel.isEmpty() ? car.getModelName() : newModel;

            CarDto updatedCar = new CarDto();
            updatedCar.setId(id);
            updatedCar.setModelName(updatedModel);

            restClient.put("cars/" + id, Mappers.get().writeValueAsString(updatedCar));

            System.out.println("Car successfully updated!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        } catch (Exception e) {
            System.out.println("Error updating car: " + e.getMessage());
        }
    }

    private void deleteCar() throws RequestException {
        System.out.print("Enter the ID of the car to delete: ");
        var carId = readLine(in);

        restClient.delete("cars/" + carId, null);
        System.out.println("Car deleted successfully.");
    }

    private String readLine(BufferedReader in) {
        try {
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Error while reading input: " + e);
        }
    }
}
