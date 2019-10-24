package tss.g2.fyre.models.actions.simple;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.datastorage.DataStorage;

/**
 * Action for get recipe.
 */
public class GetRecipe  implements Action {

  private int recipeId;
  private DataStorage dataStorage;

  /**
   * Constructor.
   * @param dataStorage storage
   * @param recipeId id of recipe
   */
  public GetRecipe(DataStorage dataStorage, int recipeId) {
    this.recipeId = recipeId;
    this.dataStorage = dataStorage;
  }

  @Override
  public Answer getAnswer() {
    return new Answer<>(true, dataStorage.getRecipe(recipeId));
  }
}
