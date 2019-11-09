package tss.g2.fyre.controllers.javalin;

import io.javalin.Javalin;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tss.g2.fyre.controllers.CreateController;
import tss.g2.fyre.controllers.utils.UserLogin;
import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.actions.Action;
import tss.g2.fyre.models.actions.ActionTime;
import tss.g2.fyre.models.actions.simple.CheckAuthorization;
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
  private static Logger logger = LoggerFactory.getLogger(AuthorizationController.class);

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
      logger.info("Request to /login user {}", login);
      String password = ctx.formParam("password");
      Action action = new CheckAuthorization(dataStorage, login, password);
      Answer answer = new ActionTime("/login", action, dataStorage).getAnswer();
      boolean status = (boolean) answer.getObj();
      if (status) {
        String token = new RandomString(tokenSize).generate();
        ctx.sessionAttribute("token", token);
        tokenStorage.put(token, new Authorization(login, dataStorage.getRole(login)));
      }
      ctx.result(answer.toJson());
    });

    app.post("/registration", ctx -> {
      String login = ctx.formParam("login");
      logger.info("Request to /registration user {}", login);
      String password = ctx.formParam("password");
      String name = ctx.formParam("name");
      String surname = ctx.formParam("surname");
      String email = ctx.formParam("email");
      Action action = new RegisterUser(dataStorage, login, password, name, surname, email);
      Answer answer = new ActionTime("/registration", action, dataStorage).getAnswer();
      boolean status = (boolean) answer.getObj();
      if (status) {
        String token = new RandomString(tokenSize).generate();
        ctx.sessionAttribute("token", token);
        tokenStorage.put(token, new Authorization(login, "user"));
      }
      ctx.result(answer.toJson());
    });

    app.post("/session", ctx -> {
      String token = ctx.sessionAttribute("token");
      Authorization authorization = tokenStorage.get(token);
      logger.info("Request to /session");
      Answer answer = new Answer<>(false);
      if (authorization != null) {
        answer = new Answer<>(true, authorization);
      }
      ctx.result(answer.toJson());
    });

    app.post("/logout", ctx -> {
      String token = ctx.sessionAttribute("token");
      Authorization authorization = tokenStorage.get(token);
      logger.info("Request to /logout with user {}", new UserLogin(tokenStorage, token).get());
      ctx.sessionAttribute("token", null);
      tokenStorage.remove(token);
      Answer answer = new Answer<>(true);
      ctx.result(answer.toJson());
    });

  }
}
