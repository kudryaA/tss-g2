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
        }
      }
    } catch (SQLException e) {
      logger.error("Executing statement for add like complete with errors - {}", e.getMessage());
    }

    return result;
  }
}
