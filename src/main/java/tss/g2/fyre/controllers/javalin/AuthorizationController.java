package tss.g2.fyre.controllers.javalin;

import io.javalin.Javalin;

import tss.g2.fyre.controllers.CreateController;
import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.actions.ChangeBannedStatus;
import tss.g2.fyre.models.actions.CheckAuthorization;
import tss.g2.fyre.models.actions.CheckModerator;
import tss.g2.fyre.models.actions.GetUsers;
import tss.g2.fyre.models.actions.RegisterModerator;
import tss.g2.fyre.models.actions.RegisterUser;
import tss.g2.fyre.models.datastorage.DataStorage;

/**
 * Person controller for javalin.
 * @author Anton Kudryavtsev
 */
public class AuthorizationController implements CreateController {

  private Javalin app;
  private DataStorage dataStorage;

  /**
   * Constructor.
   * @param app javalin app
   * @param dataStorage storage of data
   */
  public AuthorizationController(Javalin app, DataStorage dataStorage) {
    this.app = app;
    this.dataStorage = dataStorage;
  }

  @Override
  public void create() {
    app.post("/login", ctx -> {
      String login = ctx.formParam("login");
      String password = ctx.formParam("password");
      Answer answer = new CheckAuthorization(dataStorage, login, password).getAnswer();
      ctx.result(answer.toJson());
    });

    app.post("/registration", ctx -> {
      String login = ctx.formParam("login");
      String password = ctx.formParam("password");
      String name = ctx.formParam("name");
      String surname = ctx.formParam("surname");
      String email = ctx.formParam("email");

      Answer answer = new RegisterUser(dataStorage, login, password, name, surname, email)
          .getAnswer();
      ctx.result(answer.toJson());
    });
    
    app.post("/login/moderator", ctx -> {
      String login = ctx.formParam("login");
      String password = ctx.formParam("password");
      Answer answer = new CheckModerator(dataStorage, login, password).getAnswer();
      ctx.result(answer.toJson());
    });

    app.post("/registration/moderator", ctx -> {
      String name = ctx.formParam("name");
      String login = ctx.formParam("login");
      String password = ctx.formParam("password");

      Answer answer = new RegisterModerator(dataStorage, login, password, name)
          .getAnswer();
      ctx.result(answer.toJson());
    });

    app.post("/change/status", ctx -> {
      String userLogin = ctx.formParam("userLogin");

      Answer answer = new ChangeBannedStatus(dataStorage, userLogin).getAnswer();
      ctx.result(answer.toJson());
    });

    app.post("/select/users", ctx -> ctx.result(new GetUsers(dataStorage).getAnswer().toJson()));
  }
}
