package tss.g2.fyre.models.entity;

import org.jetbrains.annotations.NotNull;
import java.util.Objects;

/**
 * Comment class.
 */
public class Comment implements Comparable<Comment>{

  private String userLogin;
  private String commentText;

  /**
   * Constructor.
   * @param userLogin user login
   * @param commentText commentText
   */
  public Comment(String userLogin, String commentText) {
    this.userLogin = userLogin;
    this.commentText = commentText;
  }

  /**
   * Method for get login of user who add comment.
   * @return user login
   */
  public String getUserLogin() {
    return userLogin;
  }

  /**
   * Method for get comment text.
   * @return comment text
   */
  public String getCommentText() {
    return commentText;
  }

  @Override
  public int compareTo(@NotNull Comment o) {
    return this.getUserLogin().compareTo(o.getUserLogin());
  }
  
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Comment comment = (Comment) o;
    return Objects.equals(userLogin, comment.userLogin)
            &&
            Objects.equals(commentText, comment.commentText);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userLogin, commentText);
  }
}
