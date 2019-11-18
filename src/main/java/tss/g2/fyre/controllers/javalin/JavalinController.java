package tss.g2.fyre.controllers.javalin;

import io.javalin.Javalin;

import java.util.HashMap;
import java.util.Map;
import tss.g2.fyre.controllers.CreateController;
import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.actions.Action;
import tss.g2.fyre.models.actions.ActionTime;
import tss.g2.fyre.models.actions.simple.GetRecipe;
import tss.g2.fyre.models.datastorage.DataStorage;
import tss.g2.fyre.models.entity.Authorization;
import tss.g2.fyre.models.entity.recipe.Recipe;

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
    app.get("/recipe", ctx -> {
      String recipeId = ctx.formParam("recipeId");
      if (recipeId == null) {
        recipeId = ctx.queryParam("recipeId");
      }
      Action action = new GetRecipe(dataStorage, recipeId);
      Answer answer = new ActionTime("/recipe", action, dataStorage).getAnswer();
      Recipe recipe = (Recipe) answer.getObj();
      Map<String, String> model = new HashMap<>();
      model.put("title", recipe.getName());
      model.put("description", recipe.getComposition());
      model.put("image", "http://g2.sumdu-tss.site/image?id=" + recipe.getImage());
      model.put("url", "http://g2.sumdu-tss.site/mainPage.html?recipeId=" + recipeId);
      model.put("refresh", "0;url=http://g2.sumdu-tss.site/mainPage.html?recipeId=" + recipeId);
      ctx.render("pages/recipe.html", model);
    });
    new AuthorizationController(app, dataStorage, tokenStorage).create();
    new RecipeController(app, dataStorage, tokenStorage).create();
    new UserController(app, dataStorage, tokenStorage).create();
    new ServiceController(app, dataStorage, tokenStorage).create();
  }
}
