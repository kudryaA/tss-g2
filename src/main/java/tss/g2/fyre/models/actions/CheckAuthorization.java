package tss.g2.fyre.models.actions;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.datastorage.DataStorage;
import tss.g2.fyre.models.entity.Person;

public class CheckAuthorization implements Action {
  private DataStorage dataStorage;
  private String login;
  private String password;

  public CheckAuthorization(DataStorage dataStorage, String login, String password) {
    this.dataStorage = dataStorage;
    this.login = login;
    this.password = password;
  }


  @Override
  public Answer getAnswer() {
    Person authorization = dataStorage.getAuthorization(login);
    boolean res = false;

    if (authorization == null) {
      return new Answer<>(false, res);
    }

    if (authorization.getLogin().equals(login) && authorization.getPassword().equals(password)) {
      res = true;
    }
    return new Answer<>(true, res);
  }
}
