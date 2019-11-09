package tss.g2.fyre.models.actions.auth;

import tss.g2.fyre.models.Answer;

/**
 * Action with auth.
 * @author Anton Kudryavtsev
 */
public interface ActionAuth {
  Answer getAnswer(String login, String role);
}