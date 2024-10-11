package cat.uvic.teknos.gt3.file.jbdc.app;

import cat.uvic.teknos.gt3.domain.models.Race;

import cat.uvic.teknos.gt3.file.jbdc.models.RaceDriverId;
import cat.uvic.teknos.gt3.file.jbdc.models.Circuit;
import cat.uvic.teknos.gt3.file.jbdc.models.RaceDriver;

import cat.uvic.teknos.gt3.domain.repositories.RaceRepository;
import cat.uvic.teknos.gt3.domain.models.ModelFactory;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;


import java.io.BufferedReader;
import java.io.PrintStream;
import java.sql.Date;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static cat.uvic.teknos.gt3.file.jbdc.app.IOUtils.readLine;

public class RaceManager {

    private final PrintStream out;
    private final BufferedReader in;
    private final RaceRepository raceRepository;
    private final ModelFactory modelFactory;

    public RaceManager(BufferedReader in, PrintStream out, RaceRepository raceRepository, ModelFactory modelFactory) {
        this.out = out;
        this.in = in;
        this.raceRepository = raceRepository;
        this.modelFactory = modelFactory;
    }

    public void start() {
        var command = "";
        do {
            showMenu();
            command = readLine(in);

            switch (command) {
                case "1" -> insert();
                case "2" -> update();
                case "3" -> delete();
                case "4" -> get();
                case "5" -> getAll();
                default -> out.println("Invalid command");
            }

        } while (!command.equalsIgnoreCase("exit"));
        out.println("Exiting Race Management");
    }

    private void showMenu() {
        out.println("***Race Manager***");
        out.println("Type:");
        out.println("1 to insert a new Race");
        out.println("2 to update Race");
        out.println("3 to delete Race");
        out.println("4 to get a Race");
        out.println("5 to show all Races");
        out.println("'exit' to exit");
    }

    private void delete() {
        out.println("Please enter the id of the race you would like to delete");
        int id = Integer.parseInt(readLine(in));
        var race = modelFactory.createRace();
        race.setId(id);
        raceRepository.delete(race);
    }

    private void insert() {
        var race = modelFactory.createRace();
        var circuit = new Circuit();
        var raceDrivers = new HashSet<cat.uvic.teknos.gt3.domain.models.RaceDriver>();

        out.println("Circuit ID: ");
        circuit.setId(Integer.parseInt(readLine(in)));
        race.setCircuit(circuit);

        out.println("Race name: ");
        race.setRaceName(readLine(in));

        boolean validDate = false;
        while (!validDate) {
            out.println("Race date (YYYY-MM-DD): ");
            String dateInput = readLine(in);
            try {
                race.setRaceDate(Date.valueOf(dateInput));
                validDate = true;
            } catch (IllegalArgumentException e) {
                out.println("Invalid date format. Please enter the date in YYYY-MM-DD format.");
            }
        }

        out.println("Do you want to add drivers to the race? (yes/no)");
        if (readLine(in).equalsIgnoreCase("yes")) {
            insertDrivers(raceDrivers);
        }

        race.setRaceDrivers(raceDrivers);
        raceRepository.save(race);

        out.println("Inserted race successfully: " + race);
    }

    private void insertDrivers(HashSet<cat.uvic.teknos.gt3.domain.models.RaceDriver> raceDrivers) {
        out.println("Total drivers you want to add: ");
        int totalDrivers = Integer.parseInt(readLine(in));
        for (int i = 0; i < totalDrivers; i++) {
            var raceDriver = new RaceDriver();
            var raceDriverId = new RaceDriverId();

            out.println("Driver ID: ");
            raceDriverId.setDriverId(Integer.parseInt(readLine(in)));

            out.println("Driver position: ");
            raceDriver.setPosition(Integer.parseInt(readLine(in)));

            raceDriver.setId(raceDriverId);
            raceDrivers.add(raceDriver);
        }
    }

    private void update() {
        //var race = modelFactory.createRace();
        var raceDrivers = new HashSet<cat.uvic.teknos.gt3.domain.models.RaceDriver>();

        out.println("Please enter the race id you wish to update");
        int id = Integer.parseInt(readLine(in));
        var race = raceRepository.get(id);
        //race.setId(id);

        out.println("Do you want to update the race name? (yes/no)");
        if (readLine(in).equalsIgnoreCase("yes")) {
            out.println("New Race name: ");
            race.setRaceName(readLine(in));
        }

        out.println("Do you want to update the race date? (yes/no)");
        if (readLine(in).equalsIgnoreCase("yes")) {
            out.println("New Race date (YYYY-MM-DD): ");
            race.setRaceDate(Date.valueOf(readLine(in)));
        }

        out.println("Do you want to update the drivers in the race? (yes/no)");
        if (readLine(in).equalsIgnoreCase("yes")) {
            insertDrivers(raceDrivers);
        }

        race.setRaceDrivers(raceDrivers);
        raceRepository.save(race);
    }

    private void getAll() {
        out.println("\n*** List of Races ***\n");
        var races = raceRepository.getAll();
        out.println(AsciiTable.getTable(races, Arrays.asList(
                new Column().header("Id").with(race -> String.valueOf(race.getId())),
                new Column().header("Circuit ID").with(race -> String.valueOf(race.getCircuit().getId())),
                new Column().header("Race Name").with(Race::getRaceName),
                new Column().header("Race Date").with(race -> String.valueOf(race.getRaceDate()))
        )));
    }

    private void get() {
        out.println("Please enter the race id you wish to search");
        int id = Integer.parseInt(readLine(in));
        var race = raceRepository.get(id);

        if (race == null) {
            out.println("Race not found");
            return;
        }

        out.println("\n*** Race Details ***\n");
        out.println(AsciiTable.getTable(List.of(race), Arrays.asList(
                new Column().header("Id").with(r -> String.valueOf(r.getId())),
                new Column().header("Race Name").with(Race::getRaceName),
                new Column().header("Circuit ID").with(r -> String.valueOf(r.getCircuit().getId())),
                new Column().header("Race Date").with(r -> String.valueOf(r.getRaceDate()))
        )));

        out.println("\n*** Drivers in Race ***\n");
        out.println(AsciiTable.getTable(race.getRaceDrivers(), Arrays.asList(
                new Column().header("Driver ID").with(rd -> String.valueOf(rd.getId().getDriverId())),
                new Column().header("Position").with(rd -> String.valueOf(rd.getPosition()))
        )));
    }
}
