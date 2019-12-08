package tss.g2.fyre.models.datastorage.postgress.utils.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class GetDashboard {
  private Connection connection;
  private String login;

  /**
   * Constructor.
   * @param connection connection to database
   * @param login user login
   */
  public GetDashboard(Connection connection, String login) {
    this.connection = connection;
    this.login = login;
  }

  /**
   * Method for get main site info.
   * @return main site info
   */
  public Map<String, Integer> getDashboard() {
    Map<String, Integer> map = new LinkedHashMap<>();

    try (PreparedStatement selectStatement = connection
            .prepareStatement(
                "select (select count(*) from person where role = 'user') as countUsers, "
                + "(select count(*) from person "
                + "where role = 'experiencedUser') as countExperiencedUsers,"
                + "(select count(*) from person where role = 'moderator') as countModerators,"
                + "(select count(*) from person where role = 'admin') as countAdmins,"
                + "(select count(*) from recipe) as countRecipes,"
                + "(select count(*) from recipe "
                + "where publicationdate > current_timestamp - interval '7 day') as countSDRecipes,"
                + "(select count(*) from comment) as countComments,"
                + "(select count(*) from likes) as countLikes")) {
      try (ResultSet resultSet = selectStatement.executeQuery()) {
        if (resultSet.next()) {
          map.put("Count users", resultSet.getInt("countUsers"));
          map.put("Count experienced users", resultSet.getInt("countExperiencedUsers"));
          map.put("Count moderators", resultSet.getInt("countModerators"));
          map.put("Count admins", resultSet.getInt("countAdmins"));
          map.put("Count comments", resultSet.getInt("countComments"));
          map.put("Count likes", resultSet.getInt("countLikes"));
          map.put("Count recipes", resultSet.getInt("countRecipes"));
          map.put("The number of recipes that were added in the last 7 days", resultSet
              .getInt("countSDRecipes"));
        }
      }

      try (PreparedStatement selectTypeInfoStatement = connection
              .prepareStatement("select t.name as name, count(*) as count from type t "
                  + "join recipetype r on t.name = r.type_name "
                  + "join recipe r2 on r.recipe_id = r2.recipe_id "
                  + "group by t.name")) {
        try (ResultSet resultSet = selectTypeInfoStatement.executeQuery()) {
          while (resultSet.next()) {
            map.put(resultSet.getString("name"),
                    resultSet.getInt("count"));
          }
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return map;
  }
}
