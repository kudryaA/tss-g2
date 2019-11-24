package tss.g2.fyre.models.actions.simple;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.AnswerWithComment;
import tss.g2.fyre.models.actions.Action;
import tss.g2.fyre.models.datastorage.DataStorage;
import tss.g2.fyre.models.entity.Person;
import tss.g2.fyre.utils.ToHash;

/**
 * Action class for check authorization.
 */
public class CheckAuthorization implements Action {
  private DataStorage dataStorage;
  private String login;
  private String password;

  /**
   * Constructor.
   *
   * @param dataStorage data storage object
   * @param login user login
   * @param password user password
   */
  public CheckAuthorization(DataStorage dataStorage, String login, String password) {
    this.dataStorage = dataStorage;
    this.login = login;
    if (password == null) {
      this.password = null;
    } else {
      this.password = new ToHash(password).getHash();
    }
  }


  @Override
  public Answer getAnswer() {
    Person authorization = dataStorage.getAuthorization(login);

    if (authorization == null) {
      return new Answer<>(false, false);
    }

    if (!login.matches("\\w{" + login.length() + "}")) {
      return new AnswerWithComment(true, false, "Login must contain only [a-z0-9_-].");
    }

    if (authorization.getBannedStatus()) {
      return new AnswerWithComment(true, false, "You are banned");
    }

    if (authorization.getLogin().equals(login) && authorization.getPassword().equals(password)) {
      return new Answer<>(true, true);
    } else {
      return new AnswerWithComment(true, false, "You entered incorrect login/password");
    }
  }
}
