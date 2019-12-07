package tss.g2.fyre.test.models.datastorage.postgress;

import org.junit.*;
import tss.g2.fyre.models.datastorage.postgress.PostgresDataStorage;
import tss.g2.fyre.models.entity.Person;
import tss.g2.fyre.utils.Configuration;

import java.sql.*;
import java.util.Properties;

public class GetAuthorizationTestPostgres {

  private static Person testPerson;
  private static Properties properties = new Configuration("config/configuration.yml").getProperties();
  private static String host = properties.getProperty("database_host");
  private static String port = properties.getProperty("database_port");
  private static String database = properties.getProperty("database_database");
  private static String user = properties.getProperty("database_user");
  private static String password = properties.getProperty("database_password");


  @Before
  public void init() {
    try (Connection connection =
                 DriverManager.getConnection(
                         "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
      //password = a
      testPerson = new Person("john_test_1", "ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb",
              "john", "doe", false, "john@doe.com", "admin");
      try (PreparedStatement statement = connection.prepareStatement(
              "INSERT INTO person (login, password, name, surname, bannedstatus, email, role) " +
                      "VALUES ('john_test_1', 'ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb', 'john', " +
                      "'doe', false, 'john@doe.com', 'admin')")) {
        statement.execute();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGetAuthorization() throws SQLException {
    PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
    Person result = (Person) dataStorage.getAuthorization("john_test_1").get("authorization");
    Assert.assertEquals(testPerson, result);
    dataStorage.close();
  }

  @After
  public void finish() {
    try (Connection connection =
                 DriverManager.getConnection(
                         "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
      try (PreparedStatement statement = connection.prepareStatement(
              "DELETE FROM person WHERE login = 'john_test_1'")) {
        statement.execute();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}