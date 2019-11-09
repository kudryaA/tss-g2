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
import tss.g2.fyre.models.actions.auth.ChangeBannedStatus;
import tss.g2.fyre.models.actions.auth.GetUsers;
import tss.g2.fyre.models.actions.auth.check.AuthUser;
import tss.g2.fyre.models.datastorage.DataStorage;
import tss.g2.fyre.models.entity.Authorization;

/**
 * User controller.
 * @author Anton Kudryvtsev
 */
public class UserController implements CreateController {
  private Javalin app;
  private DataStorage dataStorage;
  private Map<String, Authorization> tokenStorage;
  private static Logger logger = LoggerFactory.getLogger(UserController.class);

  /**
   * Constructor.
   * @param app javalin app
   * @param dataStorage storage of data
   * @param tokenStorage storage with authorization info
   */
  public UserController(Javalin app, DataStorage dataStorage,
                        Map<String, Authorization> tokenStorage) {
    this.app = app;
    this.dataStorage = dataStorage;
    this.tokenStorage = tokenStorage;
  }

  @Override
  public void create() {
    app.post("/change/status", ctx -> {
      String token = ctx.sessionAttribute("token");
      String userLogin = ctx.formParam("userLogin");
      logger.info("Request to /change/status with user {} for {}",
          new UserLogin(tokenStorage, token).get(), userLogin);
      Action action = new AuthUser(
          new ChangeBannedStatus(dataStorage, userLogin),
          token,
          tokenStorage
      );
      Answer answer = new ActionTime("/change/status", action, dataStorage).getAnswer();
      ctx.result(answer.toJson());
    });

    app.post("/select/users", ctx -> {
      String token = ctx.sessionAttribute("token");
      logger.info("Request to /select/users with user {}",
          new UserLogin(tokenStorage, token).get());
      Action action = new AuthUser(
          new GetUsers(dataStorage),
          token,
          tokenStorage
      );
      Answer answer = new ActionTime("/select/users", action, dataStorage).getAnswer();
      ctx.result(answer.toJson());
    });
  }
}
