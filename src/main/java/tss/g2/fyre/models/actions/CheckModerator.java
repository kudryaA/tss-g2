package tss.g2.fyre.models.actions;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.datastorage.DataStorage;
import tss.g2.fyre.models.entity.Moderator;
import tss.g2.fyre.utils.ToHash;

/**
 * Class for check moderator authorization.
 * @author Anton Kudryavtsev
 */
public class CheckModerator implements Action {

  private DataStorage dataStorage;
  private String login;
  private String password;


  /**
   * Constructor.
   * @param dataStorage storage of data
   * @param login login of moderator
   * @param password password
   */
  public CheckModerator(DataStorage dataStorage, String login, String password) {
    this.dataStorage = dataStorage;
    this.login = login;
    this.password = new ToHash(password).getHash();
  }

  @Override
  public Answer getAnswer() {
    Moderator authorization = dataStorage.getModerator(login);
    boolean res = false;
    if (authorization != null && authorization.getLogin().equals(login)
        && authorization.getPassword().equals(password)) {
      res = true;
    }
    return new Answer<>(true, res);
  }
}
