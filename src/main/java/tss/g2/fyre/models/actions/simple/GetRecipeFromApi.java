package tss.g2.fyre.models.actions.simple;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.AnswerWithComment;
import tss.g2.fyre.models.actions.Action;
import tss.g2.fyre.models.datastorage.DataStorage;
import tss.g2.fyre.models.entity.recipe.Recipe;

public class GetRecipeFromApi implements Action {
  private DataStorage dataStorage;
  private String recipeId;
  private String key;

  /**
   * Constructor.
   * @param dataStorage DataStorage object
   * @param recipeId recipe id
   * @param key user key
   */
  public GetRecipeFromApi(DataStorage dataStorage, String recipeId, String key) {
    this.dataStorage = dataStorage;
    this.recipeId = recipeId;
    this.key = key;
  }

  @Override
  public Answer getAnswer() {
    Recipe recipe = dataStorage.getRecipeFromApi(recipeId, key);
    if (recipe != null) {
      return new Answer<>(true, recipe);
    } else {
      return new AnswerWithComment(true, false, "Invalid key. Try again.");
    }
  }
}
