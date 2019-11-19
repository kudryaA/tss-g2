package tss.g2.fyre.models.actions.simple;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.actions.Action;
import tss.g2.fyre.models.datastorage.DataStorage;

/**
 * Action class for select recipes by recipe type with sorting.
 */
public class SelectRecipes implements Action {
  private DataStorage dataStorage;
  private int pageNumber;
  private int pageSize;
  private String recipeType;
  private String sortType;

  /**
   * Constructor.
   *
   * @param dataStorage data storage object
   * @param pageNumber page number
   * @param pageSize page size
   * @param recipeType recipe type
   * @param sortType sort type
   */
  public SelectRecipes(DataStorage dataStorage, int pageNumber, int pageSize,
                       String recipeType, String sortType) {
    this.dataStorage = dataStorage;
    this.pageNumber = pageNumber;
    this.pageSize = pageSize;
    this.recipeType = recipeType;
    this.sortType = sortType;
  }

  @Override
  public Answer getAnswer() {
    return new Answer<>(true,
                        dataStorage.selectRecipes(pageNumber, pageSize, recipeType, sortType));
  }
}
