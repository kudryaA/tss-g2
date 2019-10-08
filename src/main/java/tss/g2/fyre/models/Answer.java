package tss.g2.fyre.models;

import com.google.gson.Gson;

/**
 * This class describe answer model.
 *
 * @param <T> type of additional info for answer
 * @author Anton Kudryavtsev
 */
public class Answer<T> {

  private boolean status;
  private T obj;

  /**
   * Constructor.
   *
   * @param status status of answer
   * @param obj additional info for answer
   */
  public Answer(boolean status, T obj) {
    this.status = status;
    this.obj = obj;
  }

  /**
   * Constructor.
   *
   * @param status status of answer
   */
  public Answer(boolean status) {
    this.status = status;
    this.obj = null;
  }

  /**
   * Method convert objects to json.
   *
   * @return answer in gson
   */
  public String toJson() {
    return new Gson().toJson(this);
  }
}
