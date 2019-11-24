package tss.g2.fyre.controllers.javalin;

import com.google.gson.Gson;
import io.javalin.Javalin;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tss.g2.fyre.controllers.CreateController;
import tss.g2.fyre.controllers.utils.UserLogin;
import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.actions.Action;
import tss.g2.fyre.models.actions.ActionTime;
import tss.g2.fyre.models.actions.simple.service.StoreTime;
import tss.g2.fyre.models.datastorage.DataStorage;
import tss.g2.fyre.models.entity.Authorization;

/**
 * Service controller.
 * @author Anton Kudryvtsev
 */
public class ServiceController implements CreateController {
  private Javalin app;
  private DataStorage dataStorage;
  private Map<String, Authorization> tokenStorage;
  private static Logger logger = LoggerFactory.getLogger(ServiceController.class);

  /**
   * Constructor.
   * @param app javalin app
   * @param dataStorage storage of data
   * @param tokenStorage storage with authorization info
   */
  public ServiceController(Javalin app, DataStorage dataStorage,
                           Map<String, Authorization> tokenStorage) {
    this.app = app;
    this.dataStorage = dataStorage;
    this.tokenStorage = tokenStorage;
  }

  @Override
  public void create() {
    app.post("/store/time/interactive", ctx -> {
      String token = ctx.sessionAttribute("token");
      long time = Long.parseLong(ctx.formParam("userLogin"));
      String page = ctx.formParam("page");
      logger.info("Request to /store/time/interactive with user {}",
          new UserLogin(tokenStorage, token).get());
      Action action = new StoreTime(dataStorage, page, time);
      Answer answer = new ActionTime("/store/time/interactive", action, dataStorage).getAnswer();
      ctx.result(answer.toJson());
    });

    app.post("/select/statistics", ctx -> {
      ctx.result(new Gson().toJson(dataStorage.selectStatistics()));
    });
  }
}
