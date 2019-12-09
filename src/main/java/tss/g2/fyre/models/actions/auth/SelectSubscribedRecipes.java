package tss.g2.fyre.models.actions.auth;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.datastorage.DataStorage;

public class SelectSubscribedRecipes implements ActionAuth {
  private DataStorage dataStorage;
  private int pageNumber;
  private int pageSize;

  /**
   * Constructor.
   * @param dataStorage data storage object
   * @param pageNumber page number
   * @param pageSize page size
   */
  public SelectSubscribedRecipes(DataStorage dataStorage, int pageNumber, int pageSize) {
    this.dataStorage = dataStorage;
    this.pageNumber = pageNumber;
    this.pageSize = pageSize;
  }

  @Override
  public Answer getAnswer(String login, String role) {
    return new Answer<>(true, dataStorage.selectSubscribedRecipes(login, pageNumber, pageSize));
  }
}
