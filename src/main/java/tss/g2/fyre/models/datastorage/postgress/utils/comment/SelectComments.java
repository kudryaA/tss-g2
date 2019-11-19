package tss.g2.fyre.models.datastorage.postgress.utils.comment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tss.g2.fyre.models.entity.Comment;

/**
 * Class for select comments by recipe id.
 */
public class SelectComments {
  private static Logger logger = LoggerFactory.getLogger(SelectComments.class);
  private Connection connection;
  private String recipeId;

  /**
   * Constructor.
   * @param connection connection to database
   * @param recipeId recipe id
   */
  public SelectComments(Connection connection, String recipeId) {
    this.connection = connection;
    this.recipeId = recipeId;
  }

  /**
   * Method for select all comments by recipe id.
   * @return comments list
   */
  public List<Comment> selectComments() {
    List<Comment> comments = new ArrayList<>();

    try (PreparedStatement selectCommentsStatement = connection
            .prepareStatement("select user_login, comment_text from comment where recipe_id = ?")) {
      selectCommentsStatement.setString(1, recipeId);
      logger.info(selectCommentsStatement.toString());

      try (ResultSet resultSet = selectCommentsStatement.executeQuery()) {
        while (resultSet.next()) {
          comments.add(new Comment(
                  resultSet.getString("user_login"),
                  resultSet.getString("comment_text")
          ));
        }
      }
    } catch (SQLException e) {
      logger.error(e.getMessage());
    }

    return comments;
  }
}
