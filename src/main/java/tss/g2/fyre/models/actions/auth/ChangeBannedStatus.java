package tss.g2.fyre.models.actions.auth;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.actions.auth.Action;
import tss.g2.fyre.models.datastorage.DataStorage;

public class ChangeBannedStatus implements Action {

  private DataStorage dataStorage;
  private String userLogin;

  /**
   * Constructor.
   * @param dataStorage data storage object
   * @param userLogin user login
   */
  public ChangeBannedStatus(DataStorage dataStorage, String userLogin) {
    this.dataStorage = dataStorage;
    this.userLogin = userLogin;
  }

  @Override
  public Answer getAnswer(String user) {
    return new Answer<>(true, dataStorage.changeBannedStatus(userLogin));
  }
}