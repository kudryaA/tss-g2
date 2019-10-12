package tss.g2.fyre;

import io.javalin.Javalin;
import java.sql.SQLException;
import java.util.Properties;

import tss.g2.fyre.controllers.CreateController;
import tss.g2.fyre.controllers.javalin.JavalinController;
import tss.g2.fyre.models.actions.RegisterUser;
import tss.g2.fyre.models.datastorage.DataStorage;
import tss.g2.fyre.models.datastorage.postgress.PostgresDataStorage;

public class Application {

  public static void main(String[] args) throws SQLException {
    Properties databaseProperties = new Properties();
    databaseProperties.setProperty("host", "127.0.0.1");
    databaseProperties.setProperty("port", "5432");
    databaseProperties.setProperty("database", "postgres");
    databaseProperties.setProperty("user", "postgres");
    databaseProperties.setProperty("password", "191195");
    Javalin app = Javalin.create().start(7000);
    DataStorage dataStorage = new PostgresDataStorage(databaseProperties);
    //System.out.println(new RegisterUser(dataStorage, "c", "a", "a", "a", "a").getAnswer().toJson());
    CreateController controller = new JavalinController(app, dataStorage);
    controller.create();
  }
}
