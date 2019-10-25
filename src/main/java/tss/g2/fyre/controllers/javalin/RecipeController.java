package tss.g2.fyre.controllers.javalin;

import io.javalin.Javalin;
import io.javalin.http.UploadedFile;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import tss.g2.fyre.controllers.CreateController;
import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.actions.auth.AddRecipe;
import tss.g2.fyre.models.actions.auth.AddType;
import tss.g2.fyre.models.actions.auth.DeleteRecipe;
import tss.g2.fyre.models.actions.auth.check.AuthUser;
import tss.g2.fyre.models.actions.simple.GetRecipe;
import tss.g2.fyre.models.actions.simple.GetTypes;
import tss.g2.fyre.models.actions.simple.SelectRecipes;
import tss.g2.fyre.models.datastorage.DataStorage;
import tss.g2.fyre.models.entity.Authorization;

/**
 * Recipe controller for javalin.
 * @author Andrey Sherstyuk
 */
public class RecipeController implements CreateController {

  private Javalin app;
  private DataStorage dataStorage;
  private Map<String, Authorization> tokenStorage;

  /**
   * Constructor.
   * @param app javalin app
   * @param tokenStorage storage with authorization info
   */
  public RecipeController(Javalin app, DataStorage dataStorage,
                                 Map<String, Authorization> tokenStorage) {
    this.app = app;
    this.dataStorage = dataStorage;
    this.tokenStorage = tokenStorage;
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
      Answer answer = new AuthUser(
          new AddRecipe(dataStorage, recipeName,
              recipeComposition, cookingSteps, publicationDate, selectedTypes, image),
          ctx.sessionAttribute("token"),
          tokenStorage
      ).getAnswer();
      ctx.result(answer.toJson());
    });

    app.post("/add/type", ctx -> {
      String typeName = ctx.formParam("typeName");
      String description = ctx.formParam("description");
      Answer answer = new AuthUser(
          new AddType(dataStorage, typeName, description),
          ctx.sessionAttribute("token"),
          tokenStorage
      ).getAnswer();
      ctx.result(answer.toJson());
    });

    app.post("/delete/recipe", ctx -> {
      int recipeId = Integer.parseInt(ctx.formParam("recipeId"));
      Answer answer = new AuthUser(
          new DeleteRecipe(dataStorage, recipeId),
          ctx.sessionAttribute("token"),
          tokenStorage
      ).getAnswer();
      ctx.result(answer.toJson());
    });

    app.post("/recipe", ctx -> {
      int recipeId = Integer.parseInt(ctx.formParam("recipeId"));
      Answer answer = new GetRecipe(dataStorage, recipeId).getAnswer();
      ctx.result(answer.toJson());
    });

    app.post("/select/types", ctx -> ctx.result(new GetTypes(dataStorage).getAnswer().toJson()));

    app.post("/select/recipes", ctx -> {
      int pageNumber = Integer.parseInt(ctx.formParam("pageNumber"));
      int pageSize = Integer.parseInt(ctx.formParam("pageSize"));
      String recipeType = ctx.formParam("recipeType");
      String sortType = ctx.formParam("sortType");

      ctx.result(new SelectRecipes(dataStorage, pageNumber, pageSize, recipeType, sortType)
                  .getAnswer().toJson());
    });

    app.get("/image", ctx  -> {
      String id = ctx.queryParam("id");
      ctx.result(new FileInputStream(new File("images/" + id)));
    });

  }
}
