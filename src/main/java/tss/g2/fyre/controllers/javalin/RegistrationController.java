package tss.g2.fyre.controllers.javalin;

import io.javalin.Javalin;
import tss.g2.fyre.controllers.CreateController;
import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.datastorage.DataStorage;

public class RegistrationController implements CreateController {
    private Javalin app;
    private DataStorage dataStorage;

    public RegistrationController(Javalin app, DataStorage dataStorage) {
        this.app = app;
        this.dataStorage = dataStorage;
    }

    @Override
    public void create() {
        app.post("/registration", ctx -> {
            String login = ctx.queryParam("login");
            String password = ctx.queryParam("password");
            String name = ctx.queryParam("name");
            String surname = ctx.queryParam("surname");
            String email = ctx.queryParam("email");

            Answer answer = new CheckRegistration().getAnswer();
        });
    }
}
