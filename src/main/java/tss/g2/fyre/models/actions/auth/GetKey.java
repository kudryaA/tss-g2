package tss.g2.fyre.models.actions.auth;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.datastorage.DataStorage;

public class GetKey implements ActionAuth {
  private DataStorage dataStorage;

  /**
   * Constructor.
   * @param dataStorage DataStorage object
   */
  public GetKey(DataStorage dataStorage) {
    this.dataStorage = dataStorage;
  }

  @Override
  public Answer getAnswer(String login, String role) {
    return new Answer<>(true, dataStorage.getKey(login));
  }
}
