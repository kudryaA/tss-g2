package tss.g2.fyre.models.actions.auth;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.AnswerWithComment;
import tss.g2.fyre.models.datastorage.DataStorage;
import tss.g2.fyre.models.entity.Roles;

public class GetDashboard implements ActionAuth {
  private DataStorage dataStorage;

  /**
   * Constructor.
   * @param dataStorage DataStorage object
   */
  public GetDashboard(DataStorage dataStorage) {
    this.dataStorage = dataStorage;
  }

  @Override
  public Answer getAnswer(String login, String role) {
    if (Roles.admin.toString().equals(role)) {
      return new Answer<>(true, dataStorage.getDashboard(login));
    } else {
      return new AnswerWithComment(true, false,
              "You do not have permission to perform this operation.");
    }
  }
}
