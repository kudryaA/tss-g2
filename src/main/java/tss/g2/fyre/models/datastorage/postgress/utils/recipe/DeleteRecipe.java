package tss.g2.fyre.models.datastorage.postgress.utils.recipe;

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
public class DeleteRecipe {
  private static Logger logger = LoggerFactory.getLogger(DeleteRecipe.class);

  private Connection connection;
  private String recipeId;
  private String user;

  /**
   * Constructor for delete recipe.
   *
   * @param connection connection to database
   * @param recipeId recipe id
   * @param user authorization user
   */
  public DeleteRecipe(Connection connection, String recipeId, String user) {
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
      checkStatement.setString(1, recipeId);
      checkStatement.setString(2, user);
      checkStatement.setString(3, user);

      logger.info(checkStatement.toString());
      try (ResultSet resultSet = checkStatement.executeQuery()) {
        if (resultSet.next()) {
          try (PreparedStatement deleteLikesStatement = connection
                  .prepareStatement("delete from likes where recipe_id = ?")) {
            deleteLikesStatement.setString(1, recipeId);
            deleteLikesStatement.executeUpdate();

            try (PreparedStatement deleteRelationStatement =
                       connection.prepareStatement("delete from recipetype where recipe_id = ?")) {
              deleteRelationStatement.setString(1, recipeId);
              deleteRelationStatement.executeUpdate();

              try (PreparedStatement deleteRecipeStatement =
                           connection.prepareStatement("delete from recipe where recipe_id = ?")) {
                deleteRecipeStatement.setString(1, recipeId);

                logger.info(deleteRecipeStatement.toString());
                result = deleteRecipeStatement.executeUpdate() == 1;
              }
            }
          }
        }
      }
    } catch (SQLException e) {
      logger.error(e.getMessage());
    }

    return result;
  }
}
