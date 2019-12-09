package tss.g2.fyre.models.datastorage.postgress.utils.authorization;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConfirmMail {
  private Connection connection;
  private String key;

  /**
   * Constructor.
   * @param connection connection to database
   * @param key confirmation key
   */
  public ConfirmMail(Connection connection, String key) {
    this.connection = connection;
    this.key = key;
  }

  public void confirmMail() {
    try (PreparedStatement confirmStatement = connection
            .prepareStatement("delete from mailconfirmation where confirmationkey = ?")) {
      confirmStatement.setString(1, key);
      confirmStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
