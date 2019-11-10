package tss.g2.fyre.models.datastorage.postgress.utils.recipe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tss.g2.fyre.models.entity.Type;
import tss.g2.fyre.models.entity.recipe.Recipe;
import tss.g2.fyre.models.entity.recipe.RecipeWithType;

/**
 * Class for get recipe by id from postgres.
 * @author Anton Kudryavtsev
 */
public class GetRecipe {
  private static Logger logger = LoggerFactory.getLogger(GetRecipe.class);

  private String recipeId;
  private Connection connection;

  private static final String SQL = "SELECT r.name as recipeName, recipecomposition, cookingsteps, "
          + "publicationdate, r.image as recipeImage, creator, rating, "
          + "type_name, description, t.image as typeImage FROM recipe r "
          + "JOIN recipeType rt ON(r.recipe_id = rt.recipe_id) \n"
          + "JOIN type t ON(t.name = rt.type_name) WHERE r.recipe_id = ?;";

  /**
   * Constructor.
   * @param connection postgres jdbc connection
   * @param recipeId for authorization
   */
  public GetRecipe(Connection connection, String recipeId) {
    this.connection = connection;
    this.recipeId = recipeId;
  }

  /**
   * Method for get recipe by recipe id.
   * @return recipe object
   */
  public Recipe get() {
    RecipeWithType recipe = null;
    try (PreparedStatement updateStatement =
            connection.prepareStatement("UPDATE recipe SET rating=rating+1 WHERE recipe_id = ?")) {
      updateStatement.setString(1, recipeId);

      logger.info(updateStatement.toString());
      updateStatement.executeUpdate();
    } catch (SQLException e) {
      logger.error(e.getMessage());
    }

    try (PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
      logger.info(preparedStatement.toString());
      preparedStatement.setString(1, recipeId);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        Recipe recipeWithoutType = null;
        List<Type> types = new ArrayList<>();
        while (resultSet.next()) {
          if (recipeWithoutType == null) {
            recipeWithoutType = new Recipe(
                recipeId,
                resultSet.getString("recipeName"),
                resultSet.getString("recipecomposition"),
                resultSet.getString("cookingsteps"),
                resultSet.getTimestamp("publicationdate"),
                resultSet.getString("recipeImage"),
                resultSet.getString("creator"),
                resultSet.getLong("rating")
            );
          }
          types.add(new Type(
                  resultSet.getString("type_name"),
                  resultSet.getString("description"),
                  resultSet.getString("typeImage")
          ));
        }
        recipe = new RecipeWithType(recipeWithoutType, types);
      }
    } catch (SQLException e) {
      logger.error(e.getMessage());
    }
    return recipe;
  }
}
