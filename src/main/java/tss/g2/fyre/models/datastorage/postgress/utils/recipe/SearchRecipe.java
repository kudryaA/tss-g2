package tss.g2.fyre.models.datastorage.postgress.utils.recipe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
  private int pageNumber;
  private int pageSize;

  /**
   * Constructor.
   *
   * @param connection connection to database
   * @param ingredient ingredient for search
   * @param pageNumber page number
   * @param pageSize page size
   */
  public SearchRecipe(Connection connection, String ingredient, int pageNumber, int pageSize) {
    this.connection = connection;
    this.ingredient = ingredient;
    this.pageNumber = pageNumber;
    this.pageSize = pageSize;
  }

  /**
   * Method for search recipe by ingredient.
   * @return the recipe found
   */
  public Map<String, Object> searchRecipe() {
    Map<String, Object> searchResult = new HashMap<>();
    List<Recipe> recipeList = new ArrayList<>();

    try (PreparedStatement searchStatement = connection
            .prepareStatement("select * from recipe "
                    + "where recipecomposition like '%' || ? || '%' "
                    + "AND publicationdate <= (now() AT TIME ZONE 'UTC') "
                    + "and isconfirmed = true "
                    + "offset ? fetch first ? row only ")) {
      searchStatement.setString(1, ingredient);
      searchStatement.setInt(2, (pageNumber - 1) * pageSize);
      searchStatement.setInt(3, pageSize);

      logger.info(searchStatement.toString());
      try (ResultSet resultSet = searchStatement.executeQuery()) {
        new SelectRecipes().fillRecipeList(recipeList, resultSet);
      }
    } catch (SQLException e) {
      logger.error(e.getMessage());
    }
    searchResult.put("recipes", recipeList);

    try (PreparedStatement selectCountPagesStatement = connection
            .prepareStatement("select count(recipe_id) as count from recipe "
            + "where recipecomposition like '%' || ? || '%' "
            + "AND publicationdate <= (now() AT TIME ZONE 'UTC') "
            + "and isconfirmed = true")) {
      selectCountPagesStatement.setString(1, ingredient);

      logger.info(selectCountPagesStatement.toString());
      try (ResultSet resultSet = selectCountPagesStatement.executeQuery()) {
        resultSet.next();
        int count = resultSet.getInt("count");
        searchResult.put("pages", count % pageSize == 0 ? count / pageSize : count / pageSize + 1);
      }
    } catch (SQLException e) {
      logger.error(e.getMessage());
    }
    return searchResult;
  }
}
