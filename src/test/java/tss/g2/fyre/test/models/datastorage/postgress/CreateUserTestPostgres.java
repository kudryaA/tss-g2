package tss.g2.fyre.test.models.datastorage.postgress;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tss.g2.fyre.models.datastorage.postgress.PostgresDataStorage;
import tss.g2.fyre.utils.Configuration;

import java.sql.*;
import java.util.Properties;

public class CreateUserTestPostgres {

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
    public void testCreateUser() throws SQLException {
        PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
        boolean result = dataStorage.createUser("john_test_2", "ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb", "john", "doe", "john@doe.com", "some_key");
        try(Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM person WHERE login = 'john_test_2'")){
                try (ResultSet resultSet = statement.executeQuery()){
                    if (resultSet.next()) {
                        String login = resultSet.getString("login");
                        String password = resultSet.getString("password");
                        String name = resultSet.getString("name");
                        String surname = resultSet.getString("surname");
                        String email = resultSet.getString("email");
                        boolean bannedStatus = resultSet.getBoolean("bannedStatus");
                        String role = resultSet.getString("role");
                        Assert.assertEquals("john_test_2", login);
                        Assert.assertEquals("ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb", password);
                        Assert.assertEquals("john", name);
                        Assert.assertEquals("doe", surname);
                        Assert.assertEquals("john@doe.com", email);
                        Assert.assertEquals(false, bannedStatus);
                        Assert.assertEquals("user", role);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(true, result);
        dataStorage.close();
    }

    @Test
    public void testCreateUserExist() throws SQLException {
        PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
        boolean result = dataStorage.createUser("john_test_1",
                "ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb", "john",
                "doe", "john@doe.com", "some_key");
        Assert.assertEquals(false, result);
        dataStorage.close();
    }

    @After
    public void finish() {
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM person WHERE login in ('john_test_2', 'john_test_1')")) {
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
