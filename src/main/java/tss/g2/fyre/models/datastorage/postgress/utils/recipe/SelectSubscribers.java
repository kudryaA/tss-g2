package tss.g2.fyre.models.datastorage.postgress.utils.recipe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SelectSubscribers {
  private Connection connection;
  private String login;

  /**
   * Constructor.
   * @param connection connection to database
   * @param login user login
   */
  public SelectSubscribers(Connection connection, String login) {
    this.connection = connection;
    this.login = login;
  }

  /**
   * Method for select subscribers email address.
   * @return list of subscribers email address
   */
  public List<String> selectSubscribers() {
    List<String> emailList = new ArrayList<>();
    try (PreparedStatement selectStatement = connection
            .prepareStatement("select email from person p "
                    + "  left join usersubscribe u on p.login = u.user_login "
                    + "where sub_login = ? or login = ?")) {
      selectStatement.setString(1, login);
      selectStatement.setString(2, login);

      try (ResultSet resultSet = selectStatement.executeQuery()) {
        while (resultSet.next()) {
          emailList.add(resultSet.getString("email"));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return emailList;
  }
}
