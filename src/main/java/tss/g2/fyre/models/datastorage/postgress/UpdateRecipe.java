package tss.g2.fyre.models.datastorage.postgress;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateRecipe {
  private Logger updateRecipeLogger = LoggerFactory.getLogger(UpdateRecipe.class);

  private Connection connection;
  private int recipeId;
  private String recipeName;
  private String composition;
  private String cookingSteps;
  private String creator;

  /**
   * Constructor.
   *
   * @param connection connection to database
   * @param recipeId recipe id
   * @param recipeName recipe name
   * @param composition composition
   * @param cookingSteps cooking steps
   * @param creator user who create recipe
   */
  public UpdateRecipe(Connection connection, int recipeId, String recipeName,
                      String composition, String cookingSteps, String creator) {
    this.connection = connection;
    this.recipeId = recipeId;
    this.recipeName = recipeName;
    this.composition = composition;
    this.cookingSteps = cookingSteps;
    this.creator = creator;
  }

  /**
   * Method for update recipe.
   * @return the result of the update
   */
  public boolean updateRecipe() {
    boolean result = false;

    try (PreparedStatement updateStatement = connection
            .prepareStatement("update recipe set name = ?, recipecomposition = ?, "
                    + "cookingsteps = ? where recipe_id = ? "
                    + "and (creator = ? or (select role from person where login = ?) = 'admin')")) {
      updateStatement.setString(1, recipeName);
      updateStatement.setString(2, composition);
      updateStatement.setString(3, cookingSteps);
      updateStatement.setInt(4, recipeId);
      updateStatement.setString(5, creator);

      updateRecipeLogger.info(updateStatement.toString());
      result = updateStatement.executeUpdate() == 1;
    } catch (SQLException e) {
      updateRecipeLogger.error(e.getMessage());
    }

    return result;
  }
}
