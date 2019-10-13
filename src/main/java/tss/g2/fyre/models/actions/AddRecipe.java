package tss.g2.fyre.models.actions;

import java.util.Date;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.datastorage.DataStorage;

public class AddRecipe implements Action {
  private DataStorage dataStorage;
  private String recipeName;
  private String cookingSteps;
  private Date publicationDate;
  private int rating;

  /**
   * Constructor.
   *
   * @param dataStorage data storage object
   * @param recipeName recipe name
   * @param cookingSteps recipe cooking steps
   * @param publicationDate recipe publication date
   * @param rating recipe rating
   */
  public AddRecipe(DataStorage dataStorage, String recipeName, String cookingSteps,
                   Date publicationDate, int rating) {
    this.dataStorage = dataStorage;
    this.recipeName = recipeName;
    this.cookingSteps = cookingSteps;
    this.publicationDate = publicationDate;
    this.rating = rating;
  }

  @Override
  public Answer getAnswer() {
    return new Answer<>(true,
            dataStorage.addRecipe(recipeName, cookingSteps, publicationDate, rating));
  }
}
