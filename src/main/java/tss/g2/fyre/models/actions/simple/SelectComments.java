package tss.g2.fyre.models.actions.simple;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.actions.Action;
import tss.g2.fyre.models.datastorage.DataStorage;

public class SelectComments implements Action {
  private DataStorage dataStorage;
  private String recipeId;

  /**
   * Constructor.
   * @param dataStorage data storage object
   * @param recipeId recipe id
   */
  public SelectComments(DataStorage dataStorage, String recipeId) {
    this.dataStorage = dataStorage;
    this.recipeId = recipeId;
  }

  @Override
  public Answer getAnswer() {
    return new Answer<>(true, dataStorage.selectComments(recipeId));
  }
}
