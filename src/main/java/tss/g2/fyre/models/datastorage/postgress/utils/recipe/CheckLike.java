package tss.g2.fyre.models.datastorage.postgress.utils.recipe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckLike {
  private Connection connection;
  private String user_login;
  private String recipeId;

  /**
   * Constructor.
   * @param connection connection to database
   * @param user_login user login
   * @param recipeId recipe id
   */
  public CheckLike(Connection connection, String user_login, String recipeId) {
    this.connection = connection;
    this.user_login = user_login;
    this.recipeId = recipeId;
  }

  /**
   * Method for checking like on a recipe from a given user.
   * @return result of check
   */
  public boolean checkLike() {
    boolean result = false;

    try (PreparedStatement checkStatement = connection
            .prepareStatement("select 1 from likes where user_login = ? and recipe_id = ?")) {
      checkStatement.setString(1, user_login);
      checkStatement.setString(2, recipeId);

      try (ResultSet resultSet = checkStatement.executeQuery()) {
        result = resultSet.next();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return result;
  }
}
