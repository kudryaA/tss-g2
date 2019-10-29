package tss.g2.fyre.models.datastorage.postgress;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for registration moderators.
 *
 * @author Andrey Sherstyuk
 */
class RegistrationModerator {
  private Logger registrationModeratorLogger = LoggerFactory.getLogger(RegistrationModerator.class);

  private Connection connection;
  private String login;
  private String password;
  private String name;

  /**
   * Constructor.
   *
   * @param connection connection to database
   * @param login moderator login
   * @param password moderator password
   * @param name moderator name
   */
  public RegistrationModerator(Connection connection, String login,
                               String password, String name) {
    this.connection = connection;
    this.login = login;
    this.password = password;
    this.name = name;
  }

  /**
   * Method for registration moderator.
   *
   * @return result of adding moderator
   */
  boolean createModerator() {
    boolean answer = false;
    try (PreparedStatement statement
                 = connection.prepareStatement("SELECT * FROM moderator WHERE login = ?")) {
      statement.setString(1, login);

      registrationModeratorLogger.info(statement.toString());
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return answer;
        } else {
          try (PreparedStatement registrationModeratorStatement
                       = connection.prepareStatement("insert into moderator values(?, ?, ?)")) {
            registrationModeratorStatement.setString(1, name);
            registrationModeratorStatement.setString(2, login);
            registrationModeratorStatement.setString(3, password);

            registrationModeratorLogger.info(registrationModeratorStatement.toString());
            answer = registrationModeratorStatement.executeUpdate() == 1;
          }
        }
      }
    } catch (SQLException e) {
      registrationModeratorLogger.error(e.getMessage());
    }

    return answer;
  }
}
