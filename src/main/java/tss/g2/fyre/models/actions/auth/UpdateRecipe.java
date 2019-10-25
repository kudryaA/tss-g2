package tss.g2.fyre.models.actions.auth;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.datastorage.DataStorage;

public class UpdateRecipe implements Action {
  private DataStorage dataStorage;
  private int recipeId;
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
  public UpdateRecipe(DataStorage dataStorage, int recipeId, String recipeName,
                      String composition, String cookingSteps) {
    this.dataStorage = dataStorage;
    this.recipeId = recipeId;
    this.recipeName = recipeName;
    this.composition = composition;
    this.cookingSteps = cookingSteps;
  }

  @Override
  public Answer getAnswer(String login) {
    return new Answer<>(true, dataStorage
        .updateRecipe(recipeId, recipeName, composition, cookingSteps, login));
  }
}
