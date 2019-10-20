package tss.g2.fyre.models.actions;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.datastorage.DataStorage;

public class GetUsers implements Action {
  private DataStorage dataStorage;

  /**
   * Constructor.
   *
   * @param dataStorage data storage object
   */
  public GetUsers(DataStorage dataStorage) {
    this.dataStorage = dataStorage;
  }

  @Override
  public Answer getAnswer() {
    return new Answer<>(true, dataStorage.getPersonsInformation());
  }
}
