package tss.g2.fyre.models.entity;

import java.util.Objects;

/**
 * Describe moderator.
 * @author Anton Kudryavtsev
 */
public class Moderator {
  private String name;
  private String login;
  private String password;

  /**
   * Constructor.
   * @param name name of moderator
   * @param login login of moderator
   * @param password password of moderator
   */
  public Moderator(String name, String login, String password) {
    this.name = name;
    this.login = login;
    this.password = password;
  }

  /**
   * Method for get name of moderator.
   * @return name of moderator
   */
  public String getName() {
    return name;
  }

  /**
   * Method for get login of moderator.
   * @return login of moderator
   */
  public String getLogin() {
    return login;
  }

  /**
   * Method for get password of moderator.
   * @return password of moderator
   */
  public String getPassword() {
    return password;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Moderator moderator = (Moderator) o;
    return Objects.equals(name, moderator.name)
        && Objects.equals(login, moderator.login)
        && Objects.equals(password, moderator.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, login, password);
  }

  @Override
  public String toString() {
    return "Moderator{"
        + "name='" + name + '\''
        + ", login='" + login + '\''
        + ", password='" + password + '\''
        + '}';
  }
}
