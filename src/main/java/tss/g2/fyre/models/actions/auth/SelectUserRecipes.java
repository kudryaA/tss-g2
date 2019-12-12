package tss.g2.fyre.models.actions.auth;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.datastorage.DataStorage;

public class SelectUserRecipes implements ActionAuth {
  private DataStorage dataStorage;

  public SelectUserRecipes(DataStorage dataStorage) {
    this.dataStorage = dataStorage;
  }

  @Override
  public Answer getAnswer(String login, String role) {
    return new Answer<>(true, dataStorage.selectUserRecipes(login));
  }
}
