package tss.g2.fyre.controllers.javalin;

import io.javalin.Javalin;
import tss.g2.fyre.controllers.CreateController;
import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.datastorage.DataStorage;

/**
 * This interface initialize general javalin controller.
 *
 * @author Anton Kudryavtsev
 */
public class JavalinController implements CreateController {

  private Javalin app;
  private DataStorage dataStorage;

  /**
   * Constructor.
   *
   * @param app javalin object
   *
   */
  public JavalinController(Javalin app, DataStorage dataStorage) {
    this.app = app;
    this.dataStorage = dataStorage;
  }

  @Override
  public void create() {
    app.get("/test", ctx -> ctx.result(new Answer<>(true).toJson()));
    new AuthorizationController(app, dataStorage).create();
    new RecipeController(app, dataStorage).create();
  }
}
