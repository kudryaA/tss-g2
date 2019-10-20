package tss.g2.fyre.controllers.javalin;

import io.javalin.Javalin;
import io.javalin.http.UploadedFile;

import java.text.SimpleDateFormat;
import java.util.Date;

import tss.g2.fyre.controllers.CreateController;
import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.actions.AddRecipe;
import tss.g2.fyre.models.actions.AddType;
import tss.g2.fyre.models.actions.DeleteRecipe;
import tss.g2.fyre.models.actions.GetTypes;
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

      String recipeName = ctx.formParam("recipeName");
      String recipeComposition = ctx.formParam("recipeComposition");
      String cookingSteps = ctx.formParam("cookingSteps");
      Date publicationDate = dateFormat.parse(ctx.formParam("publicationDate"));
      String selectedTypes = ctx.formParam("selectedTypes");
      UploadedFile image = ctx.uploadedFile("image");

      Answer answer = new AddRecipe(dataStorage, recipeName,
              recipeComposition, cookingSteps, publicationDate, selectedTypes, image).getAnswer();

      ctx.result(answer.toJson());
    });

    app.post("/add/type", ctx -> {
      String typeName = ctx.formParam("typeName");
      String description = ctx.formParam("description");

      Answer answer = new AddType(dataStorage, typeName, description).getAnswer();
      ctx.result(answer.toJson());
    });

    app.post("/delete/recipe", ctx -> {
      String recipeName = ctx.formParam("recipeName");

      Answer answer = new DeleteRecipe(dataStorage, recipeName).getAnswer();
      ctx.result(answer.toJson());
    });

    app.post("/select/types", ctx -> ctx.result(new GetTypes(dataStorage).getAnswer().toJson()));
  }
}
