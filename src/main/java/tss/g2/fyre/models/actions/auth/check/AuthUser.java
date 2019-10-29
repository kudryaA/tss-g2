package tss.g2.fyre.models.actions.auth.check;

import java.util.Map;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.actions.auth.Action;
import tss.g2.fyre.models.entity.Authorization;

/**
 * Class for controlling auth action.
 * @author Anton Kudryavtsev
 */
public class AuthUser {
  protected Action action;
  protected String token;
  protected Map<String, Authorization> tokenStorage;

  /**
   * Constructor.
   * @param action action for check auth
   * @param token token of authorization
   * @param tokenStorage token storage
   */
  public AuthUser(Action action, String token, Map<String, Authorization> tokenStorage) {
    this.action = action;
    this.token = token;
    this.tokenStorage = tokenStorage;
  }

  /**
   * Get answer of action.
   * @return answer
   */
  public Answer getAnswer() {
    Authorization authorization = tokenStorage.get(token);
    Answer answer = null;
    if (authorization != null) {
      answer = action.getAnswer(authorization.getLogin());
    } else  {
      answer = new Answer(false);
    }
    return answer;
  }
}
