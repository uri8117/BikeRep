package cat.uvic.teknos.gt3.clients.console.handlers;

import cat.uvic.teknos.gt3.clients.console.dto.BrandDto;
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

public class BrandCommandHandler {
    private final RestClientImpl restClient;
    private final BufferedReader in;
    private static final Scanner scanner = new Scanner(System.in);

    public BrandCommandHandler(RestClientImpl restClient, BufferedReader in) {
        this.restClient = restClient;
        this.in = in;
    }

    public void manageBrand() throws RequestException, JsonProcessingException {
        String command;
        do {
            showMenu();
            command = readLine(in);
            switch (command) {
                case "1" -> listBrands();
                case "2" -> getBrand();
                case "3" -> createBrand();
                case "4" -> updateBrand();
                case "5" -> deleteBrand();
                case "exit" -> System.out.println("Exiting Brand Management...");
                default -> System.out.println("Invalid command.");
            }
        } while (!command.equalsIgnoreCase("exit"));
    }

    private void showMenu() {
        System.out.println("\n*** Brand Manager ***");
        System.out.println("1. List all brands");
        System.out.println("2. Get brand details");
        System.out.println("3. Add new brand");
        System.out.println("4. Update brand");
        System.out.println("5. Delete brand");
        System.out.println("Type 'exit' to quit");
        System.out.print("Enter command: ");
    }

    private void listBrands() throws RequestException, JsonProcessingException {
        var brands = restClient.getAll("brands", BrandDto[].class);
        System.out.println("\n*** List of Brands ***");
        if (brands.length == 0) {
            System.out.println("No brands found.");
            return;
        }
        System.out.println(AsciiTable.getTable(Stream.of(brands).toList(), List.of(
                new Column().header("Id").with(b -> String.valueOf(b.getId())),
                new Column().header("Name").with(BrandDto::getBrandName)
        )));
    }

    private void getBrand() throws RequestException, JsonProcessingException {
        System.out.print("Enter the ID of the brand: ");
        var brandId = readLine(in);
        var brand = restClient.get("brands/" + brandId, BrandDto.class);

        if (brand == null) {
            System.out.println("Brand not found.");
            return;
        }

        System.out.println("\n*** Brand Details ***");
        System.out.println(AsciiTable.getTable(List.of(brand), List.of(
                new Column().header("Id").with(b -> String.valueOf(b.getId())),
                new Column().header("Name").with(BrandDto::getBrandName)
        )));
    }

    public void createBrand() {
        System.out.print("Enter brand name: ");
        String name = scanner.nextLine();

        try {
            if (name.isEmpty()) {
                System.out.println("Brand name is required!");
                return;
            }

            var brand = new BrandDto();
            brand.setBrandName(name);

            String json = Mappers.get().writeValueAsString(brand);
            System.out.println("Sending JSON: " + json);

            restClient.post("brands", json);

            System.out.println("Brand successfully created!");
        } catch (Exception e) {
            System.out.println("Error creating brand: " + e.getMessage());
        }
    }

    public void updateBrand() {
        System.out.print("Enter the ID of the brand to update: ");
        String idStr = scanner.nextLine();

        try {
            int id = Integer.parseInt(idStr);

            // Get current details
            String detailsPath = String.format("brands/%d", id);
            BrandDto brand = restClient.get(detailsPath, BrandDto.class);

            System.out.printf("Current Name: %s%n", brand.getBrandName());

            System.out.print("Enter new name (leave blank to keep current): ");
            String newName = scanner.nextLine();

            String updatedName = newName.isEmpty() ? brand.getBrandName() : newName;

            BrandDto updatedBrand = new BrandDto();
            updatedBrand.setId(id);
            updatedBrand.setBrandName(updatedName);

            restClient.put("brands/" +  id, Mappers.get().writeValueAsString(updatedBrand));

            System.out.println("Brand successfully updated!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        } catch (Exception e) {
            System.out.println("Error updating brand: " + e.getMessage());
        }
    }

    private void deleteBrand() throws RequestException {
        System.out.print("Enter the ID of the brand to delete: ");
        var brandId = readLine(in);

        restClient.delete("brands/" + brandId, null);
        System.out.println("Brand deleted successfully.");
    }

    private String readLine(BufferedReader in) {
        try {
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Error while reading input: " + e);
        }
    }
}
