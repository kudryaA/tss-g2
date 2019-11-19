package tss.g2.fyre.models.actions.simple;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.AnswerWithComment;
import tss.g2.fyre.models.actions.Action;
import tss.g2.fyre.models.datastorage.DataStorage;
import tss.g2.fyre.utils.ToHash;

/**
 * Action class for check user registration.
 */
public class RegisterUser implements Action {
  private DataStorage dataStorage;
  private String login;
  private String password;
  private String name;
  private String surname;
  private String email;

  /**
   * Constructor.
   *
   * @param dataStorage data storage object
   * @param login user login
   * @param password user password
   * @param name user name
   * @param surname user surname
   * @param email user email
   */
  public RegisterUser(DataStorage dataStorage, String login, String password,
                      String name, String surname, String email) {
    this.dataStorage = dataStorage;
    this.login = login;
    this.password = new ToHash(password).getHash();
    this.name = name;
    this.surname = surname;
    this.email = email;
  }

  @Override
  public Answer getAnswer() {
    if (!login.matches("\\w{" + login.length() + "}")) {
      return new AnswerWithComment(true, false, "Login must contain only [a-z0-9_-].");
    }
    if (!name.matches("\\w{" + name.length() + "}")) {
      return new AnswerWithComment(true, false, "Name must contain only [a-z0-9_-].");
    }
    if (!surname.matches("\\w{" + surname.length() + "}")) {
      return new AnswerWithComment(true, false, "Name must contain only [a-z0-9_-].");
    }

    return new Answer<>(true,
            dataStorage.createUser(login, password, name, surname, email));
  }
}
