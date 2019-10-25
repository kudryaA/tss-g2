package tss.g2.fyre.models.datastorage.postgress;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import tss.g2.fyre.models.datastorage.DataStorage;
import tss.g2.fyre.models.entity.Moderator;
import tss.g2.fyre.models.entity.Person;
import tss.g2.fyre.models.entity.Type;
import tss.g2.fyre.models.entity.recipe.Recipe;

/**
 * Postgres data storage.
 *
 * @author Anton Kudryavtsev
 */
public class PostgresDataStorage implements DataStorage {

  private Connection connection = null;

  /**
   * Constructor.
   * @throws SQLException sqlexception
   */
  public PostgresDataStorage(Properties properties) throws SQLException {
    String host = properties.getProperty("database_host");
    String port = properties.getProperty("database_port");
    String database = properties.getProperty("database_database");
    String user = properties.getProperty("database_user");
    String password = properties.getProperty("database_password");
    connection =
        DriverManager.getConnection(
            "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password);
  }

  @Override
  public Person getAuthorization(String login) {
    return new GetAuthorization(connection, login).getAuthorization();
  }

  @Override
  public boolean createUser(String login, String password, String name,
                                 String surname, String email) {
    return new Registration(connection, login, password, name, surname, email)
            .createUser();
  }
  
  @Override
  public Moderator getModerator(String login) {
    return new GetModerator(connection, login).getModerator();
  }

  @Override
  public boolean createModerator(String login, String password, String name) {
    return new RegistrationModerator(connection, login, password, name)
            .createModerator();
  }

  @Override
  public boolean addRecipe(String recipeName, String recipeCompostion, String cookingSteps,
                           Date publicationDate, List<String> selectedTypes, String image,
                           String user) {
    return new AddRecipe(connection, recipeName, recipeCompostion,
            cookingSteps, publicationDate, selectedTypes, image, user).addRecipe();
  }

  @Override
  public boolean addType(String typeName, String description) {
    return new AddType(connection, typeName, description).addType();
  }

  @Override
  public List<Person> getPersonsInformation() {
    return new GetUserInformation(connection).getUsersInformation();
  }

  @Override
  public List<Type> getTypesInformation() {
    return new GetTypeInformation(connection).getTypesInformation();
  }

  @Override
  public boolean changeBannedStatus(String userLogin) {
    return new AdminAction(connection, userLogin).changeBannedStatus();
  }

  @Override
  public boolean deleteRecipe(int recipeId, String user) {
    return new DeleteRecipe(connection, recipeId, user).deleteRecipe();
  }

  @Override
  public Recipe getRecipe(int recipeId) {
    return new GetRecipe(connection, recipeId).get();
  }

  @Override
  public Map<String, Object> selectRecipes(int pageNumber, int pageSize,
                                           String recipeType, String sortType) {
    return new SelectRecipes(connection, pageNumber, pageSize, recipeType, sortType)
            .selectRecipes();
  }

  @Override
  public boolean updateRecipe(int recipeId, String recipeName, String composition,
                              String cookingSteps, String creator) {
    return new UpdateRecipe(connection, recipeId, recipeName, composition, cookingSteps, creator)
            .updateRecipe();
  }

  @Override
  public List<Recipe> searchRecipe(String ingredientName) {
    return new SearchRecipe(connection, ingredientName).searchRecipe();
  }
}
