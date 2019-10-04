package g2.tss.fyre;

import g2.tss.fyre.controllers.CreateController;
import g2.tss.fyre.controllers.JavalinController;
import g2.tss.fyre.models.datastorage.DataStorage;
import g2.tss.fyre.models.datastorage.postgress.PostgresDataStorage;
import io.javalin.Javalin;

import java.sql.SQLException;

public class Application {
    private static CreateController controller;
    private static DataStorage dataStorage;
    public static void main(String[] args) throws SQLException {
        Javalin app = Javalin.create().start(7000);
        dataStorage = new PostgresDataStorage();
        controller = new JavalinController(app);
    }
}
