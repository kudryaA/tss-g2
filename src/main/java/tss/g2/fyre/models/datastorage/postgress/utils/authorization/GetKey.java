package tss.g2.fyre.models.datastorage.postgress.utils.authorization;

import tss.g2.fyre.utils.RandomString;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class GetKey {
  private Connection connection;
  private String login;

  /**
   * Constructor.
   * @param connection connection to database
   * @param login user login
   */
  public GetKey(Connection connection, String login) {
    this.connection = connection;
    this.login = login;
  }

  /**
   * Method for get key for download recipe.
   * @return key
   */
  public String getKey() {
    String key = new RandomString(25).generate() + new Date().getTime();

    try (PreparedStatement selectStatement = connection
            .prepareStatement("select key from keys where login = ?")) {
      selectStatement.setString(1, login);

      try (ResultSet resultSet = selectStatement.executeQuery()) {
        if (resultSet.next()) {
          key = resultSet.getString("key");
        } else {
          try (PreparedStatement insertStatement = connection
                  .prepareStatement("insert into keys values (?, ?, 0)")) {
            insertStatement.setString(1, login);
            insertStatement.setString(2, key);
            insertStatement.executeUpdate();
          }
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return key;
  }
}
