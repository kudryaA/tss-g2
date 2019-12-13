package tss.g2.fyre.models.datastorage.postgress.utils.recipe;

import tss.g2.fyre.models.entity.recipe.Recipe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SelectUserRecipes {
  private Connection connection;
  private String login;

  /**
   * Constructor.
   * @param connection connection to database
   * @param login user login
   */
  public SelectUserRecipes(Connection connection, String login) {
    this.connection = connection;
    this.login = login;
  }

  public List<Recipe> selectUserRecipes() {
    List<Recipe> list = new ArrayList<>();

    try (PreparedStatement selectStatement = connection
            .prepareStatement("select recipe_id, name, recipecomposition, image from recipe "
                    + "where creator = ? and (isconfirmed = false or publicationdate > (now() AT TIME ZONE 'UTC')) ")) {
      selectStatement.setString(1, login);

      try (ResultSet resultSet = selectStatement.executeQuery()) {
        while (resultSet.next()) {
          list.add(new Recipe(
                    resultSet.getString("name"),
                    resultSet.getString("recipecomposition"),
                    resultSet.getString("image"),
                    resultSet.getString("recipe_id")));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return list;
  }
}
