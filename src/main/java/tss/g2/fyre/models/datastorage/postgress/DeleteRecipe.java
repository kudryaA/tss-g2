package tss.g2.fyre.models.datastorage.postgress;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for delete recipe.
 *
 * @author Andrey Sherstyuk
 */
class DeleteRecipe {
  private Logger deleteRecipeLogger = LoggerFactory.getLogger(DeleteRecipe.class);

  private Connection connection;
  private int recipeId;
  private String user;

  /**
   * Constructor for delete recipe.
   *
   * @param connection connection to database
   * @param recipeId recipe id
   * @param user authorization user
   */
  DeleteRecipe(Connection connection, int recipeId, String user) {
    this.connection = connection;
    this.recipeId = recipeId;
    this.user = user;
  }

  /**
   * Method for removing the recipe.
   *
   * @return result of deleting
   */
  public boolean deleteRecipe() {
    boolean result = false;

    try (PreparedStatement checkStatement = connection
            .prepareStatement("select 1 from recipe where recipe_id = ? "
                    + "and (creator = ? or (select role from person where login = ?) = 'admin')")) {
      checkStatement.setInt(1, recipeId);
      checkStatement.setString(2, user);
      checkStatement.setString(3, user);

      deleteRecipeLogger.info(checkStatement.toString());
      try (ResultSet resultSet = checkStatement.executeQuery()) {
        if (resultSet.next()) {
          try (PreparedStatement deleteRelationStatement =
                       connection.prepareStatement("delete from recipetype where recipe_id = ?")) {
            deleteRelationStatement.setInt(1, recipeId);
            deleteRelationStatement.executeUpdate();

            try (PreparedStatement deleteRecipeStatement =
                         connection.prepareStatement("delete from recipe where recipe_id = ?")) {
              deleteRecipeStatement.setInt(1, recipeId);

              deleteRecipeLogger.info(deleteRecipeStatement.toString());
              result = deleteRecipeStatement.executeUpdate() == 1;
            }
          }
        }
      }
    } catch (SQLException e) {
      deleteRecipeLogger.error(e.getMessage());
    }

    return result;
  }
}
