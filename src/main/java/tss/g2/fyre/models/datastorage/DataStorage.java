package tss.g2.fyre.models.datastorage;

import java.util.Date;

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
  boolean createNewPerson(String login, String password, String name, String surname, String email);

  /**
   * Method for adding new moderator.
   *
   * @param login moderator login
   * @param password moderator password
   * @param name moderator name
   * @return result of adding moderator
   */
  boolean createNewModerator(String login, String password, String name);

  /**
   * Method for adding new recipe.
   *
   * @param recipeName recipe name
   * @param cookingSteps recipe cooking steps
   * @param publicationDate recipe publication date
   * @param rating recipe rating
   * @return result of adding recipe
   */
  boolean addRecipe(String recipeName, String cookingSteps, Date publicationDate, int rating);
}
