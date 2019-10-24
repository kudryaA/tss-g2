package tss.g2.fyre.models.actions.auth;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.datastorage.DataStorage;
import tss.g2.fyre.utils.ToHash;

/**
 * Class for register moderator.
 */
public class RegisterModerator implements Action {
  private DataStorage dataStorage;
  private String login;
  private String password;
  private String name;

  /**
   * Constructor.
   *
   * @param dataStorage data storage object
   * @param login moderator login
   * @param password moderator password
   * @param name moderator name
   */
  public RegisterModerator(DataStorage dataStorage, String login, String password, String name) {
    this.dataStorage = dataStorage;
    this.login = login;
    this.password = new ToHash(password).getHash();
    this.name = name;
  }

  @Override
  public Answer getAnswer(String user) {
    return new Answer<>(true, dataStorage.createModerator(login, password, name));
  }
}
