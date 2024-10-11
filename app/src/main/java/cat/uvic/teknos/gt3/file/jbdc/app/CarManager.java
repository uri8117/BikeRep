package cat.uvic.teknos.gt3.file.jbdc.app;

import cat.uvic.teknos.gt3.domain.models.Car;
import cat.uvic.teknos.gt3.file.jbdc.models.CarData;

import cat.uvic.teknos.gt3.file.jbdc.models.Brand;
import cat.uvic.teknos.gt3.domain.repositories.CarRepository;
import cat.uvic.teknos.gt3.domain.models.ModelFactory;

import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import com.github.freva.asciitable.ColumnData;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import static cat.uvic.teknos.gt3.file.jbdc.app.IOUtils.readLine;

public class CarManager {

    private final PrintStream out;
    private final BufferedReader in;
    private final CarRepository carRepository;
    private final ModelFactory modelFactory;

    public CarManager(BufferedReader in, PrintStream out, CarRepository carRepository, ModelFactory modelFactory) {
        this.out = out;
        this.in = in;
        this.carRepository = carRepository;
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
        out.println("Exiting Car Management");
    }

    private void showMenu() {
        out.println("***Car Manager***");
        out.println("Type:");
        out.println("1 to insert a new Car");
        out.println("2 to update Car");
        out.println("3 to delete Car");
        out.println("4 to get a Car");
        out.println("5 to show all Cars");
        out.println("'exit' to exit");
    }

    private void delete() {
        out.println("Please enter the id of the car you would like to delete");
        int id = Integer.parseInt(readLine(in));
        var car = modelFactory.createCar();
        car.setId(id);
        carRepository.delete(car);
    }

    private void insert() {
        var car = modelFactory.createCar();
        var carData = new CarData();
        var brand = new Brand();

        out.println("Car Model Name: ");
        car.setModelName(readLine(in));

        out.println("Brand ID: ");
        brand.setId(Integer.parseInt(readLine(in)));
        car.setBrand(brand);

        out.println("HorsePower: ");
        carData.setHorsepower(Integer.parseInt(readLine(in)));

        out.println("Weight: ");
        carData.setWeight(Integer.parseInt(readLine(in)));

        car.setCarData(carData);
        carRepository.save(car);

        out.println("Inserted car successfully: " + car);
    }

    private void update() {
        out.println("Please enter the car id you wish to update");
        int id = Integer.parseInt(readLine(in));
        var car = carRepository.get(id);

        out.println("Do you want to update the car model name? (yes/no)");
        if (readLine(in).equalsIgnoreCase("yes")) {
            out.println("New Car Model Name: ");
            car.setModelName(readLine(in));
        }

        var carData = car.getCarData();

        out.println("Do you want to update the HorsePower? (yes/no)");
        if (readLine(in).equalsIgnoreCase("yes")) {
            out.println("New HorsePower: ");
            carData.setHorsepower(Integer.parseInt(readLine(in)));
        }

        out.println("Do you want to update the weight? (yes/no)");
        if (readLine(in).equalsIgnoreCase("yes")) {
            out.println("New Weight: ");
            carData.setWeight(Integer.parseInt(readLine(in)));
        }

        car.setCarData(carData);
        carRepository.save(car);
    }

    private void getAll() {
        out.println("\n*** List of Cars ***\n");
        var cars = carRepository.getAll();
        out.println(AsciiTable.getTable(cars, Arrays.asList(
                new Column().header("Id").with(c -> String.valueOf(c.getId())),
                new Column().header("Brand ID").with(c -> String.valueOf(c.getBrand().getId())),
                new Column().header("Model Name").with(Car::getModelName),
                new Column().header("Power").with(c -> String.valueOf(c.getCarData().getHorsepower())),
                new Column().header("Weight").with(c -> String.valueOf(c.getCarData().getWeight()))
        )));
    }

    private void get() {
        out.println("Please enter the car id you wish to search");
        int id = Integer.parseInt(readLine(in));
        var car = carRepository.get(id);

        if (car == null) {
            out.println("Car not found.");
            return;
        }

        out.println("\n*** Car Details ***\n");
        out.println(AsciiTable.getTable(List.of(car), Arrays.asList(
                new Column().header("Id").with(c -> String.valueOf(c.getId())),
                new Column().header("Brand ID").with(c -> c.getBrand() != null ? String.valueOf(c.getBrand().getId()) : "N/A"),
                new Column().header("Model Name").with(Car::getModelName),
                new Column().header("Power").with(c -> String.valueOf(c.getCarData().getHorsepower())),
                new Column().header("Weight").with(c -> String.valueOf(c.getCarData().getWeight()))
        )));
    }

}
