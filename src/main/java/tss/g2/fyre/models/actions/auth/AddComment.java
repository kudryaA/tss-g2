package tss.g2.fyre.models.actions.auth;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.datastorage.DataStorage;

public class AddComment implements ActionAuth {
  private DataStorage dataStorage;
  private int recipeId;
  private String commentText;

  /**
   * Construct.
   * @param dataStorage data storage object
   * @param recipeId recipe id
   * @param commentText comment text
   */
  public AddComment(DataStorage dataStorage, int recipeId, String commentText) {
    this.dataStorage = dataStorage;
    this.recipeId = recipeId;
    this.commentText = commentText;
  }

  @Override
  public Answer getAnswer(String login, String role) {
    return new Answer<>(true, dataStorage.addComment(login, recipeId, commentText));
  }

}
