package tss.g2.fyre.models.actions.auth;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.datastorage.DataStorage;

public class AddLike implements ActionAuth {
  private DataStorage dataStorage;
  private String recipeId;

  /**
   * Constructor.
   * @param dataStorage data storage object
   * @param recipeId recipe id
   */
  public AddLike(DataStorage dataStorage, String recipeId) {
    this.dataStorage = dataStorage;
    this.recipeId = recipeId;
  }

  @Override
  public Answer getAnswer(String login, String role) {
    return new Answer<>(true, dataStorage.addLike(login, recipeId));
  }
}
