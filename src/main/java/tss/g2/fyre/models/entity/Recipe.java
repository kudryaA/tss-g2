package tss.g2.fyre.models.entity;

import java.util.Date;

public class Recipe {
  private String name;
  private String cookingSteps;
  private Date publicationDate;
  private int rating;

  /**
   * Constructor.
   *
   * @param name recipe name
   * @param cookingSteps recipe cooking steps
   * @param publicationDate recipe publication date
   * @param rating recipe reting
   */
  public Recipe(String name, String cookingSteps, Date publicationDate, int rating) {
    this.name = name;
    this.cookingSteps = cookingSteps;
    this.publicationDate = publicationDate;
    this.rating = rating;
  }

  /**
   * Method for get recipe name.
   * @return recipe name
   */
  public String getName() {
    return name;
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
  public int getRating() {
    return rating;
  }

  @Override
  public String toString() {
    return "Recipe{"
      + "name='" + name + '\''
      + ", cookingSteps='" + cookingSteps + '\''
      + ", publicationDate=" + publicationDate
      + ", rating=" + rating
      + '}';
  }
}
