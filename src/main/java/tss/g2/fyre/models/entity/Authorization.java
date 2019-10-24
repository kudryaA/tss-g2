package tss.g2.fyre.models.entity;

/**
 * Class for store authorization info.
 * @author Anton Kudryavtsev
 */
public class Authorization {
  private String login;
  private boolean moderator;

  /**
   * Constructor.
   * @param login login of user
   * @param moderator is moderator
   */
  public Authorization(String login, boolean moderator) {
    this.login = login;
    this.moderator = moderator;
  }

  /**
   * Get login.
   * @return return
   */
  public String getLogin() {
    return login;
  }

  /**
   * Get is moderator.
   * @return moderator
   */
  public boolean isModerator() {
    return moderator;
  }

  @Override
  public String toString() {
    return "Authorization{"
        + "login='" + login + '\''
        + ", moderator=" + moderator
        + '}';
  }
}
