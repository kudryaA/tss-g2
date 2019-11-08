package tss.g2.fyre.models.datastorage.postgress;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecipeConfirmation {
  private Logger recipeConfirmationLogger = LoggerFactory.getLogger(RecipeConfirmation.class);
  private Connection connection;
  private int recipeId;

  /**
   * Constructor.
   * @param connection connection to database.
   * @param recipeId recipe id
   */
  public RecipeConfirmation(Connection connection, int recipeId) {
    this.connection = connection;
    this.recipeId = recipeId;
  }

  /**
   * Method for edit recipe confirmed status.
   * @return editing result
   */
  public boolean confirmation() {
    boolean result = false;

    try (PreparedStatement confirmationStatement = connection
            .prepareStatement("update recipe set isconfirmed = true where recipe_id = ?")) {
      confirmationStatement.setInt(1, recipeId);
      recipeConfirmationLogger.info(confirmationStatement.toString());

      result = confirmationStatement.executeUpdate() == 1;
    } catch (SQLException e) {
      recipeConfirmationLogger.error(e.getMessage());
    }

    return result;
  }
}
