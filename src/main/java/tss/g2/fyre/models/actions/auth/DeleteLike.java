package tss.g2.fyre.models.actions.auth;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.datastorage.DataStorage;

public class DeleteLike implements ActionAuth {
  private DataStorage dataStorage;
  private String recipeId;

  public DeleteLike(DataStorage dataStorage, String recipeId) {
    this.dataStorage = dataStorage;
    this.recipeId = recipeId;
  }

  @Override
  public Answer getAnswer(String login, String role) {
    return new Answer<>(true, dataStorage.deleteLike(login, recipeId));
  }
}
