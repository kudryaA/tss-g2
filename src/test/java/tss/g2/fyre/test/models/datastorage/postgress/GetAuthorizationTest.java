package tss.g2.fyre.test.models.datastorage.postgress;

import org.junit.*;
import tss.g2.fyre.models.datastorage.postgress.PostgresDataStorage;
import tss.g2.fyre.models.entity.Person;
import tss.g2.fyre.utils.Configuration;

import java.sql.*;
import java.util.Properties;

public class GetAuthorizationTest {

  private static Person testPerson;

  @Before
  public void init() {
    Properties properties = new Configuration("config/configuration.yml").getProperties();
    String host = properties.getProperty("database_host");
    String port = properties.getProperty("database_port");
    String database = properties.getProperty("database_database");
    String user = properties.getProperty("database_user");
    String password = properties.getProperty("database_password");
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
        statement.executeQuery();
      }
    } catch (SQLException e) {
    }
  }

  @Test
  public void testGetAuthorization() throws SQLException {
    Properties properties = new Configuration("config/configuration.yml").getProperties();
    PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
    Person result = dataStorage.getAuthorization("john_test_1");
    Assert.assertEquals(result, testPerson);
    dataStorage.close();
  }

  @After
  public void finish() {
    Properties properties = new Configuration("config/configuration.yml").getProperties();
    String host = properties.getProperty("database_host");
    String port = properties.getProperty("database_port");
    String database = properties.getProperty("database_database");
    String user = properties.getProperty("database_user");
    String password = properties.getProperty("database_password");
    try (Connection connection =
             DriverManager.getConnection(
                 "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
      try (PreparedStatement statement = connection.prepareStatement(
                          "DELETE FROM person WHERE login = 'john_test_1'")) {
        statement.executeQuery();
      }
    } catch (SQLException e) {
    }
  }
}
