package tss.g2.fyre.controllers.javalin;

import io.javalin.Javalin;
import io.javalin.http.UploadedFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tss.g2.fyre.controllers.CreateController;
import tss.g2.fyre.controllers.utils.UserLogin;
import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.actions.Action;
import tss.g2.fyre.models.actions.ActionTime;

import tss.g2.fyre.models.actions.auth.*;

import tss.g2.fyre.models.actions.auth.check.AuthUser;
import tss.g2.fyre.models.actions.simple.*;
import tss.g2.fyre.models.datastorage.DataStorage;
import tss.g2.fyre.models.entity.Authorization;
import tss.g2.fyre.utils.DateConverter;

/**
 * Recipe controller for javalin.
 * @author Andrey Sherstyuk
 */
public class RecipeController implements CreateController {

  private Javalin app;
  private DataStorage dataStorage;
  private Map<String, Authorization> tokenStorage;
  private static Logger logger = LoggerFactory.getLogger(RecipeController.class);

  /**
   * Constructor.
   * @param app javalin app
   * @param tokenStorage storage with authorization info
   */
  RecipeController(Javalin app, DataStorage dataStorage,
                   Map<String, Authorization> tokenStorage) {
    this.app = app;
    this.dataStorage = dataStorage;
    this.tokenStorage = tokenStorage;
  }

