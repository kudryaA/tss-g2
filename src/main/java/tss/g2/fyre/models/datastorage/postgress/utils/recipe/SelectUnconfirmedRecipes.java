package tss.g2.fyre.models.datastorage.postgress.utils.recipe;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tss.g2.fyre.models.entity.Type;
import tss.g2.fyre.models.entity.recipe.Recipe;
import tss.g2.fyre.models.entity.recipe.RecipeWithType;

public class SelectUnconfirmedRecipes {
  private static Logger logger = LoggerFactory.getLogger(SelectUnconfirmedRecipes.class);
  private Connection connection;

  /**
   * Connection to database.
   * @param connection connection to database
   */
  public SelectUnconfirmedRecipes(Connection connection) {
    this.connection = connection;
  }

  /**
   * Method for selects all unconfirmed recipes.
   * @return recipe with type list
   */
  public List<RecipeWithType> selectUnconfirmedRecipes() {
    List<RecipeWithType> recipeList = new ArrayList<>();

    try (Statement selectStatement = connection.createStatement()) {
      try (ResultSet resultSet = selectStatement
              .executeQuery("select r.recipe_id, r.name as recipeName, r.recipecomposition,\n"
                  + "r.cookingsteps, r.publicationdate, r.image as recipeImage,\n"
                  + "r.creator, r.rating, t.name as typeName, t.description, t.image as typeImage\n"
                  + "from recipe r\n"
                  + "join recipetype rt on r.recipe_id = rt.recipe_id\n"
                  + "join type t on rt.type_name = t.name\n"
                  + "where isconfirmed = false \n"
                  + "order by r.recipe_id")) {
        logger.info(selectStatement.toString());
        int i = -1;
        while (resultSet.next()) {
          String recipeId = resultSet.getString("recipe_id");
          String recipeName = resultSet.getString("recipeName");
          String recipeComposition = resultSet.getString("recipeComposition");
          String cookingSteps = resultSet.getString("cookingSteps");
          Date publicationDate = resultSet.getDate("publicationDate");
          String recipeImage = resultSet.getString("recipeImage");
          String creator  = resultSet.getString("creator");
          long rating = resultSet.getLong("rating");
          String typeName = resultSet.getString("typeName");
          String description = resultSet.getString("description");
          String typeImage = resultSet.getString("typeImage");

          if (recipeList.size() == 0 || recipeList.get(i).getId() != recipeId) {
            recipeList.add(new RecipeWithType(
                    new Recipe(recipeId, recipeName, recipeComposition,
                            cookingSteps, publicationDate, recipeImage,
                            creator, rating),
                    new ArrayList<>(Arrays.asList(new Type(typeName, description, typeImage)))));
            i++;
          } else {
            recipeList.get(i).getTypes().add(typeName);
          }
        }
      }
    } catch (SQLException e) {
      logger.error(e.getMessage());
    }

    return recipeList;
  }
}
