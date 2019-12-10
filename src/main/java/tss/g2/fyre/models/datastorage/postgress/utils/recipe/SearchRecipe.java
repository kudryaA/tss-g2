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
  private String searchQuery;
  private int pageNumber;
  private int pageSize;

  /**
   * Constructor.
   *
   * @param connection connection to database
   * @param searchQuery ingredient for search
   * @param pageNumber page number
   * @param pageSize page size
   */
  public SearchRecipe(Connection connection, String searchQuery, int pageNumber, int pageSize) {
    this.connection = connection;
    this.searchQuery = searchQuery;
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
            .prepareStatement("SELECT recipe.*, ts_rank_cd(to_tsvector(recipe.name), query) r_name, "
                    + "ts_rank_cd(to_tsvector(recipe.recipecomposition), query) r_composition, "
                    + "ts_rank_cd(to_tsvector(recipe.cookingsteps), query) r_steps "
                    + "FROM recipe, plainto_tsquery('english', '%'|| ? ||'%') query "
                    + "WHERE (query @@ to_tsvector(recipe.name) or "
                    + "query @@ to_tsvector(recipe.cookingsteps) or "
                    + "query @@ to_tsvector(recipe.recipecomposition)) "
                    + "AND publicationdate <= (now() AT TIME ZONE 'UTC') "
                    + "and isconfirmed = true "
                    + "ORDER BY r_name DESC, r_composition DESC, r_steps DESC "
                    + "offset ? fetch first ? row only ")) {
      searchStatement.setString(1, searchQuery);
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
            .prepareStatement("select count(recipe_id) as count "
            + "FROM recipe, plainto_tsquery('english', ?) query "
            + "WHERE (query @@ to_tsvector(recipe.name) or "
            + "query @@ to_tsvector(recipe.cookingsteps) or "
            + "query @@ to_tsvector(recipe.recipecomposition)) "
            + "AND publicationdate <= (now() AT TIME ZONE 'UTC') "
            + "and isconfirmed = true")) {
      selectCountPagesStatement.setString(1, searchQuery);

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