  @Override
  public void create() {
    app.post("/add/recipe", ctx -> {
      String token = ctx.sessionAttribute("token");
      logger.info("Request to /add/recipe with user {}", new UserLogin(tokenStorage, token).get());
      String recipeName = ctx.formParam("recipeName").trim();
      String recipeComposition = ctx.formParam("recipeComposition").trim();
      String cookingSteps = ctx.formParam("cookingSteps").trim();
      Date publicationDate = new DateConverter(ctx.formParam("publicationDate")).date();
      String selectedTypes = ctx.formParam("selectedTypes");
      UploadedFile image = ctx.uploadedFile("image");
      Action action = new AuthUser(
              new AddRecipe(dataStorage, recipeName,
                      recipeComposition, cookingSteps, publicationDate, selectedTypes, image),
              token,
              tokenStorage
      );
      Answer answer = new ActionTime("/add/recipe", action, dataStorage).getAnswer();
      ctx.result(answer.toJson());
    });

    app.post("/add/type", ctx -> {
      String token = ctx.sessionAttribute("token");
      String typeName = ctx.formParam("typeName").trim();
      String description = ctx.formParam("description").trim();
      UploadedFile image = ctx.uploadedFile("image");
      logger.info("Request to /add/type with user {} for type {}",
              new UserLogin(tokenStorage, token).get(), typeName);
      Action action = new AuthUser(
              new AddType(dataStorage, typeName, description, image),
              token,
              tokenStorage
      );
      Answer answer = new ActionTime("/add/type", action, dataStorage).getAnswer();
      ctx.result(answer.toJson());
    });

    app.post("/delete/recipe", ctx -> {
      String token = ctx.sessionAttribute("token");
      String recipeId = ctx.formParam("recipeId");
      logger.info("Request to /delete/recipe with user {} for recipe {}",
              new UserLogin(tokenStorage, token).get(), recipeId);
      Action action = new AuthUser(
              new DeleteRecipe(dataStorage, recipeId),
              token,
              tokenStorage
      );
      Answer answer = new ActionTime("/delete/recipe", action, dataStorage).getAnswer();
      ctx.result(answer.toJson());
    });

    app.post("/recipe", ctx -> {
      System.out.println(ctx.cookieMap());
      String token = ctx.sessionAttribute("token");
      String recipeId = ctx.formParam("recipeId");
      logger.info("Request to /recipe with user {} for recipe {}",
              new UserLogin(tokenStorage, token).get(), recipeId);
      Action action = new GetRecipe(dataStorage, recipeId);
      Answer answer = new ActionTime("/recipe", action, dataStorage).getAnswer();
      ctx.result(answer.toJson());
    });

    app.post("/select/types", ctx -> {
      String token = ctx.sessionAttribute("token");
      logger.info("Request to /select/types with user {}",
              new UserLogin(tokenStorage, token).get());
      Action action = new GetTypes(dataStorage);
      Answer answer = new ActionTime("/select/types", action, dataStorage).getAnswer();
      ctx.result(answer.toJson());
    });

    app.post("/select/recipes", ctx -> {
      String token = ctx.sessionAttribute("token");
      logger.info("Request to /select/recipes with user {}",
              new UserLogin(tokenStorage, token).get());
      int pageNumber = Integer.parseInt(ctx.formParam("pageNumber"));
      int pageSize = Integer.parseInt(ctx.formParam("pageSize"));
      String recipeType = ctx.formParam("recipeType");
      String sortType = ctx.formParam("sortType");
      Action action = new SelectRecipes(dataStorage, pageNumber, pageSize, recipeType, sortType);
      Answer answer = new ActionTime("/select/recipes", action, dataStorage).getAnswer();
      ctx.result(answer.toJson());
    });

    app.get("/image", ctx  -> {
      String token = ctx.sessionAttribute("token");
      logger.info("Request to /image with user {}", new UserLogin(tokenStorage, token).get());
      String id = ctx.queryParam("id");
      ctx.result(new FileInputStream(new File("images/" + id)));
    });

    app.post("/update/recipe", ctx -> {
      String token = ctx.sessionAttribute("token");
      String recipeId = ctx.formParam("recipeId");
      String recipeName = ctx.formParam("recipeName").trim();
      String composition = ctx.formParam("composition").trim();
      String cookingSteps = ctx.formParam("cookingSteps").trim();
      logger.info("Request to /update/recipe with user {} for recipe {}",
              new UserLogin(tokenStorage, token).get(), recipeId);
      Action action = new AuthUser(
              new UpdateRecipe(dataStorage, recipeId, recipeName, composition, cookingSteps),
              token,
              tokenStorage
      );
      Answer answer = new ActionTime("/update/recipe", action, dataStorage).getAnswer();
      ctx.result(answer.toJson());
    });

    app.post("/search/recipe", ctx -> {
      String token = ctx.sessionAttribute("token");
      String ingredientName = ctx.formParam("ingredientName");
      int pageNumber = Integer.parseInt(ctx.formParam("pageNumber"));
      int pageSize = Integer.parseInt(ctx.formParam("pageSize"));
      logger.info("Request to /search/recipe with user {} with ingredient {}",
              new UserLogin(tokenStorage, token).get(), ingredientName);
      Action action = new SearchRecipe(dataStorage, ingredientName, pageNumber, pageSize);
      Answer answer = new ActionTime("/search/recipe", action, dataStorage).getAnswer();
      ctx.result(answer.toJson());
    });

    app.post("/select/unconfirmedRecipes", ctx -> {
      String token = ctx.sessionAttribute("token");
      logger.info("Request to /select/unconfirmedRecipes with user {}",
              new UserLogin(tokenStorage, token).get());
      Action action = new AuthUser(
              new SelectUnconfirmedRecipes(dataStorage),
              token,
              tokenStorage
      );
      Answer answer = new ActionTime("/select/unconfirmedRecipes", action, dataStorage).getAnswer();
      ctx.result(answer.toJson());
    });

    app.post("/recipeConfirmation", ctx -> {
      String token = ctx.sessionAttribute("token");
      String recipeId = ctx.formParam("recipeId");
      logger.info("Request to /recipeConfirmation with user {} for recipe {}",
              new UserLogin(tokenStorage, token).get(), recipeId);
      Action action = new AuthUser(
              new RecipeConfirmation(dataStorage, recipeId),
              token,
              tokenStorage
      );
      Answer answer = new ActionTime("/recipeConfirmation", action, dataStorage).getAnswer();
      ctx.result(answer.toJson());
    });

    app.post("/add/comment", ctx -> {
      String token = ctx.sessionAttribute("token");
      String recipeId = ctx.formParam("recipeId");
      logger.info("Request to /add/comment with user {} for recipe {}",
              new UserLogin(tokenStorage, token).get(), recipeId);
      String commentText = ctx.formParam("commentText").trim();
      Action action = new AuthUser(
              new AddComment(dataStorage, recipeId, commentText),
              token,
              tokenStorage
      );
      Answer answer = new ActionTime("/add/comment", action, dataStorage).getAnswer();
      ctx.result(answer.toJson());
    });

    app.post("/select/comments", ctx -> {
      String token = ctx.sessionAttribute("token");
      String recipeId = ctx.formParam("recipeId");
      logger.info("Request to /select/comments with user {} for recipe {}",
              new UserLogin(tokenStorage, token).get(), recipeId);
      Action action = new SelectComments(dataStorage, recipeId);
      Answer answer = new ActionTime("/select/comments", action, dataStorage).getAnswer();
      ctx.result(answer.toJson());
    });


    app.post("/select/subscribedRecipes", ctx -> {
      String token = ctx.sessionAttribute("token");
      int pageNumber = Integer.parseInt(ctx.formParam("pageNumber"));
      int pageSize = Integer.parseInt(ctx.formParam("pageSize"));
      logger.info("Request to /select/subscribedRecipes for user {}",
              new UserLogin(tokenStorage, token).get());
      Action action = new AuthUser(
              new SelectSubscribedRecipes(dataStorage, pageNumber, pageSize),
              token,
              tokenStorage
      );
      Answer answer = new ActionTime("/add/subscribedRecipes", action, dataStorage).getAnswer();
      ctx.result(answer.toJson());
    });

    app.post("/add/like", ctx -> {
      String token = ctx.sessionAttribute("token");
      String recipeId = ctx.formParam("recipeId");
      logger.info("Request to /add/like with user {} for recipe {}",
              new UserLogin(tokenStorage, token).get(), recipeId);
      Action action = new AuthUser(
              new AddLike(dataStorage, recipeId),
              token,
              tokenStorage
      );
      Answer answer = new ActionTime("/add/like", action, dataStorage).getAnswer();
      ctx.result(answer.toJson());
    });

    app.post("/check/like", ctx -> {
      String token = ctx.sessionAttribute("token");
      String recipeId = ctx.formParam("recipeId");
      logger.info("Request to /check/like with user {} for recipe {}",
              new UserLogin(tokenStorage, token).get(), recipeId);
      Action action = new AuthUser(
              new CheckLike(dataStorage, recipeId),
              token,
              tokenStorage
      );
      Answer answer = new ActionTime("/check/like", action, dataStorage).getAnswer();
      ctx.result(answer.toJson());
    });

    app.get("/getRecipeFromApi", ctx -> {
      String recipeId = ctx.queryParam("recipeId");
      String key = ctx.queryParam("key");
      logger.info("Request to /getRecipeFromApi for recipe {} with key",
              recipeId, key);
      Action action = new GetRecipeFromApi(dataStorage, recipeId, key);
      Answer answer = new ActionTime("/getRecipeFromApi", action, dataStorage).getAnswer();
      ctx.result(answer.toJson());
    });

    app.post("/select/userRecipes", ctx -> {
      String token = ctx.sessionAttribute("token");
      logger.info("Request to /select/userRecipes with user {}",
              new UserLogin(tokenStorage, token).get());
      Action action = new AuthUser(
              new SelectUserRecipes(dataStorage),
              token,
              tokenStorage
      );
      Answer answer = new ActionTime("/select/userRecipes", action, dataStorage).getAnswer();
      ctx.result(answer.toJson());
    });
  }
}
