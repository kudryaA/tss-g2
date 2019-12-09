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
          /*
          try (PreparedStatement updateStatement = connection
                  .prepareStatement("update users_rating set rating = rating - " +
                          "(select count(*) from likes where recipe_id = ? and user_login <> " +
                          "(select creator from recipe where recipe.recipe_id = ?))" +
                          "where user_login = (select creator from recipe where recipe.recipe_id = ?)")) {
            updateStatement.setString(1, recipeId);
            updateStatement.setString(2, recipeId);
            updateStatement.setString(3, recipeId);
            updateStatement.executeUpdate();
            logger.info(updateStatement.toString());
            updateStatement.executeUpdate();
          }

 */
          /*
          try (PreparedStatement updateStatement = connection
                  .prepareStatement("update users_rating set rating = rating - " +
                          "(select count(distinct user_login) from comment where recipe_id = ? and user_login <> " +
                          "(select creator from recipe where recipe.recipe_id = ?))" +
                          "where user_login = (select creator from recipe where recipe.recipe_id = ?)")) {
            updateStatement.setString(1, recipeId);
            updateStatement.setString(2, recipeId);
            updateStatement.setString(3, recipeId);
            updateStatement.executeUpdate();
            logger.info(updateStatement.toString());
            updateStatement.executeUpdate();
          }

           */




          try (PreparedStatement deleteLikesStatement = connection
                  .prepareStatement("delete from likes where recipe_id = ?")) {
            deleteLikesStatement.setString(1, recipeId);
            deleteLikesStatement.executeUpdate();

            try (PreparedStatement deleteRelationStatement =
                       connection.prepareStatement("delete from recipetype where recipe_id = ?")) {
              deleteRelationStatement.setString(1, recipeId);
              deleteRelationStatement.executeUpdate();
              try (PreparedStatement deleteCommentsStatement = connection
                      .prepareStatement("delete from comment where recipe_id = ?")) {
                deleteCommentsStatement.setString(1, recipeId);
                deleteCommentsStatement.executeUpdate();
                try (PreparedStatement updateStatement = connection
                        .prepareStatement("update users_rating set rating = rating - 10 -" +
                                "(select rating from recipe where recipe_id = ? )"+
                                "where user_login = (select creator from recipe where recipe_id = ? and isconfirmed = true) ")) {
                  updateStatement.setString(1, recipeId);
                  updateStatement.setString(2, recipeId);
                  logger.info(updateStatement.toString());
                  updateStatement.executeUpdate();
                }
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
      }
    } catch (SQLException e) {
      logger.error(e.getMessage());
    }

    return result;
  }
}
