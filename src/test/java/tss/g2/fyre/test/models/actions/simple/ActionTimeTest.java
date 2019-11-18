package tss.g2.fyre.test.models.actions.simple;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.actions.Action;
import tss.g2.fyre.models.actions.ActionTime;
import tss.g2.fyre.models.actions.simple.CheckAuthorization;
import tss.g2.fyre.models.actions.simple.RegisterUser;
import tss.g2.fyre.models.datastorage.postgress.PostgresDataStorage;
import tss.g2.fyre.utils.Configuration;

import java.sql.*;
import java.util.Properties;

public class ActionTimeTest {
    private static Properties properties = new Configuration("config/configuration.yml").getProperties();
    private static String host = properties.getProperty("database_host");
    private static String port = properties.getProperty("database_port");
    private static String database = properties.getProperty("database_database");
    private static String user = properties.getProperty("database_user");
    private static String password = properties.getProperty("database_password");


    @Test
    public void testActionTime() throws SQLException {
        PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
        //Assert.assertEquals(testPerson, result);
        RegisterUser userAction = new RegisterUser(dataStorage, "john_test_3", "a", "john", "doe",
                "john@doe.com");
        ActionTime actionTime = new ActionTime("testapi", userAction, dataStorage);
        Answer answer = actionTime.getAnswer();
        dataStorage.close();
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM api_time WHERE api = 'testapi'")) {
                ResultSet resultSet = statement.executeQuery();
                Assert.assertTrue(resultSet.next());
                resultSet.close();
            }
        } catch (SQLException e) {
        }
        Assert.assertEquals(true, answer.isStatus());
    }

    @After
    public void finish() {
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM api_time WHERE api = 'testapi'")) {
                statement.executeQuery();
            }
        } catch (SQLException e) {
        }
    }
}
