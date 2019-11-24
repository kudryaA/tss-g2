package tss.g2.fyre.models.actions.auth;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.AnswerWithComment;
import tss.g2.fyre.models.datastorage.DataStorage;
import tss.g2.fyre.models.entity.Roles;

/**
 * Action class for change user banned status.
 */
public class ChangeBannedStatus implements ActionAuth {

  private DataStorage dataStorage;
  private String userLogin;

  /**
   * Constructor.
   * @param dataStorage data storage object
   * @param userLogin user login
   */
  public ChangeBannedStatus(DataStorage dataStorage, String userLogin) {
    this.dataStorage = dataStorage;
    this.userLogin = userLogin;
  }

  @Override
  public Answer getAnswer(String user, String role) {
    if (Roles.moderator.toString().equals(role) || Roles.admin.toString().equals(role)) {
      return new Answer<>(true, dataStorage.changeBannedStatus(userLogin));
    } else {
      return new AnswerWithComment(true, false,
              "You do not have permission to perform this operation.");
    }
  }
}