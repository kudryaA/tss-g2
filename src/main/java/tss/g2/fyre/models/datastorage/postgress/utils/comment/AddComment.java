package tss.g2.fyre.models.datastorage.postgress.utils.comment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddComment {
  private static Logger logger = LoggerFactory.getLogger(AddComment.class);
  private Connection connection;
  private String userLogin;
  private int recipeId;
  private String commentText;

  /**
   * Constructor.
   * @param connection connection to database
   * @param userLogin user login
   * @param recipeId recipe id
   * @param commentText comment text
   */
  public AddComment(Connection connection, String userLogin, int recipeId, String commentText) {
    this.connection = connection;
    this.userLogin = userLogin;
    this.recipeId = recipeId;
    this.commentText = commentText;
  }

  /**
   * Method for add new comment to recipe.
   * @return result of adding
   */
  public boolean addComment() {
    boolean result = false;

    try (PreparedStatement addCommentStatement = connection
            .prepareStatement("insert into comment values(?, ?, ?)")) {
      addCommentStatement.setString(1, userLogin);
      addCommentStatement.setInt(2, recipeId);
      addCommentStatement.setString(3, commentText);
      logger.info(addCommentStatement.toString());

      result = addCommentStatement.executeUpdate() == 1;
    } catch (SQLException e) {
      logger.error(e.getMessage());
    }

    return result;
  }
}
