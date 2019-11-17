package tss.g2.fyre.models;

import java.util.Objects;

/**
 * This class describe answer with comment model.
 * @author Andrey Sherstyuk
 */
public class AnswerWithComment extends Answer {

  private String comment;

  /**
   * Constructor.
   * @param status status of answer
   * @param obj additional info for answer
   * @param comment comment for answer
   */
  public AnswerWithComment(boolean status, Object obj, String comment) {
    super(status, obj);
    this.comment = comment;
  }

  /**
   * Constructor.
   * @param status status of answer
   * @param comment comment for answer
   */
  public AnswerWithComment(boolean status, String comment) {
    super(status);
    this.comment = comment;
  }

  /**
   * Method for get comment.
     * @return answer comment
   */
  public String getComment() {
    return comment;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    AnswerWithComment that = (AnswerWithComment) o;
    return Objects.equals(comment, that.comment);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), comment);
  }
}
