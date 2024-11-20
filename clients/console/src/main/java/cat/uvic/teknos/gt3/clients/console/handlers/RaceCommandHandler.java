package cat.uvic.teknos.gt3.clients.console.handlers;

import cat.uvic.teknos.gt3.clients.console.dto.RaceDto;
import cat.uvic.teknos.gt3.clients.console.utils.RestClientImpl;
import cat.uvic.teknos.gt3.clients.console.utils.Mappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import cat.uvic.teknos.gt3.clients.console.exceptions.RequestException;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class RaceCommandHandler {
    private final RestClientImpl restClient;
    private final BufferedReader in;
    private static final Scanner scanner = new Scanner(System.in);

    public RaceCommandHandler(RestClientImpl restClient, BufferedReader in) {
        this.restClient = restClient;
        this.in = in;
    }

    public void manageRace() throws RequestException, JsonProcessingException {
        String command;
        do {
            showMenu();
            command = readLine(in);
            switch (command) {
                case "1" -> listRaces();
                case "2" -> getRace();
                case "3" -> createRace();
                case "4" -> updateRace();
                case "5" -> deleteRace();
                case "exit" -> System.out.println("Exiting Race Management...");
                default -> System.out.println("Invalid command.");
            }
        } while (!command.equalsIgnoreCase("exit"));
    }

    private void showMenu() {
        System.out.println("\n*** Race Manager ***");
        System.out.println("1. List all races");
        System.out.println("2. Get race details");
        System.out.println("3. Add new race");
        System.out.println("4. Update race");
        System.out.println("5. Delete race");
        System.out.println("Type 'exit' to quit");
        System.out.print("Enter command: ");
    }

    private void listRaces() throws RequestException, JsonProcessingException {
        var races = restClient.getAll("races", RaceDto[].class);
        System.out.println("\n*** List of Races ***");
        if (races.length == 0) {
            System.out.println("No races found.");
            return;
        }
        System.out.println(AsciiTable.getTable(Stream.of(races).toList(), List.of(
                new Column().header("Id").with(r -> String.valueOf(r.getId())),
                new Column().header("Name").with(RaceDto::getRaceName)
        )));
    }

    private void getRace() throws RequestException, JsonProcessingException {
        System.out.print("Enter the ID of the race: ");
        var raceId = readLine(in);
        var race = restClient.get("races/" + raceId, RaceDto.class);

        if (race == null) {
            System.out.println("Race not found.");
            return;
        }

        System.out.println("\n*** Race Details ***");
        System.out.println(AsciiTable.getTable(List.of(race), List.of(
                new Column().header("Id").with(r -> String.valueOf(r.getId())),
                new Column().header("Name").with(RaceDto::getRaceName)
        )));
    }

    public void createRace() {
        System.out.print("Enter race name: ");
        String name = scanner.nextLine();

        System.out.print("Enter date (YYYY-MM-DD): ");
        String date = scanner.nextLine();

        try {
            if (name.isEmpty() || date.isEmpty()) {
                System.out.println("Race name and date are required!");
                return;
            }

            var race = new RaceDto();
            race.setRaceName(name);
            race.setRaceDate(Date.valueOf(date));

            String json = Mappers.get().writeValueAsString(race);
            restClient.post("races", json);

            System.out.println("Race successfully created!");
        } catch (Exception e) {
            System.out.println("Error creating race: " + e.getMessage());
        }
    }

    public void updateRace() {
        System.out.print("Enter the ID of the race to update: ");
        String idStr = scanner.nextLine();

        try {
            int id = Integer.parseInt(idStr);

            // Get current details
            String detailsPath = String.format("races/%d", id);
            RaceDto race = restClient.get(detailsPath, RaceDto.class);

            System.out.printf("Current Name: %s%n", race.getRaceName());
            System.out.printf("Current Date: %s%n", race.getRaceDate());

            System.out.print("Enter new name (leave blank to keep current): ");
            String newName = scanner.nextLine();

            System.out.print("Enter new date (leave blank to keep current): ");
            String newDate = scanner.nextLine();

            String updatedName = newName.isEmpty() ? race.getRaceName() : newName;
            String updatedDate = newDate.isEmpty() ? String.valueOf(race.getRaceDate()) : newDate;

            RaceDto updatedRace = new RaceDto();
            updatedRace.setId(id);
            updatedRace.setRaceName(updatedName);
            updatedRace.setRaceDate(Date.valueOf(updatedDate));

            restClient.put("races/" + id, Mappers.get().writeValueAsString(updatedRace));

            System.out.println("Race successfully updated!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        } catch (Exception e) {
            System.out.println("Error updating race: " + e.getMessage());
        }
    }

    private void deleteRace() throws RequestException {
        System.out.print("Enter the ID of the race to delete: ");
        var raceId = readLine(in);

        restClient.delete("races/" + raceId, null);
        System.out.println("Race deleted successfully.");
    }

    private String readLine(BufferedReader in) {
        try {
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Error while reading input: " + e);
        }
    }
}
