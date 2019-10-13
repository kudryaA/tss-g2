package tss.g2.fyre.controllers.javalin;

import io.javalin.Javalin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import tss.g2.fyre.controllers.CreateController;
import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.actions.AddRecipe;
import tss.g2.fyre.models.datastorage.DataStorage;

/**
 * Recipe controller for javalin.
 * @author Andrey Sherstyuk
 */
public class RecipeController implements CreateController {

  private Javalin app;
  private DataStorage dataStorage;

  /**
   * Constructor.
   * @param app javalin app
   * @param dataStorage storage of data
   */
  public RecipeController(Javalin app, DataStorage dataStorage) {
    this.app = app;
    this.dataStorage = dataStorage;
  }

  @Override
  public void create() {
    app.post("/add/recipe", ctx -> {
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

      String recipeName = ctx.queryParam("recipeName");
      String cookingSteps = ctx.queryParam("cookingSteps");
      Date publicationDate = dateFormat.parse(ctx.queryParam("publicationDate"));
      int rating = Integer.parseInt(Objects.requireNonNull(ctx.queryParam("rating")));

      Answer answer = new AddRecipe(dataStorage, recipeName, cookingSteps, publicationDate, rating)
                .getAnswer();
      ctx.result(answer.toJson());
    });
  }
}
