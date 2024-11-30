// App.java
package cat.uvic.teknos.gt3.clients.console;

import cat.uvic.teknos.gt3.clients.console.exceptions.RequestException;
import cat.uvic.teknos.gt3.clients.console.handlers.*;
import cat.uvic.teknos.gt3.clients.console.utils.RestClientImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class App {
    private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private static PrintStream out = new PrintStream(System.out);
    private static RestClientImpl restClient = new RestClientImpl("localhost", 8070);

    public static void main(String[] args) throws RequestException, IOException {

        String command = "";
        do {
            showMainMenu();
            command = readLine(in);

            switch (command) {
                case "1" -> manageBrand();
                case "2" -> manageBike();
                case "3" -> manageUser();
                case "exit" -> out.println("Exiting the program.");
                default -> out.println("Invalid command. Try again.");
            }
        } while (!command.equals("exit"));

        out.println("Program terminated.");
    }

    private static void showMainMenu() {
        out.println("\n*** Main Menu ***");
        out.println("1. Manage Brand");
        out.println("2. Manage Bike");
        out.println("3. Manage User");
        out.println("Type 'exit' to quit.");
        out.print("Enter command: ");
    }

    public static String readLine(BufferedReader in) {
        String command;
        try {
            command = in.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Error while reading the menu option: " + e);
        }
        return command;
    }

    private static void manageBrand() throws RequestException, IOException {
        BrandCommandHandler brandHandler = new BrandCommandHandler(restClient, in);
        brandHandler.manageBrand();  // Delegating to the Brand handler
    }

    private static void manageBike() throws RequestException, IOException {
        BikeCommandHandler bikeHandler = new BikeCommandHandler(restClient, in);
        bikeHandler.manageBike();  // Delegating to the Bike handler
    }

    private static void manageUser() throws RequestException, IOException {
        UserCommandHandler userHandler = new UserCommandHandler(restClient, in);
        userHandler.manageUser();  // Delegating to the User handler
    }
}
