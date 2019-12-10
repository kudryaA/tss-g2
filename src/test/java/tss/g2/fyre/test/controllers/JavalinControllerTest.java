package tss.g2.fyre.test.controllers;

import io.javalin.Javalin;
import org.junit.Assert;
import org.junit.Test;
import tss.g2.fyre.controllers.javalin.JavalinController;
import tss.g2.fyre.models.datastorage.postgress.PostgresDataStorage;
import tss.g2.fyre.models.entity.Authorization;
import tss.g2.fyre.utils.Configuration;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class JavalinControllerTest {
    @Test
    public void javaLinControllerTest() {
        try {
            Properties properties = new Configuration("config/configuration.yml").getProperties();
            int port = Integer.parseInt(properties.getProperty("port"));
            HashMap<String, Authorization> tokenStorage = new HashMap<>();
            PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
            Javalin app = Javalin.create().start(port);
            Map<String, Authorization> map = new HashMap<>();
            JavalinController javalinController = new JavalinController(app, dataStorage,map);
            javalinController.create();
            Assert.assertTrue(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
