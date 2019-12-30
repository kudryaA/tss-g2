package tss.g2.fyre.models.datastorage.postgress.utils.recipe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteLike {
  private Connection connection;
  private String login;
  private String recipeId;

  public DeleteLike(Connection connection, String login, String recipeId) {
    this.connection = connection;
    this.login = login;
    this.recipeId = recipeId;
  }

  public boolean deleteLike() {
    boolean result = false;

    try (PreparedStatement deleteStatement = connection
            .prepareStatement("delete from likes where recipe_id = ? and user_login = ?")) {
      deleteStatement.setString(1, recipeId);
      deleteStatement.setString(2, login);

      result = deleteStatement.executeUpdate() == 1;
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return result;
  }
}
