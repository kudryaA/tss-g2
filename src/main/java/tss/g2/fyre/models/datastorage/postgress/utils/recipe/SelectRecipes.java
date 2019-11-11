package tss.g2.fyre.models.datastorage.postgress.utils.recipe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tss.g2.fyre.models.entity.Type;
import tss.g2.fyre.models.entity.recipe.Recipe;
import tss.g2.fyre.models.entity.recipe.RecipeWithType;

/**
 * Class for select all recipes with sorting.
 * @author Andrey Sherstyuk
 */
public class SelectRecipes {
  private static Logger logger = LoggerFactory.getLogger(SelectRecipes.class);

  private Connection connection;
  private int pageNumber;
  private int pageSize;
  private String recipeType;
  private String sortType;

  /**
   * Constructor.
   */
  public SelectRecipes() {
  }

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
    List<RecipeWithType> recipeWithTypesList = new ArrayList<>();
    List<Recipe> recipeList = new ArrayList<>();

    try (PreparedStatement selectStatement = connection
              .prepareStatement("select distinct r.recipe_id, name, recipecomposition, "
              + "cookingsteps, publicationdate, image, "
              + "(select name from person where login = r.creator), rating "
              + "from recipe r\n"
              + "join recipetype r2 on r.recipe_id = r2.recipe_id\n"
              + "where r2.type_name like '%' || ? || '%' "
                + "and publicationdate <= (now() AT TIME ZONE 'UTC') \n"
                + "and isConfirmed = true \n"
              + "order by " + sortType + " desc\n"
              + "offset ? fetch first ? row only ")) {
      selectStatement.setString(1, recipeType);
      selectStatement.setInt(2, (pageNumber - 1) * pageSize);
      selectStatement.setInt(3, pageSize);

      logger.info(selectStatement.toString());
      try (ResultSet resultSet = selectStatement.executeQuery()) {
        fillRecipeList(recipeList, resultSet);
      }
    } catch (SQLException e) {
      logger.error(e.getMessage());
    }

    Map<String, List<Type>> recipeTypeMap = new HashMap<>();
    try (Statement selectTypesStatement = connection.createStatement()) {
      try (ResultSet resultSet = selectTypesStatement
              .executeQuery("select recipe_id, t.name as typeName, "
                      + "description, t.image as typeImage from recipetype "
                      + "join type t on recipetype.type_name = t.name")) {
        while (resultSet.next()) {
          String recipeId = resultSet.getString("recipe_id");
          if (recipeTypeMap.containsKey(recipeId)) {
            recipeTypeMap.get(recipeId)
                    .add(new Type(resultSet.getString("typeName"),
                            resultSet.getString("description"), resultSet.getString("typeImage")));
          } else {
            List<Type> types = new ArrayList<>();
            types.add(new Type(resultSet.getString("typeName"),
                    resultSet.getString("description"), resultSet.getString("typeImage")));
            recipeTypeMap.put(recipeId, types);
          }
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    for (Recipe recipe : recipeList) {
      recipeWithTypesList.add(new RecipeWithType(recipe, recipeTypeMap.get(recipe.getId())));
    }

    Map<String, Object> map = new HashMap<>();
    map.put("recipes", recipeWithTypesList);

    int countPages = 0;
    try (PreparedStatement getCountStatement = connection
            .prepareStatement("select count(distinct r.recipe_id) from recipe r\n"
            + "join recipetype r2 on r.recipe_id = r2.recipe_id\n"
            + "where r2.type_name like '%' || ? || '%'"
              + " and publicationdate <= (now() AT TIME ZONE 'UTC') "
              + " and isConfirmed = true ")) {
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

  void fillRecipeList(List<Recipe> recipeList, ResultSet resultSet) {
    try {
      while (resultSet.next()) {
        String id = resultSet.getString(1);
        String recipeName = resultSet.getString(2);
        String composition = resultSet.getString(3);
        String cookingSteps = resultSet.getString(4);
        Date publicationDate = resultSet.getTimestamp(5);
        String image = resultSet.getString(6);
        String creator = resultSet.getString(7);
        long rating = resultSet.getLong(8);

        recipeList.add(new Recipe(id, recipeName, composition, cookingSteps,
                                  publicationDate, image, creator, rating));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
