package tss.g2.fyre.models.datastorage;

import java.util.Date;
import java.util.List;

import tss.g2.fyre.models.entity.Moderator;
import tss.g2.fyre.models.entity.Person;

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
   * @return result of adding recipe
   */
  boolean addRecipe(String recipeName, String recipeComposition, String cookingSteps,
                    Date publicationDate, List<String> selectedTypes);

  /**
   * Method for adding new type.
   *
   * @param typeName name of type
   * @param description type description
   * @return result of adding type
   */
  boolean addType(String typeName, String description);

  /**
   * Method for change user banned status.
   * @param userLogin user login
   * @return result of changing status
   */
  boolean changeBannedStatus(String userLogin);
}
