package tss.g2.fyre.models.entity.recipe;

import java.util.Date;

public class Recipe {
  protected String name;
  protected String composition;
  protected String cookingSteps;
  protected Date publicationDate;
  protected String image;
  protected String creator;
  protected int id;
  protected long rating;

  /**
   * Constructor.
   *
   * @param name recipe name
   * @param composition composition of the recipe
   * @param cookingSteps recipe cooking steps
   * @param publicationDate recipe publication date
   * @param image path to image
   * @param id id of recipe
   * @param creator author of recipe
   * @param rating recipe rating
   */
  public Recipe(int id, String name, String composition, String cookingSteps,
                Date publicationDate, String image, String creator, long rating) {
    this.name = name;
    this.composition = composition;
    this.cookingSteps = cookingSteps;
    this.publicationDate = publicationDate;
    this.rating = rating;
    this.image = image;
    this.id = id;
    this.creator = creator;
  }

  /**
   * Method for get recipe name.
   * @return recipe name
   */
  public String getName() {
    return name;
  }

  /**
   * Method for get composition of the recipe.
   * @return composition of the recipe
   */
  public String getComposition() {
    return composition;
  }

  /**
   * Method for get recipe cooking steps.
   * @return recipe cooking steps
   */
  public String getCookingSteps() {
    return cookingSteps;
  }

  /**
   * Method for get recipe publication date.
   * @return recipe publication date
   */
  public Date getPublicationDate() {
    return publicationDate;
  }

  /**
   * Method for get recipe rating.
   * @return recipe rating
   */
  public long getRating() {
    return rating;
  }

  /**
   * Method for get path to image.
   * @return path to image.
   */
  public String getImage() {
    return image;
  }

  /**
   * Method for get creator.
   * @return creator
   */
  public String getCreator() {
    return creator;
  }

  /**
   * Get id of recipe.
   * @return id
   */
  public int getId() {
    return id;
  }

  @Override
  public String toString() {
    return "Recipe{"
        + "name='" + name + '\''
        + ", composition='" + composition + '\''
        + ", cookingSteps='" + cookingSteps + '\''
        + ", publicationDate=" + publicationDate
        + ", image='" + image + '\''
        + ", creator='" + creator + '\''
        + ", id=" + id
        + ", rating=" + rating
        + '}';
  }
}
