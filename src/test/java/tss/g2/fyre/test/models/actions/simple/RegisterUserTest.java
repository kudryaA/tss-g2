package tss.g2.fyre.test.models.actions.simple;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.actions.simple.CheckAuthorization;
import tss.g2.fyre.models.actions.simple.RegisterUser;
import tss.g2.fyre.models.datastorage.postgress.PostgresDataStorage;
import tss.g2.fyre.utils.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class RegisterUserTest {
    private static Properties properties = new Configuration("config/configuration.yml").getProperties();
    private static String host = properties.getProperty("database_host");
    private static String port = properties.getProperty("database_port");
    private static String database = properties.getProperty("database_database");
    private static String user = properties.getProperty("database_user");
    private static String password = properties.getProperty("database_password");


    /*public void init() {
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
    }*/

    @Test
    public void testRegisterUser() throws SQLException {
        PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
        //Assert.assertEquals(testPerson, result);
        RegisterUser user = new RegisterUser(dataStorage, "bdfshbfdbsfbsjfbjsbdfjsbdjsbfd", "a", "john", "doe",
                "john@doe.com");
        Answer answer = user.getAnswer();
        System.out.println(answer);
        System.out.println(new Answer<>(true, true));
        Assert.assertEquals(new Answer<>(true, true), answer);
        answer = user.getAnswer();
        Assert.assertEquals(new Answer<>(true, false), answer);
        dataStorage.close();
    }

    @After
    public void finish() {
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM person WHERE login = 'bdfshbfdbsfbsjfbjsbdfjsbdjsbfd'")) {
                statement.execute();
            }
        } catch (SQLException e) {
        }
    }
}
