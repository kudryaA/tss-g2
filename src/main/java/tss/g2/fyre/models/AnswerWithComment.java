package tss.g2.fyre.models;

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
}
