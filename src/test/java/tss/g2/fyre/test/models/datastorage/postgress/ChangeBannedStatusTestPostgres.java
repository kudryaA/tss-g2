package tss.g2.fyre.test.models.datastorage.postgress;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tss.g2.fyre.models.datastorage.postgress.PostgresDataStorage;
import tss.g2.fyre.models.entity.Person;
import tss.g2.fyre.utils.Configuration;

import java.sql.*;
import java.util.Properties;

public class ChangeBannedStatusTestPostgres {
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
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO person (login, password, name, surname, bannedstatus, email, role) " +
                            "VALUES ('john_test_1', 'ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb', 'john',\'" +
                            "doe', false, 'john@doe.com', 'admin'),('john_test_2'," +
                            "'ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb', 'john2', 'doe2', true," +
                            "'john@doe.com', 'user')")) {
                statement.executeQuery();
            }
        } catch (SQLException e) {
        }
    }

    @Test
    public void testChangeBannedStatus () throws SQLException {
        PostgresDataStorage dataStorage = new PostgresDataStorage(properties);
        boolean result1 = dataStorage.changeBannedStatus("john_test_1");
        boolean result2 = dataStorage.changeBannedStatus("john_test_2");
        try(Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM person WHERE login = 'john_test_1'")){
                try (ResultSet resultSet = statement.executeQuery()){
                    if (resultSet.next()) {
                        boolean bannedStatus = resultSet.getBoolean("bannedStatus");
                        Assert.assertEquals(true, bannedStatus);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try(Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM person WHERE login = 'john_test_2'")){
                try (ResultSet resultSet = statement.executeQuery()){
                    if (resultSet.next()) {
                        boolean bannedStatus = resultSet.getBoolean("bannedStatus");
                        Assert.assertEquals(false, bannedStatus);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(true, result1);
        Assert.assertEquals(true, result2);
        dataStorage.close();
    }

    @After
    public void finish() {
        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password)){
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM person WHERE login in ('john_test_1', 'john_test_2')")) {
                statement.executeQuery();
            }
        } catch (SQLException e) {
        }
    }
}
