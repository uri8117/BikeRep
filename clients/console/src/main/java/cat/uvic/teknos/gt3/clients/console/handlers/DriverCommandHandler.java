package cat.uvic.teknos.gt3.clients.console.handlers;

import cat.uvic.teknos.gt3.clients.console.dto.DriverDto;
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

public class DriverCommandHandler {
    private final RestClientImpl restClient;
    private final BufferedReader in;
    private static final Scanner scanner = new Scanner(System.in);

    public DriverCommandHandler(RestClientImpl restClient, BufferedReader in) {
        this.restClient = restClient;
        this.in = in;
    }

    public void manageDriver() throws RequestException, JsonProcessingException {
        String command;
        do {
            showMenu();
            command = readLine(in);
            switch (command) {
                case "1" -> listDrivers();
                case "2" -> getDriver();
                case "3" -> createDriver();
                case "4" -> updateDriver();
                case "5" -> deleteDriver();
                case "exit" -> System.out.println("Exiting Driver Management...");
                default -> System.out.println("Invalid command.");
            }
        } while (!command.equalsIgnoreCase("exit"));
    }

    private void showMenu() {
        System.out.println("\n*** Driver Manager ***");
        System.out.println("1. List all drivers");
        System.out.println("2. Get driver details");
        System.out.println("3. Add new driver");
        System.out.println("4. Update driver");
        System.out.println("5. Delete driver");
        System.out.println("Type 'exit' to quit");
        System.out.print("Enter command: ");
    }

    private void listDrivers() throws RequestException, JsonProcessingException {
        var drivers = restClient.getAll("drivers", DriverDto[].class);
        System.out.println("\n*** List of Drivers ***");
        if (drivers.length == 0) {
            System.out.println("No drivers found.");
            return;
        }
        System.out.println(AsciiTable.getTable(Stream.of(drivers).toList(), List.of(
                new Column().header("Id").with(d -> String.valueOf(d.getId())),
                new Column().header("Name").with(DriverDto::getFirstName)
        )));
    }

    private void getDriver() throws RequestException, JsonProcessingException {
        System.out.print("Enter the ID of the driver: ");
        var driverId = readLine(in);
        var driver = restClient.get("drivers/" + driverId, DriverDto.class);

        if (driver == null) {
            System.out.println("Driver not found.");
            return;
        }

        System.out.println("\n*** Driver Details ***");
        System.out.println(AsciiTable.getTable(List.of(driver), List.of(
                new Column().header("Id").with(d -> String.valueOf(d.getId())),
                new Column().header("Name").with(DriverDto::getFirstName)
        )));
    }

    public void createDriver() {
//        System.out.print("Enter driver name: ");
//        String name = scanner.nextLine();
//
//        System.out.print("Enter team name: ");
//        String team = scanner.nextLine();
//
//        try {
//            if (name.isEmpty() || team.isEmpty()) {
//                System.out.println("Name and team are required!");
//                return;
//            }
//
//            var driver = new DriverDto();
//            driver.setFirstName(name);
//            driver.s(team);
//
//            String json = Mappers.get().writeValueAsString(driver);
//            restClient.post("drivers", json);
//
//            System.out.println("Driver successfully created!");
//        } catch (Exception e) {
//            System.out.println("Error creating driver: " + e.getMessage());
//        }
    }

    public void updateDriver() {
//        System.out.print("Enter the ID of the driver to update: ");
//        String idStr = scanner.nextLine();
//
//        try {
//            int id = Integer.parseInt(idStr);
//
//            // Get current details
//            String detailsPath = String.format("drivers/%d", id);
//            DriverDto driver = restClient.get(detailsPath, DriverDto.class);
//
//            System.out.printf("Current Name: %s%n", driver.getDriverName());
//            System.out.printf("Current Team: %s%n", driver.getTeamName());
//
//            System.out.print("Enter new name (leave blank to keep current): ");
//            String newName = scanner.nextLine();
//
//            System.out.print("Enter new team name (leave blank to keep current): ");
//            String newTeam = scanner.nextLine();
//
//            String updatedName = newName.isEmpty() ? driver.getDriverName() : newName;
//            String updatedTeam = newTeam.isEmpty() ? driver.getTeamName() : newTeam;
//
//            DriverDto updatedDriver = new DriverDto();
//            updatedDriver.setId(id);
//            updatedDriver.setDriverName(updatedName);
//            updatedDriver.setTeamName(updatedTeam);
//
//            restClient.put("drivers/" + id, Mappers.get().writeValueAsString(updatedDriver));
//
//            System.out.println("Driver successfully updated!");
//        } catch (NumberFormatException e) {
//            System.out.println("Invalid ID format.");
//        } catch (Exception e) {
//            System.out.println("Error updating driver: " + e.getMessage());
//        }
    }

    private void deleteDriver() throws RequestException {
        System.out.print("Enter the ID of the driver to delete: ");
        var driverId = readLine(in);

        restClient.delete("drivers/" + driverId, null);
        System.out.println("Driver deleted successfully.");
    }

    private String readLine(BufferedReader in) {
        try {
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Error while reading input: " + e);
        }
    }
}
