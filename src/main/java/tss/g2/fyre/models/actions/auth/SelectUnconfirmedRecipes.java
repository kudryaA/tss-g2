package tss.g2.fyre.models.actions.auth;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.AnswerWithComment;
import tss.g2.fyre.models.datastorage.DataStorage;
import tss.g2.fyre.models.entity.Roles;

public class SelectUnconfirmedRecipes implements ActionAuth {
  private DataStorage dataStorage;

  /**
   * Constructor.
   * @param dataStorage data storage object
   */
  public SelectUnconfirmedRecipes(DataStorage dataStorage) {
    this.dataStorage = dataStorage;
  }

  @Override
  public Answer getAnswer(String login, String role) {
    if (Roles.moderator.toString().equals(role) || Roles.admin.toString().equals(role)) {
      return new Answer<>(true, dataStorage.selectUnconfirmedRecipes());
    } else {
      return new AnswerWithComment(true, false,
              "You do not have permission to perform this operation.");
    }
  }
}
