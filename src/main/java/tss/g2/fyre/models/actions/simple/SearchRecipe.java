package tss.g2.fyre.models.actions.simple;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.actions.Action;
import tss.g2.fyre.models.datastorage.DataStorage;

public class SearchRecipe implements Action {
  private DataStorage dataStorage;
  private String ingredientName;
  private int pageNumber;
  private int pageSize;

  /**
   * Constructor.
   *
   * @param dataStorage data storage object
   * @param ingredientName ingredient name
   * @param pageNumber page number
   * @param pageSize page size
   */
  public SearchRecipe(DataStorage dataStorage, String ingredientName,
                      int pageNumber, int pageSize) {
    this.dataStorage = dataStorage;
    this.ingredientName = ingredientName;
    this.pageNumber = pageNumber;
    this.pageSize = pageSize;
  }

  @Override
  public Answer getAnswer() {
    return new Answer<>(true, dataStorage.searchRecipe(ingredientName, pageNumber, pageSize));
  }
}
