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

public class SelectSubscribedRecipes {
  Logger logger = LoggerFactory.getLogger(SelectSubscribedRecipes.class);
  private Connection connection;
  private int pageNumber;
  private int pageSize;
  private String login;

  /**
   * Constructor.
   * @param connection connection to database.
   * @param pageNumber page number
   * @param pageSize page size
   * @param login login
   */
  public SelectSubscribedRecipes(Connection connection, int pageNumber,
                                 int pageSize, String login) {
    this.connection = connection;
    this.pageNumber = pageNumber;
    this.pageSize = pageSize;
    this.login = login;
  }

  /**
   * Method for select subscribed recipes.
   * @return subscribed recipes
   */
  public Map<String, Object> selectSubscribedRecipes() {
    Map<String, Object> map = new HashMap<>();

    try (PreparedStatement selectStatement = connection
            .prepareStatement("select r.recipe_id, r.name, cookingsteps, image "
                    + "from recipe r "
                    + " join usersubscribe us on r.creator = us.sub_login "
                    + " join person p on us.user_login = p.login "
                    + "where publicationdate <= (now() AT TIME ZONE 'UTC') "
                    + " and isConfirmed = true and p.login = ? "
                    + "offset ? fetch first ? row only")) {
      selectStatement.setString(1, login);
      selectStatement.setInt(2, (pageNumber - 1) * pageSize);
      selectStatement.setInt(3, pageSize);

      List<Recipe> recipeList = new ArrayList<>();
      try (ResultSet resultSet = selectStatement.executeQuery()) {
        while (resultSet.next()) {
          recipeList.add(new Recipe(
                resultSet.getString("name"),
                resultSet.getString("cookingsteps"),
                resultSet.getString("image"),
                resultSet.getString("recipe_id")));
        }
      }
      map.put("recipes", recipeList);

      int countPages = 0;
      try (PreparedStatement selectCountStatement = connection
            .prepareStatement("select count(recipe_id) as count "
                    + "from recipe r "
                    + " join usersubscribe us on r.creator = us.sub_login "
                    + " join person p on us.user_login = p.login "
                    + "where publicationdate <= (now() AT TIME ZONE 'UTC') "
                    + " and isConfirmed = true and p.login = ?")) {
        selectCountStatement.setString(1, login);

        try (ResultSet resultSet = selectCountStatement.executeQuery()) {
          if (resultSet.next()) {
            int count = resultSet.getInt("count");
            countPages = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
          }
        }
      }
      map.put("pages", countPages);
    } catch (SQLException e) {
      logger.error("Some trouble when SelectedSubscribedRecipes. Message - {}", e.getMessage());
    }

    return map;
  }
}
