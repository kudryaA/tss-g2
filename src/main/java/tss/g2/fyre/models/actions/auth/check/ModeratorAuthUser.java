package tss.g2.fyre.models.actions.auth.check;

import java.util.Map;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.actions.auth.Action;
import tss.g2.fyre.models.entity.Authorization;

/**
 * Class for controlling auth moderator action.
 * @author Anton Kudryavtsev
 */
public class ModeratorAuthUser extends AuthUser {
  /**
   * Constructor.
   *
   * @param action       action for check auth
   * @param token        token of authorization
   * @param tokenStorage token storage
   */
  public ModeratorAuthUser(Action action, String token, Map<String, Authorization> tokenStorage) {
    super(action, token, tokenStorage);
  }

  /**
   * Get answer of action.
   * @return answer
   */
  public Answer getAnswer() {
    Authorization authorization = tokenStorage.get(token);
    Answer answer = null;
    if (authorization != null) {
      if (authorization.isModerator()) {
        answer = action.getAnswer(authorization.getLogin());
      } else {
        answer = new Answer(false);
      }
    } else  {
      answer = new Answer(false);
    }
    return answer;
  }
}
