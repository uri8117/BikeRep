package cat.uvic.teknos.gt3.file.jbdc.app;

import cat.uvic.teknos.gt3.domain.models.ModelFactory;
import cat.uvic.teknos.gt3.domain.repositories.RepositoryFactory;
import cat.uvic.teknos.gt3.file.jbdc.models.JdbcModelFactory;
import cat.uvic.teknos.gt3.file.jbdc.repositories.JdbcRepositoryFactory;
import cat.uvic.teknos.gt3.file.jpa.models.JpaModelFactory;
import cat.uvic.teknos.gt3.file.jpa.repository.JpaRepositoryFactory;

import java.io.IOException;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws SQLException, IOException {

        RepositoryFactory repositoryFactory = new JdbcRepositoryFactory();
        ModelFactory modelFactory = new JdbcModelFactory();

        var backOffice = new BackOffice(System.in, System.out, repositoryFactory, modelFactory);

        backOffice.start();
    }
}
