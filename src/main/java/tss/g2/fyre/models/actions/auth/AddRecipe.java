package tss.g2.fyre.models.actions.auth;

import io.javalin.http.UploadedFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.datastorage.DataStorage;
import tss.g2.fyre.models.entity.Roles;
import tss.g2.fyre.utils.StoreImage;

public class AddRecipe implements ActionAuth {
  private DataStorage dataStorage;
  private String recipeName;
  private String recipeComposition;
  private String cookingSteps;
  private Date publicationDate;
  private String selectedTypes;
  private String image;

  /**
   * Constructor.
   *
   * @param dataStorage data storage object
   * @param recipeName recipe name
   * @param recipeComposition composition of the recipe
   * @param cookingSteps recipe cooking steps
   * @param publicationDate recipe publication date
   * @param selectedTypes types that the moderator selects
   * @param image image of recipe
   */
  public AddRecipe(DataStorage dataStorage, String recipeName, String recipeComposition,
                   String cookingSteps, Date publicationDate,
                   String selectedTypes, UploadedFile image) {
    this.dataStorage = dataStorage;
    this.recipeName = recipeName;
    this.recipeComposition = recipeComposition;
    this.cookingSteps = cookingSteps;
    this.publicationDate = publicationDate;
    this.selectedTypes = selectedTypes;
    this.image = new StoreImage(image).store();
  }

  @Override
  public Answer getAnswer(String user, String role) {
    List<String> typesList = new ArrayList<>(Arrays.asList(selectedTypes.split("/")));

    try {
      return new Answer<>(true, dataStorage
              .addRecipe(recipeName, recipeComposition, cookingSteps, publicationDate,
                      typesList, image, user, !Roles.user.toString().equals(role)));
    } catch (Exception e) {
      return new Answer(false);
    }
  }
}
