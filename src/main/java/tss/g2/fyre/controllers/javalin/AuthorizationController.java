package tss.g2.fyre.controllers.javalin;

import io.javalin.Javalin;

import java.util.Map;

import tss.g2.fyre.controllers.CreateController;
import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.actions.auth.RegisterModerator;
import tss.g2.fyre.models.actions.auth.check.ModeratorAuthUser;
import tss.g2.fyre.models.actions.simple.CheckAuthorization;
import tss.g2.fyre.models.actions.simple.CheckModerator;
import tss.g2.fyre.models.actions.simple.RegisterUser;
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
   * @param tokenStorage storage with authorization info
   */
  public AuthorizationController(Javalin app, DataStorage dataStorage, Map<String,
      Authorization> tokenStorage) {
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
        String token = new RandomString(tokenSize).generate();
        ctx.sessionAttribute("token", token);
        tokenStorage.put(token, new Authorization(login, false));
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
        String token = new RandomString(tokenSize).generate();
        ctx.sessionAttribute("token", token);
        tokenStorage.put(token, new Authorization(login, false));
      }
      ctx.result(answer.toJson());
    });
    
    app.post("/login/moderator", ctx -> {
      String login = ctx.formParam("login");
      String password = ctx.formParam("password");
      Answer answer = new CheckModerator(dataStorage, login, password).getAnswer();
      boolean status = (boolean) answer.getObj();
      if (status) {
        String token = new RandomString(tokenSize).generate();
        ctx.sessionAttribute("token", token);
        tokenStorage.put(token, new Authorization(login, true));
      }
      ctx.result(answer.toJson());
    });

    app.post("/registration/moderator", ctx -> {
      String name = ctx.formParam("name");
      String login = ctx.formParam("login");
      String password = ctx.formParam("password");
      Answer answer = new ModeratorAuthUser(
          new RegisterModerator(dataStorage, login, password, name),
          ctx.sessionAttribute("token"),
          tokenStorage
      ).getAnswer();
      boolean status = (boolean) answer.getObj();
      if (status) {
        String token = new RandomString(tokenSize).generate();
        ctx.sessionAttribute("token", token);
        tokenStorage.put(token, new Authorization(login, true));
      }
      ctx.result(answer.toJson());
    });

    app.post("/session", ctx -> {
      String token = ctx.sessionAttribute("token");
      Authorization authorization = tokenStorage.get(token);
      Answer answer = new Answer<>(false);
      if (authorization != null) {
        answer = new Answer<>(true, authorization);
      }
      ctx.result(answer.toJson());
    });

    app.post("/logout", ctx -> {
      String token = ctx.sessionAttribute("token");
      ctx.sessionAttribute("token", null);
      tokenStorage.remove(token);
      Authorization authorization = tokenStorage.get(token);
      Answer answer = new Answer<>(true);
      ctx.result(answer.toJson());
    });

  }
}
