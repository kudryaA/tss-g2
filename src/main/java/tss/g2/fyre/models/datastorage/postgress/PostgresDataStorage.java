package tss.g2.fyre.models.datastorage.postgress;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import tss.g2.fyre.models.datastorage.DataStorage;
import tss.g2.fyre.models.entity.Person;

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
    String host = properties.getProperty("host");
    String port = properties.getProperty("port");
    String database = properties.getProperty("database");
    String user = properties.getProperty("user");
    String password = properties.getProperty("password");
    connection =
        DriverManager.getConnection(
            "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password);
  }

  @Override
  public Person getAuthorization(String login) {
    return new PostgresGetAuthorization(connection, login).getAuthorization();
  }
}
