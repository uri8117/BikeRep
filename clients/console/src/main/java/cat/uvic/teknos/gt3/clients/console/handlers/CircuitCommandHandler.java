package cat.uvic.teknos.gt3.clients.console.handlers;

import cat.uvic.teknos.gt3.clients.console.dto.CircuitDto;
import cat.uvic.teknos.gt3.clients.console.utils.RestClientImpl;
import cat.uvic.teknos.gt3.clients.console.utils.Mappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import cat.uvic.teknos.gt3.clients.console.exceptions.RequestException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;
import java.util.Scanner;

public class CircuitCommandHandler {
    private final RestClientImpl restClient;
    private final BufferedReader in;
    private static final Scanner scanner = new Scanner(System.in);

    public CircuitCommandHandler(RestClientImpl restClient, BufferedReader in) {
        this.restClient = restClient;
        this.in = in;
    }

    public void manageCircuit() throws RequestException, JsonProcessingException {
        String command;
        do {
            showMenu();
            command = readLine(in);
            switch (command) {
                case "1" -> listCircuits();
                case "2" -> getCircuit();
                case "3" -> createCircuit();
                case "4" -> updateCircuit();
                case "5" -> deleteCircuit();
                case "exit" -> System.out.println("Exiting Circuit Management...");
                default -> System.out.println("Invalid command.");
            }
        } while (!command.equalsIgnoreCase("exit"));
    }

    private void showMenu() {
        System.out.println("\n*** Circuit Manager ***");
        System.out.println("1. List all circuits");
        System.out.println("2. Get circuit details");
        System.out.println("3. Add new circuit");
        System.out.println("4. Update circuit");
        System.out.println("5. Delete circuit");
        System.out.println("Type 'exit' to quit");
        System.out.print("Enter command: ");
    }

    private void listCircuits() throws RequestException, JsonProcessingException {
        var circuits = restClient.getAll("circuits", CircuitDto[].class);
        System.out.println("\n*** List of Circuits ***");
        if (circuits.length == 0) {
            System.out.println("No circuits found.");
            return;
        }
        System.out.println(AsciiTable.getTable(Stream.of(circuits).toList(), List.of(
                new Column().header("Id").with(c -> String.valueOf(c.getId())),
                new Column().header("Name").with(CircuitDto::getCircuitName),
                new Column().header("Country").with(CircuitDto::getCountry),
                new Column().header("Length (km)").with(c -> String.valueOf(c.getLengthKm()))
        )));
    }

    private void getCircuit() throws RequestException, JsonProcessingException {
        System.out.print("Enter the ID of the circuit: ");
        var circuitId = readLine(in);
        var circuit = restClient.get("circuits/" + circuitId, CircuitDto.class);

        if (circuit == null) {
            System.out.println("Circuit not found.");
            return;
        }

        System.out.println("\n*** Circuit Details ***");
        System.out.println(AsciiTable.getTable(List.of(circuit), List.of(
                new Column().header("Id").with(c -> String.valueOf(c.getId())),
                new Column().header("Name").with(CircuitDto::getCircuitName),
                new Column().header("Country").with(CircuitDto::getCountry),
                new Column().header("Length (km)").with(c -> String.valueOf(c.getLengthKm()))
        )));
    }

    public void createCircuit() {
        System.out.print("Enter circuit name: ");
        String name = scanner.nextLine();

        System.out.print("Enter country: ");
        String country = scanner.nextLine();

        System.out.print("Enter length (km): ");
        String lengthStr = scanner.nextLine();

        try {
            if (name.isEmpty() || country.isEmpty() || lengthStr.isEmpty()) {
                System.out.println("All fields are required!");
                return;
            }

            double length = Double.parseDouble(lengthStr);

            var circuit = new CircuitDto();
            circuit.setCircuitName(name);
            circuit.setCountry(country);
            circuit.setLengthKm(length);

            String json = Mappers.get().writeValueAsString(circuit);
            System.out.println("Sending JSON: " + json);

            // Send the POST request
            restClient.post("circuits", json);

            System.out.println("Circuit successfully created!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format for length.");
        } catch (Exception e) {
            System.out.println("Error creating circuit: " + e.getMessage());
        }
    }

    public void updateCircuit() {
        System.out.print("Enter the ID of the circuit to update: ");
        String idStr = scanner.nextLine();

        try {
            int id = Integer.parseInt(idStr);

            // Obtener detalles actuales
            String detailsPath = String.format("circuits/%d", id);
            CircuitDto circuit = restClient.get(detailsPath, CircuitDto.class);

            System.out.printf("Current Name: %s%n", circuit.getCircuitName());
            System.out.printf("Current Country: %s%n", circuit.getCountry());
            System.out.printf("Current Length: %.2f km%n", circuit.getLengthKm());

            System.out.print("Enter new name (leave blank to keep current): ");
            String newName = scanner.nextLine();

            System.out.print("Enter new country (leave blank to keep current): ");
            String newCountry = scanner.nextLine();

            System.out.print("Enter new length (leave blank to keep current): ");
            String newLengthStr = scanner.nextLine();

            String updatedName = newName.isEmpty() ? circuit.getCircuitName() : newName;
            String updatedCountry = newCountry.isEmpty() ? circuit.getCountry() : newCountry;
            double updatedLength = newLengthStr.isEmpty() ? circuit.getLengthKm() : Double.parseDouble(newLengthStr);

            CircuitDto updatedCircuit = new CircuitDto();
            updatedCircuit.setId(id);
            updatedCircuit.setCircuitName(updatedName);
            updatedCircuit.setCountry(updatedCountry);
            updatedCircuit.setLengthKm(updatedLength);

            restClient.put("circuits/" +  id, Mappers.get().writeValueAsString(updatedCircuit));

            System.out.println("Circuit successfully updated!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format for length or ID.");
        } catch (Exception e) {
            System.out.println("Error updating circuit: " + e.getMessage());
        }
    }

    private void deleteCircuit() throws RequestException {
        System.out.print("Enter the ID of the circuit to delete: ");
        var circuitId = readLine(in);

        restClient.delete("circuits/" + circuitId, null);
        System.out.println("Circuit deleted successfully.");
    }

    private String readLine(BufferedReader in) {
        try {
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Error while reading input: " + e);
        }
    }
}
