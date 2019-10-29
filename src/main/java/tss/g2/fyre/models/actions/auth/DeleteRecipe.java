package tss.g2.fyre.models.actions.auth;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.datastorage.DataStorage;

public class DeleteRecipe implements Action {

  private DataStorage dataStorage;
  private int recipeId;

  /**
   * Constructor.
   *
   * @param dataStorage data storage object
   * @param recipeId recipe id
   */
  public DeleteRecipe(DataStorage dataStorage, int recipeId) {
    this.dataStorage = dataStorage;
    this.recipeId = recipeId;
  }

  @Override
  public Answer getAnswer(String user) {
    return new Answer<>(true, dataStorage.deleteRecipe(recipeId, user));
  }
}
