package cat.uvic.teknos.gt3.clients.console.handlers;

import cat.uvic.teknos.gt3.clients.console.utils.Mappers;
import cat.uvic.teknos.gt3.clients.console.utils.RestClientImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import cat.uvic.teknos.gt3.clients.console.exceptions.RequestException;
import cat.uvic.teknos.gt3.clients.console.dto.UserDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public class UserCommandHandler {
    private final RestClientImpl restClient;
    private final BufferedReader in;

    public UserCommandHandler(RestClientImpl restClient, BufferedReader in) {
        this.restClient = restClient;
        this.in = in;
    }

    public void manageUser() {
        String command;
        do {
            showMenu();
            command = readLine(in);
            try {
                switch (command) {
                    case "1" -> listUsers();
                    case "2" -> getUser();
                    case "3" -> createUser();
                    case "4" -> updateUser();
                    case "5" -> deleteUser();
                    case "exit" -> System.out.println("Exiting User Management...");
                    default -> System.out.println("Invalid command.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        } while (!command.equalsIgnoreCase("exit"));
    }

    private void showMenu() {
        System.out.println("\n*** User Manager ***");
        System.out.println("1. List all users");
        System.out.println("2. Get user details");
        System.out.println("3. Add new user");
        System.out.println("4. Update user");
        System.out.println("5. Delete user");
        System.out.println("Type 'exit' to quit");
        System.out.print("Enter command: ");
    }

    private void listUsers() throws RequestException, JsonProcessingException {
        UserDto[] users = restClient.getAll("users", UserDto[].class);
        if (users == null || users.length == 0) {
            System.out.println("No users found.");
            return;
        }

        System.out.println("\n*** List of Users ***");
        System.out.println(AsciiTable.getTable(Stream.of(users).toList(), List.of(
                new Column().header("Id").with(u -> String.valueOf(u.getId())),
                new Column().header("Name").with(UserDto::getName),
                new Column().header("Email").with(UserDto::getEmail)
        )));
    }

    private void getUser() throws RequestException, JsonProcessingException {
        System.out.print("Enter the ID of the user: ");
        String userId = readLine(in);

        try {
            UserDto user = restClient.get("users/" + userId, UserDto.class);
            if (user == null) {
                System.out.println("User not found.");
                return;
            }

            System.out.println("\n*** User Details ***");
            System.out.println(AsciiTable.getTable(List.of(user), List.of(
                    new Column().header("Id").with(u -> String.valueOf(u.getId())),
                    new Column().header("Name").with(UserDto::getName),
                    new Column().header("Email").with(UserDto::getEmail),
                    new Column().header("Bikes").with(u -> u.getBikes().toString())
            )));
        } catch (Exception e) {
            System.out.println("Error: Could not retrieve user details. Please check the ID.");
        }
    }

    public void createUser() {
        try {
            System.out.print("Enter user name: ");
            String name = readLine(in);

            System.out.print("Enter user email: ");
            String email = readLine(in);

            UserDto user = new UserDto();
            user.setName(name);
            user.setEmail(email);

            String userJson = Mappers.get().writeValueAsString(user);
            restClient.post("users", userJson);

            System.out.println("User successfully created!");
        } catch (Exception e) {
            System.out.println("Error creating user: " + e.getMessage());
        }
    }

    public void updateUser() {
        try {
            System.out.print("Enter the ID of the user to update: ");
            long id = Long.parseLong(readLine(in));

            System.out.print("Enter new user name (leave blank to keep unchanged): ");
            String name = readLine(in);

            System.out.print("Enter new user email (leave blank to keep unchanged): ");
            String email = readLine(in);

            UserDto user = restClient.get("users/" + id, UserDto.class);

            if (user == null) {
                System.out.println("User not found.");
                return;
            }

            if (!name.isBlank()) {
                user.setName(name);
            }
            if (!email.isBlank()) {
                user.setEmail(email);
            }

            String userJson = Mappers.get().writeValueAsString(user);
            restClient.put("users/" + id, userJson);

            System.out.println("User successfully updated!");
        } catch (Exception e) {
            System.out.println("Error updating user: " + e.getMessage());
        }
    }


    public void deleteUser() {
        try {
            System.out.print("Enter the ID of the user to delete: ");
            long id = Long.parseLong(readLine(in));
            restClient.delete("users/" + id, null);
            System.out.println("User successfully deleted!");
        } catch (Exception e) {
            System.out.println("Error deleting user: " + e.getMessage());
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
