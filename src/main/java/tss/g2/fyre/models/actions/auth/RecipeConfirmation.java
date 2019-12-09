package tss.g2.fyre.models.actions.auth;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.AnswerWithComment;
import tss.g2.fyre.models.actions.SendEmailThread;
import tss.g2.fyre.models.datastorage.DataStorage;
import tss.g2.fyre.models.entity.Roles;
import tss.g2.fyre.models.entity.recipe.Recipe;

/**
 * Action class for confirmation recipes.
 */
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
      boolean result = dataStorage.recipeConfirmation(recipeId);
      if (result) {
        Recipe recipe = dataStorage.getRecipe(recipeId);
        Thread thread = new Thread(new SendEmailThread(dataStorage, recipe.getCreator(),
                                                       recipeId, recipe.getName()));
        thread.start();
      }
      return new Answer<>(true, result);
    } else {
      return new AnswerWithComment(true, false,
          "You do not have permission to perform this operation.");
    }
  }
}
