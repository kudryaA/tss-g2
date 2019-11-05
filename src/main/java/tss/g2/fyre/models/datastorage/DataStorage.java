package tss.g2.fyre.models.datastorage;

import java.util.Date;
import java.util.List;
import java.util.Map;

import tss.g2.fyre.models.entity.Moderator;
import tss.g2.fyre.models.entity.Person;
import tss.g2.fyre.models.entity.Type;
import tss.g2.fyre.models.entity.recipe.Recipe;

/**
 * This interface describe data storage worker.
 *
 * @author Anton Kudryavtsev
 */
public interface DataStorage {

  
  /**
   * Get moderator by login.
   *
   * @param login login
   * @return moderator
   */
  Moderator getModerator(String login);
  
  /**
   * Get authorization info by login.
   *
   * @param login login
   * @return authorization info
   */
  Person getAuthorization(String login);

  /**
   * Method for adding new person.
   *
   * @param login user login
   * @param password user password
   * @param name user name
   * @param surname user surname
   * @param email user email
   * @return result of adding user
   */
  boolean createUser(String login, String password, String name, String surname, String email);

  /**
   * Method for adding new moderator.
   *
   * @param login moderator login
   * @param password moderator password
   * @param name moderator name
   * @return result of adding moderator
   */
  boolean createModerator(String login, String password, String name);

  /**
   * Method for adding new recipe.
   *
   * @param recipeName recipe name
   * @param recipeComposition composition of the recipe
   * @param cookingSteps recipe cooking steps
   * @param publicationDate recipe publication date
   * @param selectedTypes list with types that the moderator selects
   * @param image image of recipe
   * @param user owner of recipe
   * @return id of recipe
   */
  String addRecipe(String recipeName, String recipeComposition, String cookingSteps,
                    Date publicationDate, List<String> selectedTypes, String image,
                    String user);

  /**
   * Method for adding new type.
   *
   * @param typeName name of type
   * @param description type description
   * @return result of adding type
   */
  boolean addType(String typeName, String description);

  /**
   * Method for select persons information.
   *
   * @return list with persons info
   */
  List<Person> getPersonsInformation();

  /**
   * Method for select types information.
   *
   * @return list with types info
   */
  List<Type> getTypesInformation();

  /**
   * Method for change user banned status.
   * @param userLogin user login
   * @return result of changing status
   */
  boolean changeBannedStatus(String userLogin);

  /**
   * Method for removing the recipe.
   * @param recipeId recipe id
   * @param user authorization user
   * @return result of deleting
   */
  boolean deleteRecipe(int recipeId, String user);

  /**
   * Get recipe by id.
   * @param recipeId id of recipe
   * @return some recipe
   */
  Recipe getRecipe(int recipeId);

  /**
   * Method for get the requested information.
   *
   * @param pageNumber page number
   * @param pageSize page size
   * @param recipeType recipe type
   * @param sortType sort type
   * @return the requested information
   */
  Map<String, Object> selectRecipes(int pageNumber, int pageSize,
                                    String recipeType, String sortType);

  /**
   * Method for update recipe.
   * @param recipeId recipe id
   * @param recipeName recipe name
   * @param composition composition
   * @param cookingSteps cooking steps
   * @param creator user who create recipe
   * @return the result of the update
   */
  boolean updateRecipe(int recipeId, String recipeName, String composition,
                       String cookingSteps, String creator);

  /**
   * Method for search recipe by ingredient name.
   *
   * @param ingredientName ingredient name
   * @return the recipe found
   */
  List<Recipe> searchRecipe(String ingredientName);
}
