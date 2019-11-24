package tss.g2.fyre.models.datastorage.postgress.utils.authorization;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class for get user role by login.
 */
public class GetUserRole {
  private Connection connection;
  private String login;

  /**
   * Constructor.
   * @param connection connection to database
   * @param login user login
   */
  public GetUserRole(Connection connection, String login) {
    this.connection = connection;
    this.login = login;
  }

  /**
   * Method for get user role.
   * @return user role
   */
  public String getRole() {
    String role = "user";

    try (PreparedStatement getRoleStatement = connection
            .prepareStatement("select role from person where login = ?")) {
      getRoleStatement.setString(1, login);

      try (ResultSet resultSet = getRoleStatement.executeQuery()) {
        if (resultSet.next()) {
          role = resultSet.getString("role");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return role;
  }
}
