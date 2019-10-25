package tss.g2.fyre.models.entity.recipe;

import java.util.List;
import java.util.stream.Collectors;

import tss.g2.fyre.models.entity.Type;

/**
 * Class content information with name and type.
 */
public class RecipeWithType extends Recipe {

  protected List<String> types;

  /**
   * Constructor.
   *
   * @param recipe          recipe
   * @param types           type
   */
  public RecipeWithType(Recipe recipe, List<Type> types) {
    super(
        recipe.getId(),
        recipe.getName(),
        recipe.getComposition(),
        recipe.getCookingSteps(),
        recipe.getPublicationDate(),
        recipe.getImage(),
        recipe.getCreator(),
        recipe.getRating()
    );
    this.types = types.stream().map(Type::getTypeName).collect(Collectors.toList());
  }

  /**
   * Get types of recipe.
   * @return types
   */
  public List<String> getTypes() {
    return types;
  }

  @Override
  public String toString() {
    return "RecipeWithType{"
        + "type='" + types.toString() + '\''
        + ", name='" + name + '\''
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
