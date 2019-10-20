package tss.g2.fyre.models.datastorage.postgress;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import tss.g2.fyre.models.datastorage.DataStorage;
import tss.g2.fyre.models.entity.Moderator;
import tss.g2.fyre.models.entity.Person;
import tss.g2.fyre.models.entity.Type;

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
    return new PostgresGetAuthorization(connection, login).getAuthorization();
  }

  @Override
  public boolean createUser(String login, String password, String name,
                                 String surname, String email) {
    return new PostgresRegistration(connection, login, password, name, surname, email)
            .createUser();
  }
  
  @Override
  public Moderator getModerator(String login) {
    return new PostgresGetModerator(connection, login).getModerator();
  }

  @Override
  public boolean createModerator(String login, String password, String name) {
    return new PostgresRegistrationModerator(connection, login, password, name)
            .createModerator();
  }

  @Override
  public boolean addRecipe(String recipeName, String recipeCompostion, String cookingSteps,
                           Date publicationDate, List<String> selectedTypes, String image) {
    return new PostgresAddRecipe(connection, recipeName, recipeCompostion,
            cookingSteps, publicationDate, selectedTypes, image).addRecipe();
  }

  @Override
  public boolean addType(String typeName, String description) {
    return new PostgresAddType(connection, typeName, description).addType();
  }

  @Override
  public List<Person> getPersonsInformation() {
    return new PostgresGetUserInformation(connection).getUsersInformation();
  }

  @Override
  public List<Type> getTypesInformation() {
    return new PostgresGetTypeInformation(connection).getTypesInformation();
  }
}
