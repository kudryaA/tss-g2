package tss.g2.fyre.controllers.javalin;

import io.javalin.Javalin;
import java.util.HashMap;
import java.util.Map;

import tss.g2.fyre.controllers.CreateController;
import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.actions.CheckAuthorization;
import tss.g2.fyre.models.actions.CheckModerator;
import tss.g2.fyre.models.actions.RegisterModerator;
import tss.g2.fyre.models.actions.RegisterUser;
import tss.g2.fyre.models.datastorage.DataStorage;
import tss.g2.fyre.models.entity.Authorization;
import tss.g2.fyre.utils.RandomString;

/**
 * Person controller for javalin.
 * @author Anton Kudryavtsev
 */
public class AuthorizationController implements CreateController {

  private Javalin app;
  private DataStorage dataStorage;
  private Map<String, Authorization> tokenStorage;
  private static final int tokenSize = 50;

  /**
   * Constructor.
   * @param app javalin app
   * @param dataStorage storage of data
   */
  public AuthorizationController(Javalin app, DataStorage dataStorage, Map<String, Authorization> tokenStorage) {
    this.app = app;
    this.dataStorage = dataStorage;
    this.tokenStorage = tokenStorage;
  }

  @Override
  public void create() {
    app.post("/login", ctx -> {
      String login = ctx.formParam("login");
      String password = ctx.formParam("password");
      Answer answer = new CheckAuthorization(dataStorage, login, password).getAnswer();
      boolean status = (boolean) answer.getObj();
      if (status) {
        String token = new RandomString(40).generate();
        ctx.sessionAttribute("token", token);
        tokenStorage.put(login, new Authorization(login, false));
      }
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
      boolean status = (boolean) answer.getObj();
      if (status) {
        String token = new RandomString(40).generate();
        ctx.sessionAttribute("token", token);
        tokenStorage.put(login, new Authorization(login, false));
      }
      ctx.result(answer.toJson());
    });
    
    app.post("/login/moderator", ctx -> {
      String login = ctx.formParam("login");
      String password = ctx.formParam("password");
      Answer answer = new CheckModerator(dataStorage, login, password).getAnswer();
      boolean status = (boolean) answer.getObj();
      if (status) {
        String token = new RandomString(40).generate();
        ctx.sessionAttribute("token", token);
        tokenStorage.put(login, new Authorization(login, true));
      }
      ctx.result(answer.toJson());
    });

    app.post("/registration/moderator", ctx -> {
      String name = ctx.formParam("name");
      String login = ctx.formParam("login");
      String password = ctx.formParam("password");
      Answer answer = new RegisterModerator(dataStorage, login, password, name)
          .getAnswer();
      boolean status = (boolean) answer.getObj();
      if (status) {
        String token = new RandomString(40).generate();
        ctx.sessionAttribute("token", token);
        tokenStorage.put(login, new Authorization(login, true));
      }
      ctx.result(answer.toJson());
    });

    app.post("/session", ctx -> {
      String token = ctx.sessionAttribute("token");
      Authorization authorization = tokenStorage.get(token);
      Answer<Authorization> answer = new Answer<>(false);
      if (authorization != null) {
        answer = new Answer<>(true, authorization);
      }
      ctx.result(answer.toJson());
    });
  }
}
