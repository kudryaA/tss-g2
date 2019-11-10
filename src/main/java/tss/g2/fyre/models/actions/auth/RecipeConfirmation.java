package tss.g2.fyre.models.actions.auth;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.AnswerWithComment;
import tss.g2.fyre.models.datastorage.DataStorage;
import tss.g2.fyre.models.entity.Roles;

public class RecipeConfirmation implements ActionAuth {
  private DataStorage dataStorage;
  private String recipeId;

  /**
   * Constructor.
   * @param dataStorage data storage object
   * @param recipeId recipe id
   */
  public RecipeConfirmation(DataStorage dataStorage, String recipeId) {
    this.dataStorage = dataStorage;
    this.recipeId = recipeId;
  }

  @Override
  public Answer getAnswer(String login, String role) {
    if (Roles.moderator.toString().equals(role) || Roles.admin.toString().equals(role)) {
      return new Answer<>(true, dataStorage.recipeConfirmation(recipeId));
    } else {
      return new AnswerWithComment(true, false,
          "You do not have permission to perform this operation.");
    }
  }
}
