package tss.g2.fyre.models.datastorage.postgress.utils.authorization;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for handling administrator actions.
 * @author Andrey Sherstyuk
 */
public class AdminAction {
  private static Logger logger = LoggerFactory.getLogger(AdminAction.class);

  private Connection connection;
  private String userLogin;

  /**
   * Constructor.
   *
   * @param connection connection to database
   * @param userLogin user login
   */
  public AdminAction(Connection connection, String userLogin) {
    this.connection = connection;
    this.userLogin = userLogin;
  }

  /**
   * Method for change user banned status.
   * @return result of changes
   */
  public boolean changeBannedStatus() {
    boolean result = false;

    try (PreparedStatement changeStatusStatement =
             connection.prepareStatement("update person set bannedstatus = "
                     + "case when (select bannedstatus from person where login = ?) = false "
                     + "then true "
                     + "else false end where login = ?")) {
      changeStatusStatement.setString(1, userLogin);
      changeStatusStatement.setString(2, userLogin);
      logger.info(changeStatusStatement.toString());
      result = changeStatusStatement.executeUpdate() == 1;
    } catch (SQLException e) {
      logger.error(e.getMessage());
    }
    return result;
  }
}
