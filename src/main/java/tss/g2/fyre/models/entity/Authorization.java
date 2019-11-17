package tss.g2.fyre.models.entity;

import java.util.Objects;

/**
 * Class for store authorization info.
 * @author Anton Kudryavtsev
 */
public class Authorization {
  private String login;
  private String role;

  /**
   * Constructor.
   * @param login login of user
   * @param role user role
   */
  public Authorization(String login, String role) {
    this.login = login;
    this.role = role;
  }

  /**
   * Get user login.
   * @return user login
   */
  public String getLogin() {
    return login;
  }

  /**
   * Get is user role.
   * @return user role
   */
  public String getRole() {
    return role;
  }

  @Override
  public String toString() {
    return "Authorization{"
        + "login='" + login + '\''
        + ", role=" + role
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Authorization that = (Authorization) o;
    return Objects.equals(login, that.login)
            &&
            Objects.equals(role, that.role);
  }

  @Override
  public int hashCode() {
    return Objects.hash(login, role);
  }
}
