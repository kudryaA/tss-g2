package tss.g2.fyre.models.datastorage.postgress;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import tss.g2.fyre.models.entity.Type;
import tss.g2.fyre.models.entity.recipe.Recipe;
import tss.g2.fyre.models.entity.recipe.RecipeWithType;

/**
 * Class for get recipe by id from postgres.
 * @author Anton Kudryavtsev
 */
class GetRecipe {
  private int recipeId;
  private Connection connection;

  private static final String SQL = "SELECT * FROM recipe r JOIN recipeType rt ON(r.recipe_id = rt.recipe_id) \n" +
      "JOIN type t ON(t.name = rt.type_name) WHERE r.recipe_id = ?;";

  /**
   * Constructor.
   * @param connection postgres jdbc connection
   * @param recipeId for authorization
   */
  GetRecipe(Connection connection, int recipeId) {
    this.connection = connection;
    this.recipeId = recipeId;
  }

  public Recipe get() {
    RecipeWithType recipe = null;
    try(PreparedStatement updateStatement =
            connection.prepareStatement("UPDATE recipe SET rating=rating+1 WHERE recipe_id = ?")) {
      updateStatement.setInt(1, recipeId);
      updateStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    try (PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
      preparedStatement.setInt(1, recipeId);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        Recipe recipeWithoutType = null;
        List<Type> types = new ArrayList<>();
        while (resultSet.next()) {
          if (recipeWithoutType == null) {
            recipeWithoutType = new Recipe(
                recipeId,
                resultSet.getString("name"),
                resultSet.getString("recipecomposition"),
                resultSet.getString("cookingsteps"),
                resultSet.getDate("publicationdate"),
                resultSet.getString("image"),
                resultSet.getString("creator"),
                resultSet.getLong("rating")
            );
          }
          types.add(new Type(
                  resultSet.getString("type_name"),
                  resultSet.getString("description")
          ));
        }
        recipe = new RecipeWithType(recipeWithoutType, types);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return recipe;
  }
}
