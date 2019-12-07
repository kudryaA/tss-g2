package tss.g2.fyre.models.actions.auth;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.datastorage.DataStorage;
import tss.g2.fyre.utils.ToHash;

public class ChangePassword implements ActionAuth {
  private DataStorage dataStorage;
  private String password;

  /**
   * Constructor.
   * @param dataStorage data storage object
   * @param password new password
   */
  public ChangePassword(DataStorage dataStorage, String password) {
    this.dataStorage = dataStorage;
    this.password = new ToHash(password).getHash();
  }

  @Override
  public Answer getAnswer(String login, String role) {
    return new Answer<>(true, dataStorage.changePassword(password, login));
  }
}
