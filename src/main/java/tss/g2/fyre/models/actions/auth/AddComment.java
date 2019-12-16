package tss.g2.fyre.models.actions.auth;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.AnswerWithComment;
import tss.g2.fyre.models.datastorage.DataStorage;

/**
 * Action class for add comment to recipe.
 */
public class AddComment implements ActionAuth {
  private DataStorage dataStorage;
  private String recipeId;
  private String commentText;

  /**
   * Construct.
   * @param dataStorage data storage object
   * @param recipeId recipe id
   * @param commentText comment text
   */
  public AddComment(DataStorage dataStorage, String recipeId, String commentText) {
    this.dataStorage = dataStorage;
    this.recipeId = recipeId;
    this.commentText = commentText.replace("<", "&lt");
  }

  @Override
  public Answer getAnswer(String login, String role) {
    if ("".equals(commentText)) {
      return new AnswerWithComment(true, false,
              "The comment text field must not be empty.");
    }

    return new Answer<>(true, dataStorage.addComment(login, recipeId, commentText));
  }

}
