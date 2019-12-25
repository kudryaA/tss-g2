package tss.g2.fyre.models.actions.auth;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.AnswerWithComment;
import tss.g2.fyre.models.datastorage.DataStorage;

/**
 * Action class for update recipe.
 */
public class UpdateRecipe implements ActionAuth {
  private DataStorage dataStorage;
  private String recipeId;
  private String recipeName;
  private String composition;
  private String cookingSteps;

  /**
   * Constructor.
   *
   * @param dataStorage data storage object
   * @param recipeId recipe id
   * @param recipeName recipe name
   * @param composition composition
   * @param cookingSteps cooking steps
   */
  public UpdateRecipe(DataStorage dataStorage, String recipeId, String recipeName,
                      String composition, String cookingSteps) {
    this.dataStorage = dataStorage;
    this.recipeId = recipeId;
    this.recipeName = recipeName.replace("<", "&lt");
    this.composition = composition.replace("<", "&lt");
    this.cookingSteps = cookingSteps.replaceAll("<(?i)img", "&ltimg")
            .replaceAll("<(?i)script", "&ltscript")
            .replaceAll("<(?i)meta", "&ltmeta")
            .replaceAll("<(?i)style", "&ltstyle")
            .replace("=", "&#61;");
  }

  @Override
  public Answer getAnswer(String login, String role) {

    if ("".equals(recipeName)) {
      return new AnswerWithComment(true, false,
              "The name of the recipe field must not be empty.");
    }
    if ("".equals(composition)) {
      return new AnswerWithComment(true, false,
              "The composition of the recipe field must not be empty.");
    }
    if ("".equals(cookingSteps)) {
      return new AnswerWithComment(true, false,
              "The cooking steps of the recipe field must not be empty.");
    }

    if (recipeName.matches("[а-яА-ЯЄєІіїЇҐґ]+")) {
      return new AnswerWithComment(true, false,
              "The recipe name should contain only English words.");
    }
    if (composition.matches("[а-яА-ЯЄєІіїЇҐґ]+")) {
      return new AnswerWithComment(true, false,
              "The recipe composition should contain only English words.");
    }
    if (cookingSteps.matches("[а-яА-ЯЄєІіїЇҐґ]+")) {
      return new AnswerWithComment(true, false,
              "The cooking steps should contain only English words.");
    }

    return new Answer<>(true, dataStorage
        .updateRecipe(recipeId, recipeName, composition, cookingSteps, login));
  }
}
