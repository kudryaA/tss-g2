package tss.g2.fyre.controllers.javalin;

import io.javalin.Javalin;
import java.util.Map;

import tss.g2.fyre.controllers.CreateController;
import tss.g2.fyre.models.Answer;
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
  private static final int tokenSize = 50;

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
      String userLogin = ctx.formParam("userLogin");
      Answer answer = new AuthUser(
          new ChangeBannedStatus(dataStorage, userLogin),
          ctx.sessionAttribute("token"),
          tokenStorage
      ).getAnswer();
      ctx.result(answer.toJson());
    });

    app.post("/select/users", ctx -> {
      Answer answer = new AuthUser(
          new GetUsers(dataStorage),
          ctx.sessionAttribute("token"),
          tokenStorage
      ).getAnswer();
      ctx.result(answer.toJson());
    });
  }
}
