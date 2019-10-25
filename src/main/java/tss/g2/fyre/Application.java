package tss.g2.fyre;

import io.javalin.Javalin;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

import tss.g2.fyre.controllers.CreateController;
import tss.g2.fyre.controllers.javalin.JavalinController;
import tss.g2.fyre.utils.Configuration;
import tss.g2.fyre.models.datastorage.DataStorage;
import tss.g2.fyre.models.datastorage.postgress.PostgresDataStorage;
import tss.g2.fyre.models.entity.Authorization;

/**
 * Main class.
 */
public class Application {

  /**
   * Main method.
   * @param args system args
   * @throws SQLException sql exception
   */
  public static void main(String[] args) throws SQLException {
    new File("images").mkdirs();
    Properties properties = new Configuration("config/configuration.yml").getProperties();
    int port = Integer.parseInt(properties.getProperty("port"));
    HashMap<String, Authorization> tokenStorage = new HashMap<>();
    Javalin app = Javalin.create().start(port);
    app.config.addStaticFiles("pages");
    DataStorage dataStorage = new PostgresDataStorage(properties);
    CreateController controller = new JavalinController(app, dataStorage, tokenStorage);
    controller.create();
  }
}
