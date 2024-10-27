package cat.uvic.teknos.gt3.services;

import cat.uvic.teknos.gt3.domain.models.ModelFactory;
import cat.uvic.teknos.gt3.domain.repositories.RepositoryFactory;
import cat.uvic.teknos.gt3.file.jbdc.repositories.JdbcRaceRepository;
import cat.uvic.teknos.gt3.services.controllers.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Properties;

public class App {
    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        var properties = new Properties();
        properties.load(App.class.getResourceAsStream("/app.properties"));

        RepositoryFactory repositoryFactory = (RepositoryFactory) Class.forName(properties.getProperty("repositoryFactory")).getConstructor().newInstance();
        ModelFactory modelFactory = (ModelFactory) Class.forName(properties.getProperty("modelFactory")).getConstructor().newInstance();

        var controllers = new HashMap<String, Controller>();
        controllers.put("drivers", new DriverController(repositoryFactory, modelFactory));
        controllers.put("brands", new BrandController(repositoryFactory, modelFactory));
        controllers.put("circuits", new CircuitController(repositoryFactory, modelFactory));
        controllers.put("cars", new CarController(repositoryFactory, modelFactory));
        controllers.put("races", new RaceController(repositoryFactory, modelFactory));

        var requestRouter = new RequestRouterImpl(controllers);

        new Server(requestRouter).start();
    }
}
