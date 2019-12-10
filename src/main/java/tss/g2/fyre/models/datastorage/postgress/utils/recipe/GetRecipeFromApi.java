package tss.g2.fyre.models.datastorage.postgress.utils.recipe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import tss.g2.fyre.models.entity.recipe.Recipe;

public class GetRecipeFromApi {
  private Connection connection;
  private String recipeId;
  private String key;

  /**
   * Constructor.
   * @param connection connection to database
   * @param recipeId recipe key
   * @param key user login
   */
  public GetRecipeFromApi(Connection connection, String recipeId, String key) {
    this.connection = connection;
    this.recipeId = recipeId;
    this.key = key;
  }

  /**
   * Method for get recipe by recipe id.
   * @return recipe object
   */
  public Recipe getRecipeFromApi() {
    Recipe recipe = null;

    try (PreparedStatement checkStatement = connection.prepareStatement("select * from keys where key = ?")) {
      checkStatement.setString(1, key);
      try (ResultSet resultSet = checkStatement.executeQuery()) {
        if (resultSet.next()) {
          try (PreparedStatement updateStatement = connection
                  .prepareStatement("update keys set numberofdownload = numberofdownload + 1 "
                          + "where key = ?")) {
            updateStatement.setString(1, key);
            updateStatement.executeUpdate();

            recipe = new GetRecipe(connection, recipeId).get();
          }
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return recipe;
  }
}
