package tss.g2.fyre.models.datastorage.postgress.utils.authorization;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

  public String changePassword() {
    String message = "";

    try (PreparedStatement checkStatement = connection.prepareStatement("select 1 from person where login = ? and password = ?")) {
      try (ResultSet resultSet = checkStatement.executeQuery()) {
        if (resultSet.next()) {
          message = "New password shouldn't match the old one.";
        } else {
          try (PreparedStatement changePasswordStatement = connection
                  .prepareStatement("UPDATE person set password = ? where login = ?")) {
            changePasswordStatement.setString(1, password);
            changePasswordStatement.setString(2, login);

            message = changePasswordStatement.executeUpdate() == 1 ? "ok" : "Some errors";
          }
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return message;
  }
}
