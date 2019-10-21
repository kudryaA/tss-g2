package tss.g2.fyre.controllers.javalin;

import io.javalin.Javalin;
import java.util.Map;
import tss.g2.fyre.controllers.CreateController;
import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.datastorage.DataStorage;
import tss.g2.fyre.models.entity.Authorization;


/**
 * This interface initialize general javalin controller.
 *
 * @author Anton Kudryavtsev
 */
public class JavalinController implements CreateController {

  private Javalin app;
  private DataStorage dataStorage;
  private Map<String, Authorization> tokenStorage;

  /**
   * Constructor.
   *
   * @param app javalin object
   * @param dataStorage storage of data
   * @param tokenStorage storage with token
   */
  public JavalinController(Javalin app, DataStorage dataStorage,
                           Map<String, Authorization> tokenStorage) {
    this.app = app;
    this.dataStorage = dataStorage;
    this.tokenStorage = tokenStorage;
  }

  @Override
  public void create() {
    app.get("/test", ctx -> ctx.result(new Answer<>(true).toJson()));
    app.get("/", ctx -> ctx.render("pages/mainPage.html"));
    new AuthorizationController(app, dataStorage, tokenStorage).create();
    new RecipeController(app, dataStorage, tokenStorage).create();
  }
}
