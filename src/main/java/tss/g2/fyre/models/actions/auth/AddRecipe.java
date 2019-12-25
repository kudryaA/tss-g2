package tss.g2.fyre.models.actions.auth;

import io.javalin.http.UploadedFile;

import java.util.*;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.AnswerWithComment;
import tss.g2.fyre.models.actions.SendEmailThread;
import tss.g2.fyre.models.datastorage.DataStorage;
import tss.g2.fyre.models.entity.Roles;
import tss.g2.fyre.utils.StoreImage;

/**
 * Action class for add recipe to database.
 */
public class AddRecipe implements ActionAuth {
  private DataStorage dataStorage;
  private String recipeName;
  private String recipeComposition;
  private String cookingSteps;
  private Date publicationDate;
  private String selectedTypes;
  private UploadedFile image;

  /**
   * Constructor.
   *
   * @param dataStorage data storage object
   * @param recipeName recipe name
   * @param recipeComposition composition of the recipe
   * @param cookingSteps recipe cooking steps
   * @param publicationDate recipe publication date
   * @param selectedTypes types that the moderator selects
   * @param image image of recipe
   */
  public AddRecipe(DataStorage dataStorage, String recipeName, String recipeComposition,
                   String cookingSteps, Date publicationDate,
                   String selectedTypes, UploadedFile image) {
    this.dataStorage = dataStorage;
    this.recipeName = recipeName.replace("<", "&lt");
    this.recipeComposition = recipeComposition.replace("<", "&lt");
    this.cookingSteps = cookingSteps.replaceAll("<(?i)img", "&ltimg")
            .replaceAll("<(?i)script", "&ltscript")
            .replaceAll("<(?i)meta", "&ltmeta")
            .replaceAll("<(?i)style", "&ltstyle")
            .replace("=", "&#61;");
    this.publicationDate = publicationDate;
    this.selectedTypes = selectedTypes;
    this.image = image;
  }

  @Override
  public Answer getAnswer(String user, String role) {
    List<String> typesList = new ArrayList<>(Arrays.asList(selectedTypes.split("/")));

    if ("".equals(recipeName)) {
      return new AnswerWithComment(true, false,
            "The name of the recipe field must not be empty.");
    }
    if ("".equals(recipeComposition)) {
      return new AnswerWithComment(true, false,
              "The composition of the recipe field must not be empty.");
    }
    if ("".equals(cookingSteps)) {
      return new AnswerWithComment(true, false,
              "The cooking steps of the recipe field must not be empty.");
    }
    if (publicationDate == null) {
      return new AnswerWithComment(true, false,
              "The publication date field must not be empty.");
    }
    if ("".equals(selectedTypes)) {
      return new AnswerWithComment(true, false,
              "You must select one or more types for the recipe.");
    }
    if (image == null) {
      return new AnswerWithComment(true, false,
              "You must add an image for the recipe.");
    }

    if (recipeName.matches("[а-яА-ЯЄєІіїЇҐґ]+")) {
      return new AnswerWithComment(true, false,
              "The recipe name should contain only English words.");
    }
    if (recipeComposition.matches("[а-яА-ЯЄєІіїЇҐґ]+")) {
      return new AnswerWithComment(true, false,
              "The recipe composition should contain only English words.");
    }
    if (cookingSteps.matches("[а-яА-ЯЄєІіїЇҐґ]+")) {
      return new AnswerWithComment(true, false,
              "The cooking steps should contain only English words.");
    }

    String image = new StoreImage(this.image).store();

    try {
      String recipeId = dataStorage.addRecipe(recipeName, recipeComposition, cookingSteps,
              publicationDate, typesList, image, user, !Roles.user.toString().equals(role));
      if (!"".equals(recipeId) && !Roles.user.toString().equals(role)) {
        Thread thread = new Thread(new SendEmailThread(dataStorage, user, recipeId, recipeName));
        thread.start();
      }
      return new Answer<>(true, recipeId);
    } catch (Exception e) {
      return new Answer(false);
    }
  }
}
