package tss.g2.fyre.models.entity;

import java.util.Objects;

/**
 * Class authorization.
 *
 * @author Anton Kudryavtsev
 */
public class Authorization {
  private String login;
  private String password;

  /**
   * Constructor.
   *
   * @param login login of user
   * @param password password of user
   */
  public Authorization(String login, String password) {
    this.login = login;
    this.password = password;
  }

  /**
   * Method for get login of user.
   * @return login of user
   */
  public String getLogin() {
    return login;
  }

  /**
   * Method for get password of user.
   * @return password of user
   */
  public String getPassword() {
    return password;
  }


  @Override
  public int hashCode() {
    return Objects.hash(login, password);
  }

  @Override
  public String toString() {
    return "Authorization{" +
        "login='" + login + '\'' +
        ", password='" + password + '\'' +
        '}';
  }
}
