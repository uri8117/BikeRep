package cat.uvic.teknos.gt3.clients.console.handlers;

import cat.uvic.teknos.gt3.clients.console.utils.Mappers;
import cat.uvic.teknos.gt3.clients.console.utils.RestClientImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import cat.uvic.teknos.gt3.clients.console.exceptions.RequestException;
import cat.uvic.teknos.gt3.clients.console.dto.BrandDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static cat.uvic.teknos.gt3.clients.console.App.readLine;

public class BrandCommandHandler {
    private final RestClientImpl restClient;
    private final BufferedReader in;

    public BrandCommandHandler(RestClientImpl restClient, BufferedReader in) {
        this.restClient = restClient;
        this.in = in;
    }

    public void manageBrand() {
        String command;
        do {
            showMenu();
            command = readLine(in);
            try {
                switch (command) {
                    case "1" -> listBrands();
                    case "2" -> getBrand();
                    case "3" -> createBrand();
                    case "4" -> updateBrand();
                    case "5" -> deleteBrand();
                    case "exit" -> System.out.println("Exiting Brand Management...");
                    default -> System.out.println("Invalid command.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
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
        BrandDto[] brands = restClient.getAll("brands", BrandDto[].class);
        if (brands == null || brands.length == 0) {
            System.out.println("No brands found.");
            return;
        }

        System.out.println("\n*** List of Brands ***");
        System.out.println(AsciiTable.getTable(Stream.of(brands).toList(), List.of(
                new Column().header("Id").with(b -> String.valueOf(b.getId())),
                new Column().header("Name").with(BrandDto::getName),
                new Column().header("Country").with(BrandDto::getCountry)
        )));
    }

    private void getBrand() throws RequestException, JsonProcessingException {
        System.out.print("Enter the ID of the brand: ");
        String brandId = readLine(in);

        try {
            BrandDto brand = restClient.get("brands/" + brandId, BrandDto.class);
            if (brand == null) {
                System.out.println("Brand not found.");
                return;
            }

            System.out.println("\n*** Brand Details ***");
            System.out.println(AsciiTable.getTable(List.of(brand), List.of(
                    new Column().header("Id").with(b -> String.valueOf(b.getId())),
                    new Column().header("Name").with(BrandDto::getName),
                    new Column().header("Country").with(BrandDto::getCountry)
            )));
        } catch (Exception e) {
            System.out.println("Error: Could not retrieve brand details. Please check the ID.");
        }
    }

    public void createBrand() {
        try {
            System.out.print("Enter brand name: ");
            String name = readLine(in);

            System.out.print("Enter brand country: ");
            String country = readLine(in);

            BrandDto brand = new BrandDto();
            brand.setName(name);
            brand.setCountry(country);

            String brandJson = Mappers.get().writeValueAsString(brand);
            restClient.post("brands", brandJson);

            System.out.println("Brand successfully created!");
        } catch (Exception e) {
            System.out.println("Error creating brand: " + e.getMessage());
        }
    }


    public void updateBrand() {
        try {
            System.out.print("Enter the ID of the brand to update: ");
            long id = Long.parseLong(readLine(in));

            System.out.print("Enter new brand name (leave blank to keep unchanged): ");
            String name = readLine(in);

            System.out.print("Enter new brand country (leave blank to keep unchanged): ");
            String country = readLine(in);

            BrandDto brand = restClient.get("brands/" + id, BrandDto.class);

            if (brand == null) {
                System.out.println("Brand not found.");
                return;
            }

            if (!name.isBlank()) {
                brand.setName(name);
            }
            if (!country.isBlank()) {
                brand.setCountry(country);
            }

            String brandJson = Mappers.get().writeValueAsString(brand);
            restClient.put("brands/" + id, brandJson);

            System.out.println("Brand successfully updated!");
        } catch (Exception e) {
            System.out.println("Error updating brand: " + e.getMessage());
        }
    }


    public void deleteBrand() {
        try {
            System.out.print("Enter the ID of the brand to delete: ");
            long id = Long.parseLong(readLine(in));
            restClient.delete("brands/" + id, null);
            System.out.println("Brand successfully deleted!");
        } catch (Exception e) {
            System.out.println("Error deleting brand: " + e.getMessage());
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
