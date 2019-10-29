package tss.g2.fyre.models.actions.simple;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.datastorage.DataStorage;

public class SearchRecipe implements Action {
  private DataStorage dataStorage;
  private String ingredientName;

  /**
   * Constructor.
   *
   * @param dataStorage data storage object
   * @param ingredientName ingredient name
   */
  public SearchRecipe(DataStorage dataStorage, String ingredientName) {
    this.dataStorage = dataStorage;
    this.ingredientName = ingredientName;
  }

  @Override
  public Answer getAnswer() {
    return new Answer<>(true, dataStorage.searchRecipe(ingredientName));
  }
}
