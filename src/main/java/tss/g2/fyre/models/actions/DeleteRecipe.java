package tss.g2.fyre.models.actions;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.datastorage.DataStorage;

public class DeleteRecipe implements Action {

  private DataStorage dataStorage;
  private String recipeName;

  /**
   * Constructor.
   *
   * @param dataStorage data storage object
   * @param recipeName recipe name
   */
  public DeleteRecipe(DataStorage dataStorage, String recipeName) {
    this.dataStorage = dataStorage;
    this.recipeName = recipeName;
  }

  @Override
  public Answer getAnswer() {
    return new Answer<>(true, dataStorage.deleteRecipe(recipeName));
  }
}
