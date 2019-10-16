package tss.g2.fyre.models.actions;

import java.util.Date;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.datastorage.DataStorage;

public class AddRecipe implements Action {
  private DataStorage dataStorage;
  private String recipeName;
  private String cookingSteps;
  private Date publicationDate;

  /**
   * Constructor.
   *
   * @param dataStorage data storage object
   * @param recipeName recipe name
   * @param cookingSteps recipe cooking steps
   * @param publicationDate recipe publication date
   */
  public AddRecipe(DataStorage dataStorage, String recipeName, String cookingSteps,
                   Date publicationDate) {
    this.dataStorage = dataStorage;
    this.recipeName = recipeName;
    this.cookingSteps = cookingSteps;
    this.publicationDate = publicationDate;
  }

  @Override
  public Answer getAnswer() {
    return new Answer<>(true,
            dataStorage.addRecipe(recipeName, cookingSteps, publicationDate));
  }
}
