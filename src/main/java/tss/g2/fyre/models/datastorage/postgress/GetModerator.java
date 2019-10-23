package tss.g2.fyre.models.datastorage.postgress;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import tss.g2.fyre.models.entity.Moderator;


/**
 * Class for get moderator from postgres database.
 * @author Anton Kudryavtsev
 */
public class GetModerator {

  private Connection connection;
  private String login;

  /**
   * Constructor.
   * @param connection postgres jdbc connection
   * @param login for authorization
   */
  public GetModerator(Connection connection, String login) {
    this.connection = connection;
    this.login = login;
  }

  /**
   * It is method for get moderator.
   * @return moderator
   */
  public Moderator getModerator() {
    Moderator moderator = null;
    try (PreparedStatement statement =
             connection.prepareStatement("SELECT * FROM moderator WHERE login = ?")) {
      statement.setString(1, login);
      System.out.println(statement.toString());
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        String name = resultSet.getString("name");
        String password = resultSet.getString("password");
        moderator = new Moderator(name, login, password);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return moderator;
  }
}
