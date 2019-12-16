package tss.g2.fyre.test.models.datastorage.postgress;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tss.g2.fyre.models.datastorage.postgress.PostgresDataStorage;
import tss.g2.fyre.utils.Configuration;

import java.sql.*;
import java.text.ParseException;
import java.util.Properties;

public class ChangePasswordTestPostgres {
    private static Properties properties = new Configuration("config/configuration.yml").getProperties();
    private static String host = properties.getProperty("database_host");
    private static String port = properties.getProperty("database_port");
    private static String database = properties.getProperty("database_database");
    private static String user = properties.getProperty("database_user");
    private static String password = properties.getProperty("database_password");

    @Before
    public void init() throws ParseException {
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO person (login, password, name, surname, bannedstatus, email, role) " +
                            "VALUES ('test_user1', 'a', 'john', " +
                            "'doe', false, 'john@doe.com', 'user')")) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testChangePassword() throws SQLException {
        PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
        String result = dataStorage.changePassword("b","test_user1");
        Assert.assertEquals("ok", result);
        try(Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM person WHERE login = 'test_user1'")){
                try (ResultSet resultSet = statement.executeQuery()){
                    if (resultSet.next()) {
                        String login = resultSet.getString("login");
                        String password = resultSet.getString("password");
                        String name = resultSet.getString("name");
                        String surname = resultSet.getString("surname");
                        boolean bannedStatus = resultSet.getBoolean("bannedStatus");
                        String email = resultSet.getString("email");
                        String role = resultSet.getString("role");
                        Assert.assertEquals("test_user1",  login);
                        Assert.assertEquals("b", password);
                        Assert.assertEquals("john", name);
                        Assert.assertEquals("doe", surname);
                        Assert.assertEquals(false, bannedStatus);
                        Assert.assertEquals("john@doe.com", email);
                        Assert.assertEquals("user", role);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dataStorage.close();
    }

    @After
    public void finish() {
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement1 = connection.prepareStatement(
                    "DELETE FROM person where login in ('test_user1', 'test_user2', 'test_user3')")) {
                statement1.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
