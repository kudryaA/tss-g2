package tss.g2.fyre.models.datastorage.postgress.utils.authorization;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckSubscribe {
  private Connection connection;
  private String user_login;
  private String sub_login;

  /**
   * Constructor.
   * @param connection connection to database.
   * @param user_login user login
   * @param sub_login user login to which this user is subscribed
   */
  public CheckSubscribe(Connection connection, String user_login, String sub_login) {
    this.connection = connection;
    this.user_login = user_login;
    this.sub_login = sub_login;
  }

  /**
   * Method for check subscribe.
   * @return result of check
   */
  public boolean checkSubscribe() {
    boolean result = false;

    try (PreparedStatement checkStatement = connection
          .prepareStatement("select 1 from usersubscribe where user_login = ? and sub_login = ?")) {
      checkStatement.setString(1, user_login);
      checkStatement.setString(2, sub_login);

      try (ResultSet resultSet = checkStatement.executeQuery()) {
        result = resultSet.next();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return result;
  }
}
