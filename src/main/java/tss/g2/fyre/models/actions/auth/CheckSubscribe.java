package tss.g2.fyre.models.actions.auth;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.datastorage.DataStorage;

public class CheckSubscribe implements ActionAuth {
  private DataStorage dataStorage;
  private String sub_login;

    /**
     * Constructor.
     * @param dataStorage data storage object
     * @param sub_login user login to which this user is subscribed
     */
  public CheckSubscribe(DataStorage dataStorage, String sub_login) {
    this.dataStorage = dataStorage;
    this.sub_login = sub_login;
  }

  @Override
  public Answer getAnswer(String login, String role) {
    return new Answer<>(true, dataStorage.checkSubscribe(login, sub_login));
  }
}
