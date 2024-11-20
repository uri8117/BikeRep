package cat.uvic.teknos.gt3.clients.console;

import cat.uvic.teknos.gt3.clients.console.dto.*;
import cat.uvic.teknos.gt3.clients.console.exceptions.RequestException;
import cat.uvic.teknos.gt3.clients.console.handlers.*;
import cat.uvic.teknos.gt3.clients.console.utils.Mappers;
import cat.uvic.teknos.gt3.clients.console.utils.RestClientImpl;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.sql.SQLException;

import static org.apache.logging.log4j.util.LambdaUtil.getAll;

public class App {
    private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private static PrintStream out = new PrintStream(System.out);
    private static RestClientImpl restClient= new RestClientImpl("localhost", 8080);

    public static void main(String[] args) throws RequestException, IOException {
        showMainMenu();

        var command = "";
        do {
            showMainMenu();
            command = readLine(in);

            switch (command){
                case "1" -> manageBrand();
                case "2" -> manageCar();
                case "3" -> manageCircuit();
                case "4" -> manageDriver();
                case "5" -> manageRace();

            }

        }

        while (!command.equals("exit"));

        out.println("Fi del programa");

    }

    private static void showMainMenu() {
        out.println("1. Brand");
        out.println("2. Car");
        out.println("3. Circuit");
        out.println("4. Driver");
        out.println("5. Race");
    }

    static String readLine(BufferedReader in ){
        String command;
        try {
            command = in.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Error while reading the menu option "+e);
        }
        return command;
    }

    private static void manageBrand() throws RequestException, JsonProcessingException, IOException {
        BrandCommandHandler brandHandler = new BrandCommandHandler(restClient, in);
        brandHandler.manageBrand();  // Delegamos la gestión de marcas al nuevo handler
    }

    private static void manageCar() throws RequestException, JsonProcessingException, IOException {
        CarCommandHandler carHandler = new CarCommandHandler(restClient, in);
        carHandler.manageCar();  // Delegamos la gestión de marcas al nuevo handler
    }

    private static void manageCircuit() throws RequestException, JsonProcessingException {
        CircuitCommandHandler circuitHandler = new CircuitCommandHandler(restClient, in);
        circuitHandler.manageCircuit();  // Delegate the handling to the new class
    }

    private static void manageDriver() throws RequestException, JsonProcessingException {
        DriverCommandHandler driverHandler = new DriverCommandHandler(restClient, in);
        driverHandler.manageDriver();  // Delegate the handling to the new class
    }

    private static void manageRace() throws RequestException, JsonProcessingException {
        RaceCommandHandler raceHandler = new RaceCommandHandler(restClient, in);
        raceHandler.manageRace();  // Delegate the handling to the new class
    }

}
