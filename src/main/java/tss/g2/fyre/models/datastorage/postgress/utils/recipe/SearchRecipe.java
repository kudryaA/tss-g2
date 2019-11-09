package tss.g2.fyre.models.datastorage.postgress.utils.recipe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tss.g2.fyre.models.entity.recipe.Recipe;

/**
 * Class for search recipe by ingredient name.
 * @author Andrey Sherstyuk
 */
public class SearchRecipe {
  private static Logger logger = LoggerFactory.getLogger(SearchRecipe.class);

  private Connection connection;
  private String ingredient;

  /**
   * Constructor.
   *
   * @param connection connection to database
   * @param ingredient ingredient for search
   */
  public SearchRecipe(Connection connection, String ingredient) {
    this.connection = connection;
    this.ingredient = ingredient;
  }

  /**
   * Method for search recipe by ingredient.
   * @return the recipe found
   */
  public List<Recipe> searchRecipe() {
    List<Recipe> recipeList = new ArrayList<>();

    try (PreparedStatement searchStatement = connection
            .prepareStatement("select * from recipe "
                    + "where recipecomposition like '%' || ? || '%' "
                    + "AND publicationdate <= (now() AT TIME ZONE 'UTC') ")) {
      searchStatement.setString(1, ingredient);

      logger.info(searchStatement.toString());
      try (ResultSet resultSet = searchStatement.executeQuery()) {
        new SelectRecipes().fillRecipeList(recipeList, resultSet);
      }
    } catch (SQLException e) {
      logger.error(e.getMessage());
    }

    return recipeList;
  }
}
