package tss.g2.fyre.models.datastorage.postgress.utils.authorization;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ChangePassword {
  private Connection connection;
  private String password;
  private String login;

  /**
   * Constructor.
   * @param connection connection to database
   * @param password new password
   * @param login login
   */
  public ChangePassword(Connection connection, String password, String login) {
    this.connection = connection;
    this.password = password;
    this.login = login;
  }

  public boolean changePassword() {
    boolean result = false;

    try (PreparedStatement changePasswordStatement = connection
            .prepareStatement("UPDATE person set password = ? where login = ?")) {
      changePasswordStatement.setString(1, password);
      changePasswordStatement.setString(2, login);

      result = changePasswordStatement.executeUpdate() == 1;
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return result;
  }
}
