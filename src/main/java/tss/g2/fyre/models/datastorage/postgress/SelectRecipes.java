package tss.g2.fyre.models.datastorage.postgress;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tss.g2.fyre.models.entity.recipe.Recipe;

public class SelectRecipes {
  private Connection connection;
  private int pageNumber;
  private int pageSize;
  private String recipeType;
  private String sortType;

  /**
   * Constructor.
   *
   * @param connection connection to database
   * @param pageNumber page number
   * @param pageSize page size
   * @param recipeType recipe type
   * @param sortType sort type
   */
  public SelectRecipes(Connection connection, int pageNumber, int pageSize,
                       String recipeType, String sortType) {
    this.connection = connection;
    this.pageNumber = pageNumber;
    this.pageSize = pageSize;
    this.recipeType = recipeType;
    this.sortType = sortType;
  }

  /**
   * Method for get the requested information.
   *
   * @return the requested information
   */
  public Map<String, Object> selectRecipes() {
    Map<String, Object> map = new HashMap<>();
    List<Recipe> recipeList = new ArrayList<>();

    if ("date".equals(sortType)) {
      try (PreparedStatement selectStatement = connection
              .prepareStatement("select distinct r.recipe_id, name, recipecomposition, "
              + "cookingsteps, publicationdate, image, "
              + "(select name from person where login = r.creator), rating "
              + "from recipe r\n"
              + "join recipetype r2 on r.recipe_id = r2.recipe_id\n"
              + "where r2.type_name like '%' || ? || '%' and publicationdate < current_date\n"
              + "order by publicationdate desc\n"
              + "offset ? fetch first ? row only ")) {
        selectStatement.setString(1, recipeType);
        selectStatement.setInt(2, (pageNumber - 1) * pageSize);
        selectStatement.setInt(3, pageSize);

        try (ResultSet resultSet = selectStatement.executeQuery()) {
          fillRecipeList(recipeList, resultSet);
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    } else {
      try (PreparedStatement selectStatement = connection
            .prepareStatement("select distinct r.recipe_id, name, recipecomposition, "
                    + "cookingsteps, publicationdate, image, "
                    + "(select name from person where login = r.creator), rating "
                    + "from recipe r\n"
                    + "join recipetype r2 on r.recipe_id = r2.recipe_id\n"
                    + "where r2.type_name like '%' || ? || '%' and publicationdate < current_date\n"
                    + "order by rating desc\n"
                    + "offset ? fetch first ? row only ")) {
        selectStatement.setString(1, recipeType);
        selectStatement.setInt(2, (pageNumber - 1) * pageSize);
        selectStatement.setInt(3, pageSize);

        try (ResultSet resultSet = selectStatement.executeQuery()) {
          fillRecipeList(recipeList, resultSet);
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    map.put("recipes", recipeList);

    int countPages = 0;
    try (PreparedStatement getCountStatement = connection
            .prepareStatement("select count(distinct r.name) from recipe r\n"
            + "join recipetype r2 on r.recipe_id = r2.recipe_id\n"
            + "where r2.type_name like '%' || ? || '%' and publicationdate < current_date")) {
      getCountStatement.setString(1, recipeType);

      try (ResultSet resultSet = getCountStatement.executeQuery()) {
        if (resultSet.next()) {
          int count = resultSet.getInt(1);
          countPages = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    map.put("pages", countPages);

    return map;
  }

  private void fillRecipeList(List<Recipe> recipeList, ResultSet resultSet) {
    try {
      while (resultSet.next()) {
        int id = resultSet.getInt(1);
        String recipeName = resultSet.getString(2);
        String composition = resultSet.getString(3);
        String cookingSteps = resultSet.getString(4);
        Date publicationDate = resultSet.getDate(5);
        String image = resultSet.getString(6);
        String creator = resultSet.getString(7);
        int rating = resultSet.getInt(8);

        recipeList.add(new Recipe(id, recipeName, composition, cookingSteps,
                                  publicationDate, image, creator, rating));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
