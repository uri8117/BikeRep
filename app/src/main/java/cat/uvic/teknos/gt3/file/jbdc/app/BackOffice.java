package cat.uvic.teknos.gt3.file.jbdc.app;

import java.io.*;
import java.sql.SQLException;

import cat.uvic.teknos.gt3.domain.models.ModelFactory;
import cat.uvic.teknos.gt3.domain.repositories.RepositoryFactory;
import static cat.uvic.teknos.gt3.file.jbdc.app.IOUtils.*;

public class BackOffice {

    private final BufferedReader in;
    private final PrintStream out;
    private final RepositoryFactory repositoryFactory;
    private final ModelFactory modelFactory;

    public BackOffice(InputStream inputStream, OutputStream outputStream, RepositoryFactory repositoryFactory, ModelFactory modelFactory) {
        this.in = new BufferedReader(new InputStreamReader(inputStream));
        this.out = new PrintStream(outputStream);
        this.repositoryFactory = repositoryFactory;
        this.modelFactory = modelFactory;
    }

    public void start() throws SQLException {
        showWelcomeMessage();

        var command = "";
        do {
            showMainMenu();
            command = readLine(in);

            switch (command) {
                case "1" -> managerCar();
                case "2" -> managerRace();
            }

        } while (!command.equals("exit"));

        out.println("\n*** Program Finished ***\n");
    }

    private void managerCar() throws SQLException {
        new CarManager(in, out, repositoryFactory.getCarRepository(), modelFactory).start();
    }

    private void managerRace() throws SQLException {
        new RaceManager(in, out, repositoryFactory.getRaceRepository(), modelFactory).start();
    }


    private void showWelcomeMessage() {
        out.println("\n*** Welcome to the GT3 Back Office ***\n");
        out.println("Select a menu option:");
        out.println();
    }

    private void showMainMenu() {
        out.println("\n*** Main Menu ***\n");
        out.println("1. Cars");
        out.println("2. Races");
        out.println("\nType 'exit' to quit.");
    }
}
