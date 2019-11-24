package tss.g2.fyre.models;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This class describe answer model.
 *
 * @param <T> type of additional info for answer
 * @author Anton Kudryavtsev
 */
public class Answer<T> {

  protected boolean status;
  protected T obj;

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
   * Get status.
   * @return status
   */
  public boolean isStatus() {
    return status;
  }

  /**
   * Get object.
   * @return object
   */
  public T getObj() {
    return obj;
  }

  /**
   * Method convert objects to json.
   *
   * @return answer in gson
   */
  public String toJson() {
    return new Gson().toJson(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Answer<?> answer = (Answer<?>) o;
    return status == answer.status
            && Objects.equals(obj, answer.obj);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, obj);
  }
}
