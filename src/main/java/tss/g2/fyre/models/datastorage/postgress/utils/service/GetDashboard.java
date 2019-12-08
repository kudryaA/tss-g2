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
                + "(select count(*) from likes) as countLikes,"
                + "(select count(*) from type) as countTypes")) {
      try (ResultSet resultSet = selectStatement.executeQuery()) {
        if (resultSet.next()) {
          map.put("countUsers", resultSet.getInt("countUsers"));
          map.put("countExperiencedUsers", resultSet.getInt("countExperiencedUsers"));
          map.put("countModerators", resultSet.getInt("countModerators"));
          map.put("countAdmins", resultSet.getInt("countAdmins"));
          map.put("countRecipes", resultSet.getInt("countRecipes"));
          map.put("countSDRecipes", resultSet.getInt("countSDRecipes"));
          map.put("countComments", resultSet.getInt("countComments"));
          map.put("countLikes", resultSet.getInt("countLikes"));
          map.put("countTypes", resultSet.getInt("countTypes"));
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
