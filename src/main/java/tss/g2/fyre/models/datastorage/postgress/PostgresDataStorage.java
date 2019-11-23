package tss.g2.fyre.models.datastorage.postgress;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import tss.g2.fyre.models.datastorage.DataStorage;
import tss.g2.fyre.models.datastorage.postgress.utils.authorization.*;
import tss.g2.fyre.models.datastorage.postgress.utils.comment.AddComment;
import tss.g2.fyre.models.datastorage.postgress.utils.comment.SelectComments;
import tss.g2.fyre.models.datastorage.postgress.utils.recipe.*;
import tss.g2.fyre.models.datastorage.postgress.utils.service.AddTimeApiExecution;
import tss.g2.fyre.models.datastorage.postgress.utils.type.AddType;
import tss.g2.fyre.models.datastorage.postgress.utils.type.GetTypeInformation;
import tss.g2.fyre.models.entity.Comment;
import tss.g2.fyre.models.entity.Person;
import tss.g2.fyre.models.entity.Type;
import tss.g2.fyre.models.entity.recipe.Recipe;
import tss.g2.fyre.models.entity.recipe.RecipeWithType;

/**
 * Postgres data storage.
 *
 * @author Anton Kudryavtsev
 */
public class PostgresDataStorage implements DataStorage {

  private Connection connection;

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
  public String addRecipe(String recipeName, String recipeCompostion, String cookingSteps,
                           Date publicationDate, List<String> selectedTypes, String image,
                           String user, boolean isConfirmed) {
    return new AddRecipe(connection, recipeName, recipeCompostion,
            cookingSteps, publicationDate, selectedTypes, image, user, isConfirmed).addRecipe();
  }

  @Override
  public boolean addType(String typeName, String description, String image) {
    return new AddType(connection, typeName, description, image).addType();
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
  public boolean deleteRecipe(String recipeId, String user) {
    return new DeleteRecipe(connection, recipeId, user).deleteRecipe();
  }

  @Override
  public Recipe getRecipe(String recipeId) {
    return new GetRecipe(connection, recipeId).get();
  }

  @Override
  public Map<String, Object> selectRecipes(int pageNumber, int pageSize,
                                           String recipeType, String sortType) {
    return new SelectRecipes(connection, pageNumber, pageSize, recipeType, sortType)
            .selectRecipes();
  }

  @Override
  public boolean updateRecipe(String recipeId, String recipeName, String composition,
                              String cookingSteps, String creator) {
    return new UpdateRecipe(connection, recipeId, recipeName, composition, cookingSteps, creator)
            .updateRecipe();
  }

  @Override
  public Map<String, Object> searchRecipe(String ingredientName, int pageNumber, int pageSize) {
    return new SearchRecipe(connection, ingredientName, pageNumber, pageSize).searchRecipe();
  }

  @Override
  public String getRole(String login) {
    return new GetUserRole(connection, login).getRole();
  }

  @Override
  public List<RecipeWithType> selectUnconfirmedRecipes() {
    return new SelectUnconfirmedRecipes(connection).selectUnconfirmedRecipes();
  }

  @Override
  public boolean recipeConfirmation(String recipeId) {
    return new RecipeConfirmation(connection, recipeId).confirmation();
  }

  @Override
  public boolean addComment(String userLogin, String recipeId, String commentText) {
    return new AddComment(connection, userLogin, recipeId, commentText).addComment();
  }

  @Override
  public List<Comment> selectComments(String recipeId) {
    return new SelectComments(connection, recipeId).selectComments();
  }

  @Override
  public boolean addTimeApi(String api, long time) {
    return new AddTimeApiExecution(connection, api, time).add();
  }

  @Override
  public boolean addSubscribe(String user_login, String sub_login) {
    return new AddSubscribe(connection, user_login, sub_login).addSubscribe();
  }

  @Override
  public boolean deleteSubscribe(String user_login, String sub_login) {
    return new DeleteSubscribe(connection, user_login, sub_login).deleteSubscribe();
  }

  @Override
  public List<String> selectSubscribers(String login) {
    return new SelectSubscribers(connection, login).selectSubscribers();
  }

  /**
   * Close connection.
   */
  public void close() throws SQLException {
    connection.close();
  }
}
