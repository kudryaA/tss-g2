package tss.g2.fyre.models.datastorage.postgress.utils.recipe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddLike {
  private static Logger logger = LoggerFactory.getLogger(AddLike.class);
  private Connection connection;
  private String login;
  private String recipeId;

  /**
   * Constructor.
   * @param connection connection to database
   * @param login user login
   * @param recipeId recipe id
   */
  public AddLike(Connection connection, String login, String recipeId) {
    this.connection = connection;
    this.login = login;
    this.recipeId = recipeId;
  }

  /**
   * Method for add like to recipe.
   * @return result of add
   */
  public boolean addLike() {
    boolean result = false;
    logger.info("Start add like recipe id = {}, login = {}.", recipeId, login);

    try (PreparedStatement checkStatement = connection
            .prepareStatement("select 1 from likes where user_login = ? and recipe_id = ?")) {
      checkStatement.setString(1, login);
      checkStatement.setString(2, recipeId);

      try (ResultSet resultSet = checkStatement.executeQuery()) {
        if (!resultSet.next()) {

          try (PreparedStatement addLikeStatement = connection
                .prepareStatement("insert into likes values (?, ?)")) {
            addLikeStatement.setString(1, login);
            addLikeStatement.setString(2, recipeId);

            result = addLikeStatement.executeUpdate() == 1;
          }
          try (PreparedStatement updateStatement = connection
                  .prepareStatement("UPDATE users_rating SET rating = rating + 2 WHERE user_login  = ?")) {
            updateStatement.setString(1, login);

            logger.info(updateStatement.toString());
            updateStatement.executeUpdate();
          }
          try (PreparedStatement newcheckStatement = connection
                  .prepareStatement("select 1 from likes join recipe on (user_login = ? and "
                          + "likes.recipe_id = recipe.recipe_id and user_login != creator and "
                          + "likes.recipe_id = ?)")) {
            newcheckStatement.setString(1, login);
            newcheckStatement.setString(2, recipeId);
            try (ResultSet newResultSet = newcheckStatement.executeQuery()) {
              if (newResultSet.next()) {
                try (PreparedStatement updateStatement = connection
                        .prepareStatement("UPDATE recipe SET rating = rating + 1 WHERE recipe_id = ?")) {
                  updateStatement.setString(1, recipeId);

                  logger.info(updateStatement.toString());
                  updateStatement.executeUpdate();
                }
                try (PreparedStatement updateStatement = connection
                        .prepareStatement("UPDATE users_rating SET rating = rating + 1 WHERE user_login  = " +
                                "(SELECT creator from recipe where recipe_id = ?)")) {
                  updateStatement.setString(1, recipeId);

                  logger.info(updateStatement.toString());
                  updateStatement.executeUpdate();
                }
              }
            }
          }
        }
      }
    } catch (SQLException e) {
      logger.error("Executing statement for add like complete with errors - {}", e.getMessage());
    }
    return result;
  }
}
