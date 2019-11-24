package tss.g2.fyre.models.actions.auth;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.AnswerWithComment;
import tss.g2.fyre.models.datastorage.DataStorage;
import tss.g2.fyre.models.entity.Roles;

/**
 * Action class for get users if you are a admin.
 */
public class GetUsers implements ActionAuth {
  private DataStorage dataStorage;

  /**
   * Constructor.
   *
   * @param dataStorage data storage object
   */
  public GetUsers(DataStorage dataStorage) {
    this.dataStorage = dataStorage;
  }

  @Override
  public Answer getAnswer(String auth, String role) {
    if (Roles.moderator.toString().equals(role) || Roles.admin.toString().equals(role)) {
      return new Answer<>(true, dataStorage.getPersonsInformation());
    } else {
      return new AnswerWithComment(true, false,
              "You do not have permission to perform this operation.");
    }
  }
}
