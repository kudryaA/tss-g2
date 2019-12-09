package tss.g2.fyre.models.datastorage.postgress.utils.authorization;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeleteSubscribe {
  private Connection connection;
  private String user_login;
  private String sub_login;

  /**
   * Constructor.
   * @param connection connection to database
   * @param user_login user login
   * @param sub_login user login on which want to unsubscribed
   */
  public DeleteSubscribe(Connection connection, String user_login, String sub_login) {
    this.connection = connection;
    this.user_login = user_login;
    this.sub_login = sub_login;
  }

  /**
   * Method for delete subscribe.
   * @return result of delete
   */
  public boolean deleteSubscribe() {
    boolean result = false;

    try (PreparedStatement deleteSubscribeStatement = connection
        .prepareStatement("delete from usersubscribe where user_login = ? and sub_login = ?")) {
      deleteSubscribeStatement.setString(1, user_login);
      deleteSubscribeStatement.setString(2, sub_login);

      result = deleteSubscribeStatement.executeUpdate() == 1;
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return result;
  }
}
