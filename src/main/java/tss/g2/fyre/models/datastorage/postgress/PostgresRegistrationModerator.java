package tss.g2.fyre.models.datastorage.postgress;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostgresRegistrationModerator {
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
  public PostgresRegistrationModerator(Connection connection, String login,
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
  boolean createNewModerator() {
    boolean answer = false;
    PreparedStatement statement = null;
    try {
      statement = connection.prepareStatement("SELECT * FROM moderator WHERE login = ?");
      statement.setString(1, login);

      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        return answer;
      } else {
        statement = connection.prepareStatement("insert into moderator values(?, ?, ?)");
        statement.setString(1, name);
        statement.setString(2, login);
        statement.setString(3, password);
        answer = statement.executeUpdate() == 1;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (statement != null) {
          statement.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return answer;
  }
}
